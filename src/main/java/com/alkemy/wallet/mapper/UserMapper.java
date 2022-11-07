package com.alkemy.wallet.mapper;

import com.alkemy.wallet.config.util.DateFormatUtil;
import com.alkemy.wallet.dto.UserDTO;
import com.alkemy.wallet.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public User userDTO2Entity(UserDTO userDTO){
        User user = new User();
       // user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setSoftDelete(userDTO.isSoftDelete());
        user.setRole(userDTO.getRole());
        return user;
    }
    
    public UserDTO userEntity2DTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setCreationDate(DateFormatUtil.toCustomDate(user.getCreationDate()));
        userDTO.setUpdateDate(DateFormatUtil.toCustomDate(user.getUpdateDate()));
        userDTO.setSoftDelete(user.isSoftDelete());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public List<UserDTO> userEntityList2DTOList(List<User> userList) {
        List<UserDTO> dtos = new ArrayList<>();
        for(User user: userList){
            dtos.add(userEntity2DTO(user));
        }
        return dtos;
    }

    public UserDTO createUserEntity2DTOResponse(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreationDate(DateFormatUtil.toCustomDate(user.getCreationDate()));
        userDTO.setUpdateDate(DateFormatUtil.toCustomDate(user.getUpdateDate()));
        userDTO.setRole(user.getRole());
        return userDTO;
    }
}
