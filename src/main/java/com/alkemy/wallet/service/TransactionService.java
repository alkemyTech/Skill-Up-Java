package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.TransactionDepositDto;
import com.alkemy.wallet.dto.TransactionDepositRequestDto;
import com.alkemy.wallet.dto.TransactionDetailDto;
import com.alkemy.wallet.dto.TransactionPatchDto;
import com.alkemy.wallet.model.User;
import java.util.List;

public interface TransactionService {
    List<TransactionDetailDto> getTransactionsDetailByAccountId (Integer accountId);
    TransactionDetailDto getTransactionDetailById (Integer Id, String token) throws Exception;

    TransactionDepositDto createDeposit(TransactionDepositRequestDto transactionDepositRequestDto);

    TransactionDetailDto updateTransaction(TransactionPatchDto transaction, Integer Id) throws Exception;

    List<TransactionDetailDto> getTransactions(Integer userId);
    List<TransactionDetailDto> getTransactionsByAccount(Integer accountId);

    User getUserByTransactionId(Integer id);


}
