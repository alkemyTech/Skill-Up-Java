package com.alkemy.wallet.service;

import com.alkemy.wallet.model.entity.UserEntity;

import java.util.List;

public interface IUserService {
    public List<UserEntity> showAllUsers();
}
