package com.alkemy.wallet.assembler;

import com.alkemy.wallet.assembler.model.UserModel;
import com.alkemy.wallet.controller.UserController;
import com.alkemy.wallet.dto.ResponseUserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<ResponseUserDto, UserModel> {

    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }

    @Override
    public UserModel toModel(ResponseUserDto dto) {
        UserModel model = new UserModel();
        BeanUtils.copyProperties(dto, model);
        return model;
    }
}
