package com.alkemy.wallet.assembler;

import com.alkemy.wallet.assembler.model.TransactionModel;
import com.alkemy.wallet.controller.TransactionsController;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.model.Transaction;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class TransactionModelAssembler extends RepresentationModelAssemblerSupport<TransactionDto, TransactionModel> {

    public TransactionModelAssembler() {
        super(TransactionsController.class, TransactionModel.class);
    }

    @Override
    public TransactionModel toModel(TransactionDto dto) {
        TransactionModel model = new TransactionModel();
        BeanUtils.copyProperties(dto, model);
        return model;
    }
}
