package com.alkemy.wallet.model.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.lang.Boolean.TRUE;
import static java.time.LocalDateTime.now;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "USERS")
@SQLDelete(sql = "UPDATE users SET ENABLED=false WHERE id=?")
@Where(clause = "ENABLED=true")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false, name = "FIRST_NAME")
    private String firstName;

    @Column(nullable = false, name = "LAST_NAME")
    private String lastName;

    @Column(nullable = false, unique = true, name = "EMAIL")
    private String email;

    @Column(nullable = false, name = "PASSWORD")
    private String password;

    @OneToMany(mappedBy = "user", fetch = EAGER, cascade = ALL)
    Set<Role> authorities;

    @Column(name = "ENABLED")
    private boolean enabled = TRUE;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name = "CREATED_AT")
    private LocalDateTime creationDate = now();

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name = "UPDATED_AT")
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "user", fetch = LAZY, cascade = ALL)
    private List<Account> accounts;

    @OneToMany(mappedBy = "user", fetch = LAZY, cascade = ALL)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "user", fetch = LAZY, cascade = ALL)
    private List<FixedTermDeposit> fixedTermDeposits;

    @Override
    public Set<Role> getAuthorities() {
        return authorities;
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
        return enabled;
    }
}
