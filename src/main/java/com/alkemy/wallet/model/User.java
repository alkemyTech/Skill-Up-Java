package com.alkemy.wallet.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table( name = "USER" )
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Integer userId;

    @Column( nullable = false )
    private String firstName;

    @Column( nullable = false )
    private String lastName;

    @Column( nullable = false, unique = true )
    private String email;

    @Column( nullable = false )
    private String password;

    // TODO: check timestamps format and auditing
    private Timestamp creationDate;
    private Timestamp updateDate;

    // TODO: check soft delete rules
    private Boolean softDelete;
}
