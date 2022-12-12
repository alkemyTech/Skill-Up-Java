package com.alkemy.wallet.service.interfaces;

import com.alkemy.wallet.dto.RoleDto;
import com.alkemy.wallet.listing.RoleName;
import com.alkemy.wallet.model.Role;

public interface IRoleService {

    RoleDto findByName(RoleName roleUser);

    Role createRole(Role role);
}
