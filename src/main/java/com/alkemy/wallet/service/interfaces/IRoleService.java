package com.alkemy.wallet.service.interfaces;

import com.alkemy.wallet.dto.RoleDto;
import com.alkemy.wallet.listing.RoleName;

public interface IRoleService {

    RoleDto findByName(RoleName roleUser);

    RoleDto createRole(RoleDto role);
}
