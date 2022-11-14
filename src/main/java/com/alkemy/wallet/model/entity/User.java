package com.alkemy.wallet.model.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private LocalDateTime creationDate = LocalDateTime.now();

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name = "UPDATED_AT")
    private LocalDateTime updateDate;

    @Column(name = "DELETED")
    private boolean softDelete = Boolean.FALSE;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Account> accounts;

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "REL_USER_ROLE",
            joinColumns = {@JoinColumn(name = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
    @ToString.Exclude
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<FixedTermDeposit> fixedTermDeposits;
}
