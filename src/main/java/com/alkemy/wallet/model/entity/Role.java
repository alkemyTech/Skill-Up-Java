package com.alkemy.wallet.model.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "ROLES")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false, name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name = "CREATED_AT")
    private LocalDateTime creationDate;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name = "UPDATED_AT")
    private LocalDateTime updateDate;
}
