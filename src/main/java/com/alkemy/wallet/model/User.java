package com.alkemy.wallet.model;

import java.sql.Timestamp;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;
	@Column(name = "first_name", nullable=false)
	@NotEmpty
	private String firstName; 
	@Column(name = "last_name", nullable=false)
	@NotEmpty
	private String lastName;
	@Column(name = "email", unique=true, nullable=false)
	@Email
	@NotEmpty
	private String email;
	@Column(name = "password", nullable=false)
	@NotEmpty
	private String password;
	@ManyToOne
	private String roleId; // Clave foranea hacia ID de Role
	@Column(name = "creation_date")
	private Timestamp creationDate;
	@Column(name = "update_date")
	private Timestamp updateDate;
	@Column(name = "soft_delete")
	private boolean softDelete = false;
}
