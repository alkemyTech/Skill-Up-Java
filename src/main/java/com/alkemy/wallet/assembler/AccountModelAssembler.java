package com.alkemy.wallet.assembler;

import com.alkemy.wallet.assembler.model.AccountModel;
import com.alkemy.wallet.controller.AccountController;
import com.alkemy.wallet.dto.AccountDto;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class AccountModelAssembler extends RepresentationModelAssemblerSupport<AccountDto, AccountModel> {

    public AccountModelAssembler() {
        super(AccountController.class, AccountModel.class);
    }

    @Override
    public AccountModel toModel(AccountDto dto) {
        AccountModel model = new AccountModel();
        BeanUtils.copyProperties(dto, model);
        return model;
    }

}
