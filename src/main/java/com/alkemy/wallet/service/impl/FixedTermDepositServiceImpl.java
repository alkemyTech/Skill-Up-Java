package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.FixedTermDepositRequestDTO;
import com.alkemy.wallet.dto.FixedTermDepositResponseDTO;
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
import java.util.List;

@Service
public class FixedTermDepositServiceImpl implements IFixedTermDepositService {

    @Autowired
    FixedTermDepositRepository fixedTermDepositRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

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

        // retrieve accounts by user and check if the user have a account with that currency
        List<Account> accountsByUser = accountRepository.findByUser(userRepository.findById(1).get());
        boolean userHaveAccountWithThatCurrency = accountsByUser.stream()
                .anyMatch( account -> requestDTO.getCurrency().equals(account.getCurrency()) );

        FixedTermDepositResponseDTO responseDTO = new FixedTermDepositResponseDTO();

        // Open Date and Closing Date
        Instant dateFXD = Instant.now();
        Instant closingDateFXD = dateFXD.plusSeconds(86400L * requestDTO.getDays());

        // Calculate Interest
        Double interest = calculateInterest.getInterestPlusAmount(requestDTO.getAmount(), requestDTO.getDays());

        // Debit money from the account
        if(userHaveAccountWithThatCurrency) {
            Account account = accountRepository.findByCurrency(requestDTO.getCurrency());
            // Validate amount
            if(requestDTO.getAmount() > account.getBalance()) {
                throw new TransactionException("Balance insufficient");
            }
            // Response DTO
            responseDTO.setAmount(requestDTO.getAmount());
            responseDTO.setInterest(interest);
            responseDTO.setCreationDate(dateFXD);
            responseDTO.setClosingDate(closingDateFXD);
            responseDTO.setAccount(account);
            account.setBalance( account.getBalance() - requestDTO.getAmount() );
        }
        fixedTermDepositRepository.save(
                fixedTermDepositMapper.dtoToEntity(responseDTO)
        );
        return responseDTO;
    }
}
