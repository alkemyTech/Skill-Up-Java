package com.alkemy.wallet.service;


import com.alkemy.wallet.dto.*;

import com.alkemy.wallet.dto.TransactionDepositDto;
import com.alkemy.wallet.dto.TransactionDepositRequestDto;
import com.alkemy.wallet.dto.TransactionDetailDto;
import com.alkemy.wallet.dto.TransactionPatchDto;
import com.alkemy.wallet.model.User;

import java.awt.print.Pageable;
import java.util.List;

public interface TransactionService {
    TransactionDetailDto getTransactionDetailById (Integer Id, String token) throws Exception;

    TransactionDepositDto createDeposit(TransactionDepositRequestDto transactionDepositRequestDto, String token);

    TransactionDetailDto updateTransaction(TransactionPatchDto transaction, Integer Id, String userToken) throws Exception;

    List<TransactionDetailDto> getTransactions(Integer userId);
    List<TransactionDetailDto> getTransactionsByAccount(Integer accountId);

    TransactionPaymentDto createPayment(TransactionPaymentRequestDto transactionPaymentRequestDto, String token);
    User getUserByTransactionId(Integer id);

    TransactionDetailDto sendArs(String token, TransactionTransferRequestDto transactionTransferRequestDto);
    TransactionDetailDto sendUsd(String token, TransactionTransferRequestDto transactionTransferRequestDto);

    TransactionPaginatedDto paginateTransactionsByUser(Integer page, Integer userId, String token);

}
