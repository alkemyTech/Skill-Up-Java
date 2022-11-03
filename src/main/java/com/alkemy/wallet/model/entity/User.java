package com.alkemy.wallet.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@Table(name = "users")
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
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private Boolean softDelete;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Account> accounts;
    @OneToOne
    @JoinColumn(name = "fkRoleId", insertable = false, updatable = false)
    private Role role;
}
