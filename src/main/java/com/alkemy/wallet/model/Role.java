package com.alkemy.wallet.model;

import com.alkemy.wallet.listing.RoleName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 *
 * @author marti
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private Long id;

    @NotNull
    @Column(name = "NAME")
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @Column(name = "DESCRIPTION")
    @Nullable
    private String description;

    @Column(name = "CREATION_DATE")
    @CreationTimestamp
    private Date creationDate;

    @Column(name = "UPDATE_DATE")
    @UpdateTimestamp
    private Date updateDate;


    @OneToMany(mappedBy = "role", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<User> users;


}
