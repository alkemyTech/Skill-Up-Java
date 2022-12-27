package com.alkemy.wallet.service;

import com.alkemy.wallet.model.constant.RoleEnum;
import com.alkemy.wallet.model.entity.Role;
import com.alkemy.wallet.model.entity.User;

import java.util.List;

public interface IRoleService {

    Role saveNewRole(String roleName, User user);
}
