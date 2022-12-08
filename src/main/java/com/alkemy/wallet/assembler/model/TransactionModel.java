package com.alkemy.wallet.assembler.model;

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.model.enums.TypeOfTransaction;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Setter
@Getter
public class TransactionModel extends RepresentationModel<TransactionModel> {

    private Long id;

    private Double amount;

    private TypeOfTransaction type;

    private String description;

    private AccountDto account;

}
