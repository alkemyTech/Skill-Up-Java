package com.alkemy.wallet.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table( name = "USERS" )
public class User implements UserDetails {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "USER_ID" )
    private Integer userId;

    @Column( nullable = false, name = "FIRST_NAME" )
    private String firstName;

    @Column( nullable = false, name = "LAST_NAME" )
    private String lastName;

    @Column( nullable = false, unique = true, name = "EMAIL" )
    private String email;

    @Column( nullable = false, name = "PASSWORD" )
    private String password;

    @OneToOne( fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn( name = "ROLE_ID", referencedColumnName = "ROLE_ID", nullable = false)
    private Role role;

    // TODO: check timestamps format and auditing
    private Timestamp creationDate;
    private Timestamp updateDate;

    // TODO: check soft delete rules
    private Boolean softDelete;

    public Integer getUserId() {
        return userId;
    }

    public User setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public User setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public User setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public Boolean getSoftDelete() {
        return softDelete;
    }

    public User setSoftDelete(Boolean softDelete) {
        this.softDelete = softDelete;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
