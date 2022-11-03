package com.alkemy.wallet.model.entity;

import com.alkemy.wallet.model.RoleEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private RoleEnum name;
    private String description;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
}
