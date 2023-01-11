package com.andrewsha.marketplace.domain.user;

import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import com.andrewsha.marketplace.domain.user.request.CreateUserForm;
import com.andrewsha.marketplace.domain.user.request.UpdateUserForm;
import com.andrewsha.marketplace.utils.DTOBuilder;
import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.toedter.spring.hateoas.jsonapi.MediaTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/user")
@Validated
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private DTOBuilder<User> builder;

	@GetMapping(produces = MediaTypes.JSON_API_VALUE)
	@PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
	// TODO get params from request ?
	public ResponseEntity<?> getUsers(
			@RequestParam(value = "page[number]", required = true) @Min(0) int page,
			@RequestParam(value = "page[size]", required = true) @Min(1) int size) {
		return ResponseEntity.ok(this.builder.build(this.userService.getUsers(page, size)));
	}

	@GetMapping(path = "/{id}", produces = MediaTypes.JSON_API_VALUE)
	@PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
	public ResponseEntity<?> getUser(@PathVariable("id") UUID id) {
		return ResponseEntity.ok(this.builder.build(this.userService.getUser(id)));
	}

	@GetMapping(path = "/me", produces = MediaTypes.JSON_API_VALUE)
	public ResponseEntity<?> getAuthenticatedUser(Authentication authentication) {
		return ResponseEntity
				.ok(this.builder.build(this.userService.getAuthenticatedUser(authentication)));
	}

	@PostMapping(produces = MediaTypes.JSON_API_VALUE)
	public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserForm user) {
		return ResponseEntity.ok(this.builder.build(this.userService.createUser(user)));
	}

	@PatchMapping(path = "{id}", produces = MediaTypes.JSON_API_VALUE)
	@PreAuthorize("#id == authentication.principal.id or hasRole('SUPER_ADMIN')")
	public ResponseEntity<?> patchUser(@PathVariable("id") UUID id,
			@Valid @RequestBody UpdateUserForm userDetails) {
		return ResponseEntity.ok(this.builder.build(this.userService.patchUser(id, userDetails)));
	}

	@PutMapping(path = "{id}", produces = MediaTypes.JSON_API_VALUE)
	@PreAuthorize("#id == authentication.principal.id")
	public ResponseEntity<?> putUser(@PathVariable("id") UUID id,
			@Valid @RequestBody UpdateUserForm userDetails) {
		return ResponseEntity.ok(this.builder.build(this.userService.putUser(id, userDetails)));
	}

	@DeleteMapping(path = "{id}", produces = MediaTypes.JSON_API_VALUE)
	@PreAuthorize("#id == authentication.principal.id")
	public ResponseEntity<?> deleteUser(@PathVariable("id") UUID id) {
		this.userService.deleteUser(id);
		return ResponseEntity.ok(JsonApiModelBuilder.jsonApiModel().meta("deleted", id));
	}
}
