package com.alkemy.wallet.model.mapper.response.transaction;

import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.model.dto.response.transaction.TransactionResponseDto;
import com.alkemy.wallet.model.mapper.response.account.IAccountResponseMapper;
import com.alkemy.wallet.model.mapper.response.user.IUserResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IUserResponseMapper.class, IAccountResponseMapper.class})
public interface ITransactionResponseMapper {
    @Mapping(target = "userResponseDto", source = "user")
    @Mapping(target = "userId", source = "fkUserId")
    @Mapping(target = "accountResponseDto", source = "account")
    @Mapping(target = "accountId", source = "fkAccountId")
    TransactionResponseDto transactionToTransactionResponse(Transaction transaction);

    List<TransactionResponseDto> listTransactionToListTransactionRespose(List<Transaction> transactions);
}
