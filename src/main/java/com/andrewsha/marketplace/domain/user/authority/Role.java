package com.andrewsha.marketplace.domain.user.authority;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import com.andrewsha.marketplace.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GenericGenerator(name = "LongId", strategy = "org.hibernate.id.IncrementGenerator")
    @GeneratedValue(generator = "LongId")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", updatable = false, nullable = false)
    private RoleEnum name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    public Role() {}

    public Role(RoleEnum name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleEnum getName() {
        return name;
    }

    public void setName(RoleEnum name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
