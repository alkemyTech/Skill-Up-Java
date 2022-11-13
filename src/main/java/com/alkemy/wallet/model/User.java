package com.alkemy.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

import static java.lang.Boolean.FALSE;

@Entity
@Table( name = "USERS" )
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete( sql = "UPDATE users SET soft_delete = true WHERE USER_ID=?" )
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

    @OneToOne( fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn( name = "ROLE_ID", referencedColumnName = "ROLE_ID", nullable = false )
    private Role role;

    // TODO: check timestamps format and auditing
    private Timestamp creationDate;
    private Timestamp updateDate;

    // TODO: check soft delete rules
    @Column( name = "SOFT_DELETE" )
    private Boolean softDelete = FALSE;

    public User( int userId ) {
        this.userId = userId;
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
