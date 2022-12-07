package com.alkemy.wallet.assembler;

import com.alkemy.wallet.assembler.model.TransactionModel;
import com.alkemy.wallet.controller.TransactionsController;
import com.alkemy.wallet.model.Transaction;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class TransactionModelAssembler extends RepresentationModelAssemblerSupport<Transaction, TransactionModel> {

    public TransactionModelAssembler() {
        super(TransactionsController.class, TransactionModel.class);
    }

    @Override
    public TransactionModel toModel(Transaction entity) {
        TransactionModel model = new TransactionModel();
        // Both CustomerModel and Customer have the same property names. So copy the values from the Entity to the Model
        BeanUtils.copyProperties(entity, model);
        return model;
    }
}
