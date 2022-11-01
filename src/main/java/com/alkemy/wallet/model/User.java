package com.alkemy.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "USERS" )
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

    // TODO: check timestamps format and auditing
    private Timestamp creationDate;
    private Timestamp updateDate;

    // TODO: check soft delete rules
    private Boolean softDelete;

    public User(int userId){
        this.userId = userId;
    }
}
