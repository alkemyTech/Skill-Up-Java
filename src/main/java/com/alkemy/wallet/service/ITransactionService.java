package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.enumeration.TypeTransaction;
import java.util.List;

public interface ITransactionService {

  List<TransactionDto> transactionsById(Long userId);

  public List<TransactionDto> getByAccountAndType(Long accountId, TypeTransaction type);

  TransactionDto createTransaction(TransactionDto dto);


  TransactionDto getDetailById(Long transactionId);


  TransactionDto refreshValues(Long id, TransactionDto transactionDto);

  TransactionDto createNewDeposit(TransactionDto dto);

}
