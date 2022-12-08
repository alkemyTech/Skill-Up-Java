package com.alkemy.wallet.assembler.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Setter
@Getter
public class UserModel extends RepresentationModel<TransactionModel> {

    private String firstName;

    private String lastName;

    private String email;
}
