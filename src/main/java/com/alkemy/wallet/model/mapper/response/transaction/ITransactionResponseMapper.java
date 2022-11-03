package com.alkemy.wallet.model.mapper.response.transaction;

import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.model.mapper.response.complemento.IAccountResponse2Mapper;
import com.alkemy.wallet.model.mapper.response.complemento.IUserResponse2Mapper;
import com.alkemy.wallet.model.response.TransactionResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {IUserResponse2Mapper.class, IAccountResponse2Mapper.class})
public interface ITransactionResponseMapper {
    @Mapping(target = "userResponseDto", source = "user")
    @Mapping(target = "userId", source = "fkUserId")
    @Mapping(target = "accountResponseDto", source = "account")
    @Mapping(target = "accountId", source = "fkAccountId")
    TransactionResponseDto transactionToTransactionResponse(Transaction transaction);
}
