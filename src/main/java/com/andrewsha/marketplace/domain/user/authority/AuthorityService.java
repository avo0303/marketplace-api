package com.andrewsha.marketplace.domain.user.authority;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.andrewsha.marketplace.exception.UserServiceException;

@Service
public class AuthorityService {
	@Autowired
	private AuthorityRepository authorityRepository;
	@Autowired
	private AuthoritySetRepository authoritySetRepository;

	public Authority findPermission(Long id) {
		return this.authorityRepository.findById(id).orElseThrow(
				() -> new UserServiceException("permission with id " + id + " does not exists"));
	}

	public Authority findPermissionByName(String name) {
		return this.authorityRepository.findByName(name).orElseThrow(() -> new UserServiceException(
				"permission with name " + name + " does not exists"));
	}

	public AuthoritySet findPermissionSet(AuthoritySet set) {
		return this.authoritySetRepository.findOne(Example.of(set)).orElseThrow(
				() -> new UserServiceException("given permission set does not exists in database"));
	}

	public Set<Authority> getPermissions(Set<AuthorityEnum> actions, Class<? extends Object> type) {
		Set<Authority> permissions = new HashSet<>();
		for (AuthorityEnum action : actions) {
			Authority permission = new Authority(action, type);
			Optional<Authority> optional =
					this.authorityRepository.findByName(permission.getName());
			if (!optional.isPresent()) {
				permissions.add(this.authorityRepository.save(permission));
			} else {
				permissions.add(optional.get());
			}
		}
		return permissions;
	}

	public AuthoritySet save(AuthoritySet authoritySet) {
		return this.authoritySetRepository.save(authoritySet);
	}

	// public PermissionSet getPermissionSet(String scope, Set<Permission> permissions) {
	// List<PermissionSet> userPermissionsList = this.userPermissionRepository.findByScope(scope);
	// if (!userPermissionsList.isEmpty()) {
	// for (PermissionSet set : userPermissionsList) {
	// if (set.getPermissions().equals(permissions)) {
	// return set;
	// }
	// }
	// }
	// PermissionSet newSet = new PermissionSet();
	// newSet.setScope(scope);
	// newSet.setPermissions(permissions);
	// return this.userPermissionRepository.save(newSet);
	// }
}
