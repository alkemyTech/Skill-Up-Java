package com.alkemy.wallet.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table( name = "USERS" )
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "USER_ID" )
    private Integer userId;

    @Column( nullable = false, name = "FIRST_NAME" )
    private String firstName;

    @Column( nullable = false, name = "LAST_NAME" )
    private String lastName;

    @Column( nullable = false, unique = true, name = "EMAIL" )
    private String email;

    @Column( nullable = false, name = "PASSWORD" )
    private String password;

    @OneToOne( fetch = FetchType.EAGER, cascade = CascadeType.PERSIST )
    @JoinColumn( name = "ROLE_ID", referencedColumnName = "ROLE_ID", nullable = false )
    private Role role;

    // TODO: check timestamps format and auditing
    private Timestamp creationDate;
    private Timestamp updateDate;

    // TODO: check soft delete rules
    private Boolean softDelete;

    public User(int userId){
        this.userId = userId;
    }
}
