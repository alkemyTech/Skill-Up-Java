package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.FixedTermDepositRequestDTO;
import com.alkemy.wallet.dto.FixedTermDepositResponseDTO;
import com.alkemy.wallet.enumeration.CurrencyList;
import com.alkemy.wallet.exception.TransactionException;
import com.alkemy.wallet.mapper.FixedTermDepositMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.FixedTermDepositRepository;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.service.IFixedTermDepositService;
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
            throw new TransactionException("The minimum days is 30");
        }

        // retrieve user auth


        // Retrieve account by currency;
        Account account = accountRepository.findByCurrencyAndUserId(CurrencyList.valueOf(requestDTO.getCurrency()), 2);

        // Validate amount
        if(requestDTO.getAmount() > account.getBalance()) {
            throw new TransactionException("Balance insufficient");
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
        account.setBalance( account.getBalance() - requestDTO.getAmount() );
        fixedTermDepositRepository.save(
                fixedTermDepositMapper.dtoToEntity(responseDTO)
        );
        return responseDTO;
    }
}
