package com.alkemy.wallet.service;

import com.alkemy.wallet.exception.UserNotLoggedException;
import com.alkemy.wallet.service.interfaces.IUserService;
import com.alkemy.wallet.util.JwtUtil;

public class UserService implements IUserService {
    private JwtUtil jwtUtil;

    @Override
    public boolean checkLoggedUser(String token) {
        if (jwtUtil.getKey(token) != null)
            return true;
        else throw new UserNotLoggedException("El usuario no está loggeado");
    }

}
