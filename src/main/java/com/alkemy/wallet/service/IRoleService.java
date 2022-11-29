package com.alkemy.wallet.service;

import com.alkemy.wallet.model.entity.Role;

import java.util.List;

public interface IRoleService {

    void save();

    Role getById(Long id);

    List<Role> getAll();
}
