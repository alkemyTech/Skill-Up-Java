package com.alkemy.wallet.model.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.lang.Boolean.FALSE;
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
@ToString
@Builder
@Table(name = "USERS")
@SQLDelete(sql = "UPDATE users SET DELETED=true WHERE id=?")
@Where(clause = "DELETED=false")
public class User {

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

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name = "CREATED_AT")
    private LocalDateTime creationDate = now();

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name = "UPDATED_AT")
    private LocalDateTime updateDate;

    @Column(name = "DELETED")
    private boolean deleted = FALSE;

    @OneToMany(mappedBy = "user", fetch = LAZY, cascade = ALL)
    @ToString.Exclude
    private List<Account> accounts;

    @ManyToMany(fetch = EAGER, cascade = ALL)
    @JoinTable(
            name = "REL_USER_ROLE",
            joinColumns = {@JoinColumn(name = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
    @ToString.Exclude
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", fetch = LAZY, cascade = ALL)
    @ToString.Exclude
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "user", fetch = LAZY, cascade = ALL)
    @ToString.Exclude
    private List<FixedTermDeposit> fixedTermDeposits;
}
