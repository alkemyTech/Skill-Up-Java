package com.alkemy.wallet.service;

import com.alkemy.wallet.dto.PageDto;
import com.alkemy.wallet.dto.TransactionDto;
import com.alkemy.wallet.enumeration.TypeTransaction;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ITransactionService {

  public List<TransactionDto> getByAccountAndType(Long accountId, TypeTransaction type);

  TransactionDto createTransaction(TransactionDto dto);


  TransactionDto getDetailById(Long transactionId);


  TransactionDto refreshValues(Long id, TransactionDto transactionDto);

  TransactionDto createNewDeposit(TransactionDto dto);

  PageDto<TransactionDto> findAllTransaction(Pageable page, HttpServletRequest request, Long id);
}
