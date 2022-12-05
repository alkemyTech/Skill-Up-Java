package com.alkemy.wallet.dto;

import com.alkemy.wallet.listing.RoleName;
import lombok.Data;

import java.util.Date;

@Data
public class RoleDto {

    private Long id;

    private RoleName name;

    private String description;

    private Date creationDate;

    private Date updateDate;
}
