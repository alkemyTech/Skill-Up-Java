package com.alkemy.wallet.model;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Entity
@Data
@Table(name="users")
public class User {
	
	@Id
	@Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;
	@Column(name = "FIRST_NAME", nullable=false)
	@NotEmpty
	private String firstName; 
	@Column(name = "LAST_NAME", nullable=false)
	@NotEmpty
	private String lastName;
	@Column(name = "EMAIL", unique=true, nullable=false)
	@Email
	@NotEmpty
	private String email;
	@Column(name = "PASSWORD", nullable=false)
	@NotEmpty
	private String password;
	@ManyToOne
	@Column(name = "ROLE_ID")
	private String roleId; // Clave foranea hacia ID de Role
	@Column(name = "CREATION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	@Column(name = "UPDATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	@Column(name = "SOFT_DELETE")
	private boolean softDelete = false;
	
	public User() {
	}
	
	@PrePersist
	public void prePersist() {
		this.creationDate = new Date();
	}
	
	@PreUpdate
	public void preUpdate() {
		this.updateDate = new Date();
	}
}
