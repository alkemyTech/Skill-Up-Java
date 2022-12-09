package com.alkemy.wallet.service.interfaces;

import com.alkemy.wallet.dto.UserDto;
import com.alkemy.wallet.model.User;

public interface IUserService {

    boolean checkLoggedUser(String token);

    User findLoggedUser(String token);

    UserDto findByEmail(String email );
}
