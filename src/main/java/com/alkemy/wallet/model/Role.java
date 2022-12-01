package com.alkemy.wallet.model;

import com.alkemy.wallet.listing.Rol;
import com.sun.istack.NotNull;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

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
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "NAME")
    @NotNull
    private Rol name;

    @Column(name = "DESCRIPTION")
    @Nullable
    private String description;

    @Column(name = "TIMESTAMPS")
    private Timestamp timestamps;

}
