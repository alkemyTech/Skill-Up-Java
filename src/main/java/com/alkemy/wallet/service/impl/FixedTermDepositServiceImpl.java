package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.FixedTermDepositRequestDTO;
import com.alkemy.wallet.dto.FixedTermDepositResponseDTO;
import com.alkemy.wallet.dto.TransactionCreateDTO;
import com.alkemy.wallet.enumeration.CurrencyList;
import com.alkemy.wallet.enumeration.ErrorList;
import com.alkemy.wallet.exception.FixedTermException;
import com.alkemy.wallet.exception.TransactionException;
import com.alkemy.wallet.mapper.FixedTermDepositMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.FixedTermDepositRepository;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.service.IFixedTermDepositService;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.impl.transaction.util.ITransactionStrategy;
import com.alkemy.wallet.service.impl.transaction.util.InvestStrategy;
import com.alkemy.wallet.service.impl.transaction.util.PaymentStrategy;
import com.alkemy.wallet.util.CalculateInterest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class FixedTermDepositServiceImpl implements IFixedTermDepositService {

    @Autowired
    FixedTermDepositRepository fixedTermDepositRepository;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ITransactionService transactionService;

//    @Autowired
//    UserRepository userRepository;

    @Autowired
    CalculateInterest calculateInterest;

    @Autowired
    FixedTermDepositMapper fixedTermDepositMapper;

    @Override
    public FixedTermDepositResponseDTO createFXD(FixedTermDepositRequestDTO requestDTO) {
        // Validate minimum days
        if(requestDTO.getDays() < 30) {
            throw new FixedTermException(ErrorList.MINIMUN_DAYS_FXD.getMessage());
        }

        // retrieve user auth


        // Retrieve account by currency;
        Account account = accountRepository.findByCurrencyAndUserId(CurrencyList.valueOf(requestDTO.getCurrency()), 1);

        // Validate amount
        if(requestDTO.getAmount() > account.getBalance()) {
            throw new FixedTermException(ErrorList.INSUFFICIENT_BALANCE.getMessage());
        }

        FixedTermDepositResponseDTO responseDTO = new FixedTermDepositResponseDTO();

        // Open Date and Closing Date
        Instant dateFXD = Instant.now();
        Instant closingDateFXD = dateFXD.plusSeconds(86400L * requestDTO.getDays());

        // Calculate Interest
        Double interest = calculateInterest.getInterestPlusAmount(requestDTO.getAmount(), requestDTO.getDays());

        // Response DTO
        responseDTO.setAmount(requestDTO.getAmount());
        responseDTO.setInterest(interest);
        responseDTO.setCreationDate(dateFXD);
        responseDTO.setClosingDate(closingDateFXD);
        responseDTO.setAccount(account);
        //Create Transaction
        transactionService.makeTransaction(new InvestStrategy(),account, requestDTO.getAmount());

        fixedTermDepositRepository.save(
                fixedTermDepositMapper.dtoToEntity(responseDTO)
        );
        return responseDTO;
    }
}
