package com.alkemy.wallet.assembler.model;

import com.alkemy.wallet.dto.BasicAccountDto;
import com.alkemy.wallet.model.enums.Currency;
import com.alkemy.wallet.model.enums.TypeOfTransaction;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Setter
@Getter
public class AccountModel extends RepresentationModel<AccountModel>  {

    private Long id;

    private Currency currency;

    private Double transactionLimit;

    private Double balance;

    private boolean softDelete;

    private Long user_id;


}
