package com.alkemy.wallet.model.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET soft_delete=true WHERE user_id=?")
@Where(clause = "soft_delete=false")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 20)
    private String firstName;

    @Column(nullable = false, length = 20)
    private String lastName;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 20)
    private String password;

    private Long fkRoleId;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime creationDate;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime updateDate;

    private Boolean softDelete = Boolean.FALSE;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Account> accounts;

    @OneToOne
    @JoinColumn(name = "fkRoleId", insertable = false, updatable = false)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<FixedTermDeposit> fixedTermDeposits;
}
