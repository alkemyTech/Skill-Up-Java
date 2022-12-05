package com.alkemy.wallet.service.interfaces;

import com.alkemy.wallet.dto.UserDto;

public interface IUserService {

    boolean checkLoggedUser(String token);

    UserDto findByEmail(String email );
}
