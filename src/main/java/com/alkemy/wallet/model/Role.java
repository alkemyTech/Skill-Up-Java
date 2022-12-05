package com.alkemy.wallet.model;

import com.alkemy.wallet.listing.RoleName;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;

/**
 *
 * @author marti
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ApiModel("Rol")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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




}
