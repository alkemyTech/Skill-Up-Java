package com.alkemy.wallet.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="users")
@ApiModel("Usuario")
public class User {
	
	@Id
	@Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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
	@JoinColumn(name = "ROLE_ID")
	private Role role; // Clave foranea hacia ID de Role

	@Column(name = "CREATION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Column(name = "UPDATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	@Column(name = "SOFT_DELETE")
	private boolean softDelete = false;

//	@PrePersist
//	public void prePersist() {
//		this.creationDate = new Date();
//	}
//
//	@PreUpdate
//	public void preUpdate() {
//		this.updateDate = new Date();
//	}
}
