package com.alkemy.wallet.model;

import com.alkemy.wallet.enumeration.RoleList;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private RoleList name;

    @Column(name = "description", length = 80)
    private String description;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "update_date")
    private Instant updateDate;
}
