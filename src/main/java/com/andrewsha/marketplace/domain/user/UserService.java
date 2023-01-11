package com.andrewsha.marketplace.domain.user;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.regex.Pattern;
import com.andrewsha.marketplace.config.Config;
import com.andrewsha.marketplace.domain.user.authority.Authority;
import com.andrewsha.marketplace.domain.user.authority.AuthorityService;
import com.andrewsha.marketplace.domain.user.authority.AuthoritySet;
import com.andrewsha.marketplace.domain.user.authority.Role;
import com.andrewsha.marketplace.domain.user.authority.RoleEnum;
import com.andrewsha.marketplace.domain.user.authority.RoleRepository;
import com.andrewsha.marketplace.domain.user.request.AddAuthorityForm;
import com.andrewsha.marketplace.domain.user.request.CreateUserForm;
import com.andrewsha.marketplace.domain.user.request.UpdateUserForm;
import com.andrewsha.marketplace.exception.UserServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthorityService permissionService;

	public User getUser(UUID id) {
		return this.userRepository.findById(id).orElseThrow(
				() -> new UserServiceException("user with id " + id + " does not exists"));
	}

	public Role getRole(Long id) {
		return this.roleRepository.findById(id).orElseThrow(
				() -> new UserServiceException("role with id " + id + " does not exists"));
	}

	@Override
	// @Cacheable(key = "#username", value = "loadByUsernameCache")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (Pattern.matches(Config.emailRegexp, username)) {
			return this.userRepository.findByEmail(username)
					.orElseThrow(() -> new UserServiceException("wrong email"));
		} else {
			if (Pattern.matches(Config.phoneNumberRegexp, username)) {
				return this.userRepository.findByPhoneNumber(username)
						.orElseThrow(() -> new UserServiceException("wrong phone number"));
			} else {
				throw new UserServiceException(
						"username " + username + " cannot be resolved to email or phone number");
			}
		}
	}

	public Page<User> getUsers(int page, int size) {
		return this.userRepository.findAll(PageRequest.of(page, size));
	}

	@Transactional
	public User createUser(CreateUserForm userDetails) {
		if (userDetails.getEmail() != null) {
			if (this.userRepository.findByEmail(userDetails.getEmail()).isPresent()) {
				throw new UserServiceException("email taken");
			}
		}
		if (userDetails.getPhoneNumber() != null) {
			if (this.userRepository.findByPhoneNumber(userDetails.getPhoneNumber()).isPresent()) {
				throw new UserServiceException("phone number taken");
			}
		}
		User user = new User();
		user.setName(userDetails.getName());
		user.setEmail(userDetails.getEmail());
		user.setPhoneNumber(userDetails.getPhoneNumber());
		user.setDob(userDetails.getDob());
		user.setProfileIcon(userDetails.getProfileIcon());

		user.addAuthority(this.roleRepository.findByName(RoleEnum.ROLE_USER)
				.orElseThrow(() -> new UserServiceException("role USER does not exists")));
		user.setPassword(this.passwordEncoder.encode(userDetails.getPassword()));
		return this.userRepository.save(user);
	}

	@Transactional
	public User addRole(UUID userId, Long roleId, Authentication authentication) {
		if (authentication == null) {
			throw new UserServiceException("not authenticated");
		}
		User authenticatedUser = (User) authentication.getPrincipal();
		Role role = this.getRole(roleId);

		// TODO replace b.logic by permission evaluator
		if (role.getName().equals(RoleEnum.ROLE_SUPER_ADMIN)
				&& !authenticatedUser.hasRole(RoleEnum.ROLE_SUPER_ADMIN)) {
			throw new AccessDeniedException("access denied: cannot add role to user");
		}

		User targetUser = this.getUser(userId);

		// TODO addAuthority method working only for roles now
		targetUser.addAuthority(role);

		return this.userRepository.save(targetUser);
	}

	@Transactional
	public User addPermission(UUID userId, AddAuthorityForm form) {
		User user = this.getUser(userId);
		Authority permission = (form.getAuthorityId() != null)
				? this.permissionService.findPermission(form.getAuthorityId())
				: this.permissionService.findPermissionByName(form.getAuthorityName());
		return this.addAuthority(user, permission, form.getScope());
	}

	@Transactional
	public User addAuthority(User user, Authority permission, String scope) {
		AuthoritySet newSet = new AuthoritySet();
		for (AuthoritySet set : user.getAuthoritySets()) {
			// move old permissions with this scope to new set
			if (set.getScope().equals(scope)) {
				newSet.setAuthoritySet(set.getAuthoritySet());
				// remove old set
				user.getAuthoritySets().remove(set);
			}
		}
		// add new permission
		newSet.getAuthoritySet().add(permission);
		newSet.setScope(scope);
		try {
			// link to existing set with this scope and authorities
			user.addAuthoritySet(this.permissionService.findPermissionSet(newSet));
			return this.userRepository.save(user);
		} catch (UserServiceException e) {
			// create new set
			// replace by new set
			// TODO check cascade
			newSet.getUsers().add(user);
			user.addAuthoritySet(this.permissionService.save(newSet));
			return this.userRepository.save(user);
		}
	}

	@Transactional
	public User patchUser(UUID id, UpdateUserForm userDetails) {
		User user = this.getUser(id);
		if (userDetails.getEmail() != null) {
			user.setEmail(userDetails.getEmail());
		}
		if (userDetails.getName() != null) {
			user.setEmail(userDetails.getName());
		}
		if (userDetails.getPassword() != null) {
			user.setEmail(userDetails.getPassword());
		}
		if (userDetails.getPhoneNumber() != null) {
			user.setEmail(userDetails.getPhoneNumber());
		}
		if (userDetails.getProfileIcon() != null) {
			user.setEmail(userDetails.getProfileIcon());
		}
		return user;
	}

	@Transactional
	public User putUser(UUID id, UpdateUserForm userDetails) {
		User user = this.getUser(id);
		user.setName(userDetails.getName());
		user.setEmail(userDetails.getEmail());
		user.setPhoneNumber(userDetails.getPhoneNumber());
		user.setProfileIcon(userDetails.getProfileIcon());
		return this.userRepository.save(user);
	}

	public void deleteUser(UUID id) {
		if (!(this.userRepository.existsById(id))) {
			throw new NoSuchElementException("user with id " + id + "does not exists");
		}
		this.userRepository.deleteById(id);
	}

	public User getAuthenticatedUser(Authentication authentication) {
		if (authentication != null) {
			return (User) authentication.getPrincipal();
		} else {
			throw new UserServiceException("not authenticated");
		}
	}
}
