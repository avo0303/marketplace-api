package com.andrewsha.marketplace.domain.user.resource;

import java.time.LocalDate;
import com.andrewsha.marketplace.domain.user.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiType;

public class UserDTO {
	@JsonApiId
	private final String id;
	@JsonApiType
	private final String type = "user";
	private final String name;
	private final String email;
	private final String phoneNumber;
	private final String profileIcon;
	@JsonSerialize(using = LocalDateSerializer.class)
	private final LocalDate dob;

	public UserDTO(User user) {
		this.id = user.getId().toString();
		this.name = user.getName();
		this.email = user.getEmail();
		this.phoneNumber = user.getPhoneNumber();
		this.profileIcon = user.getProfileIcon();
		this.dob = user.getDob();
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getProfileIcon() {
		return profileIcon;
	}

	public LocalDate getDob() {
		return dob;
	}
}
