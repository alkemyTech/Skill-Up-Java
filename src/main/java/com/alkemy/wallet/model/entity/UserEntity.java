package com.alkemy.wallet.model.entity;


import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE user_id = ?")
@Where(clause = "deleted = false")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @Column(name = "firstName")
    @NotNull
    private String firstName;

    @Column(name = "lastName")
    @NotNull
    private String lastName;

    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "password")
    @NotNull
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="role_id")
    private RoleEntity role;

    @Column(name = "creationDate")
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Column(name = "updateDate")
    @UpdateTimestamp
    private LocalDateTime updateDate;

    private boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<AccountEntity> account;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<FixedTermDepositEntity> fixedTermDeposit;
}
