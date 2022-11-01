package com.alkemy.wallet.model;

import javax.persistence.*;
import java.sql.Timestamp;

// TODO: Check db table name
@Entity
@Table
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    // TODO: Check enum string content
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private RoleName name;

    @Column(name = "description")
    private String description;

    // TODO: Check timestamps format and auditing
    private Timestamp creationDate;
    private Timestamp updateDate;
}
