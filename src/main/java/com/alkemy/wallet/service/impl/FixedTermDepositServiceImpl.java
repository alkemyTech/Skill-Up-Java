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
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.FixedTermDepositRepository;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.security.config.JwtTokenProvider;
import com.alkemy.wallet.service.IFixedTermDepositService;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.impl.transaction.util.ITransactionStrategy;
import com.alkemy.wallet.service.impl.transaction.util.InvestStrategy;
import com.alkemy.wallet.service.impl.transaction.util.PaymentStrategy;
import com.alkemy.wallet.util.CalculateInterest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class FixedTermDepositServiceImpl implements IFixedTermDepositService {

    @Autowired
    FixedTermDepositRepository fixedTermDepositRepository;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ITransactionService transactionService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    CalculateInterest calculateInterest;

    @Autowired
    FixedTermDepositMapper fixedTermDepositMapper;

    @Override
    public FixedTermDepositResponseDTO createFXD(String token, FixedTermDepositRequestDTO requestDTO) {
        // Validate minimum days
        if(requestDTO.getDays() < 30) {
            throw new FixedTermException(ErrorList.MINIMUN_DAYS_FXD.getMessage());
        }

        // retrieve user auth
        Authentication authentication = jwtTokenProvider.getAuthentication(token.substring(7));
        User user = userRepository.findByEmail(authentication.getName());

        // Retrieve account by currency and user id
        Account account = accountRepository.findByCurrencyAndUserId(CurrencyList.valueOf(requestDTO.getCurrency()), user.getId());

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

    public Map<String, Object> simulate(FixedTermDepositRequestDTO requestDTO) {
        // Validate minimum days
        if(requestDTO.getDays() < 30) {
            throw new FixedTermException(ErrorList.MINIMUN_DAYS_FXD.getMessage());
        }
        Map<String, Object> response = new LinkedHashMap<>();
        DecimalFormat df = new DecimalFormat("#.00");
        Double interestTotal = calculateInterest.getInterestPlusAmount(requestDTO.getAmount(), requestDTO.getDays());
        Double interestWin = calculateInterest.getInterest(requestDTO.getAmount(), requestDTO.getDays());
        // Open Date and Closing Date
        Instant dateFXD = Instant.now();
        Instant closingDateFXD = dateFXD.plusSeconds(86400L * requestDTO.getDays());
        response.put("amount", requestDTO.getAmount());
        response.put("totalInterest", Double.parseDouble(df.format(interestTotal).replaceAll(",",".")));
        response.put("interestGained", Double.parseDouble(df.format(interestWin).replaceAll(",",".")));
        response.put("currency", requestDTO.getCurrency());
        response.put("creationDate", dateFXD);
        response.put("closingDate", closingDateFXD);
        return response;
    }

}
