package com.andrewsha.marketplace.domain.user.authority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.andrewsha.marketplace.domain.user.User;
import com.andrewsha.marketplace.security.authority.CustomGrantedAuthority;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "permission_sets")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AuthoritySet {
	@Id
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@GeneratedValue(generator = "UUID")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "scope", nullable = false, updatable = false)
	private String scope;

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_permissions", joinColumns = @JoinColumn(name = "permission_set_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> users = new HashSet<>();

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "permission_sets_permissions",
			joinColumns = @JoinColumn(name = "permission_set_id"),
			inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private Set<Authority> authoritySet = new HashSet<>();

	@JsonIgnore
	public Collection<CustomGrantedAuthority> getAuthorities() {
		Collection<CustomGrantedAuthority> authorities = new ArrayList<>();
		for (Authority auth : this.authoritySet) {
			authorities.add(new CustomGrantedAuthority(auth, this.scope));
		}
		return authorities;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public void addUser(User user) {
		this.users.add(user);
	}

	public Set<Authority> getAuthoritySet() {
		return authoritySet;
	}

	public void addAuthorities(Set<Authority> set) {
		this.authoritySet.addAll(set);
	}

	public void setAuthoritySet(Set<Authority> authoritySet) {
		this.authoritySet = authoritySet;
	}
}
