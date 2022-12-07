package com.alkemy.wallet.assembler.model;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.enums.TypeOfTransaction;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
@Setter
@Getter
public class TransactionModel extends RepresentationModel<TransactionModel> {

        private Long id;

        private Double amount;

        private TypeOfTransaction type;

        private String description;

        private Long accountId;

        private Date transactionDate;
}
