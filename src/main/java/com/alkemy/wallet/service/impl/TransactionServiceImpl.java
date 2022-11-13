package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.enumeration.ErrorList;
import com.alkemy.wallet.enumeration.UrlList;
import com.alkemy.wallet.exception.TransactionException;
import com.alkemy.wallet.mapper.AccountMapper;
import com.alkemy.wallet.mapper.TransactionMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.security.config.JwtTokenProvider;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.impl.account.util.TransferValidation;
import com.alkemy.wallet.service.impl.transaction.util.ITransactionStrategy;
import com.alkemy.wallet.service.impl.transaction.util.IncomeStrategy;
import com.alkemy.wallet.service.impl.transaction.util.PaymentStrategy;
import com.alkemy.wallet.util.GetTokenData;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Service
public class TransactionServiceImpl implements ITransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionMapper transactionMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    public TransactionDTO getTransactionById(Integer id, Integer user_id) {

        Transaction transEntity = this.checkIdBtwRequestUserAndUserTrans(id, user_id);

        return transactionMapper.transEntity2DTO(transEntity);
    }

    @Override
    public TransactionPageDTO findAllByUserId(Integer userId, Integer page) {

        // All accounts of User
        List<Account> accounts = accountRepository.findAccountsByUserID(userId);
        // Account List ID of User
        List<Integer> accIds = new ArrayList<>();
        accounts.forEach((Account acc) -> accIds.add(acc.getId()));
        // Transactions pagination
        Pageable pageWithTenElements = PageRequest.of(page - 1, 10);
        Page<Transaction> transPage = transactionRepository.findByAccountIds(accIds, pageWithTenElements);
        List<Transaction> transList = transPage.getContent();

        // Create DTO
        TransactionPageDTO transPageDTO = new TransactionPageDTO();
        int totalPages = transPage.getTotalPages();
        transPageDTO.setTotalPages(totalPages);

        if (page > transPage.getTotalPages() || page <= 0) {
            throw new TransactionException(ErrorList.PAGE_ERROR.getMessage());
        }
        // Create URLs
        StringBuilder url = new StringBuilder(
                UrlList.MAIN_PATH.getUrlString() + "transactions/" + userId.toString() + "?page=");

        transPageDTO.setNextPage(totalPages == page ? null : url + String.valueOf(page + 1));
        transPageDTO.setPreviusPage(page == 1 ? null : url + String.valueOf(page - 1));

        transPageDTO.setTransDTOList(transactionMapper.transListEntity2ListDTO(transList));

        return transPageDTO;
    }

    /**
     * This method makeTransactions is used for strategies DEPOSIT and INCOME.
     * Check if account you try deposit/income money exist in DB.
     * Call strategy method make in "/transaction/util" package
     * Make a mapping to Entity for persist and set Type Transaction select in DTO.
     * Then persist Transaction in DB.
     * 
     * @param transDTO = Need account_id of Account Destiny, amount and description
     * @param strategy = Strategies can see on "/transactions/util" package, and how
     *                 make updates.
     */
    @Override
    public void makeTransaction(TransactionCreateDTO transDTO, ITransactionStrategy strategy) {
        Account recieverAccount = accountRepository.findById(transDTO.getAccount_id()).orElseThrow(
                () -> new TransactionException(ErrorList.OBJECT_NOT_FOUND.getMessage()));

        strategy.make(transDTO.getAmount(), recieverAccount);
        Transaction newTrans = transactionMapper.transCreateDTO2Entity(transDTO);
        newTrans.setType(strategy.type());
        transactionRepository.save(newTrans);
    }

    /**
     *
     * This method makeTransactions is used for strategy Payment and only send money
     * to other account.
     * (send money feature)
     * Take user logged username
     * Check if account you try send money exist in DB.
     * Check if account u select disccount money have the same money type of
     * reciever account.
     * Check you not send money to the same account own
     * Call strategy method make in "/transaction/util" package
     * Make a mapping to Entity for persist and set Type Transaction select in DTO.
     * Then persist Transaction in DB.
     * 
     * @param transDTO = DTO with account destiny of money, description and amount
     *                 you try send
     * @param strategy = Need a PaymentStrategy class
     * @param token    = retrieve from controller and request header
     */
    @Override
    public void makeTransaction(TransactionCreateDTO transDTO, ITransactionStrategy strategy, String token) {
        // Retrieve user on token:
        Authentication authentication = jwtTokenProvider.getAuthentication(token.substring(7));
        User senderUser = userRepository.findByEmail(authentication.getName());

        Account recieverAccount = accountRepository.findById(transDTO.getAccount_id()).orElseThrow(
                () -> new TransactionException(ErrorList.OBJECT_NOT_FOUND.getMessage()));

        List<Account> senderAccs = accountRepository.findAccountsByUserID(senderUser.getId());
        Account senderAccount = senderAccs.stream().filter(
                acc -> acc.getCurrency().equals(recieverAccount.getCurrency()))
                .findFirst()
                .orElseThrow(() -> new TransactionException(ErrorList.OBJECT_NOT_FOUND.getMessage()));

        if (recieverAccount.getId().equals(senderAccount.getId())) {
            throw new TransactionException(ErrorList.SAME_ACCOUNT.getMessage());
        }

        strategy.make(transDTO.getAmount(), senderAccount);// accOrigin
        Transaction newTrans = transactionMapper.transCreateDTO2Entity(transDTO);
        newTrans.setAccount_id(senderAccount.getId());// AccOrigin
        newTrans.setType(strategy.type());
        transactionRepository.save(newTrans);
    }

    /**
     * This method makeTransactions is used for strategy INVEST.
     * Check if account you try create FixedDeposit money exist in DB.
     * Call strategy method make in "/transaction/util" package
     * Make a mapping to Entity for persist and set Type Transaction select in DTO.
     * Make Invest.
     * Then persist Transaction in DB.
     * 
     * @param strategy = Retrieve a "INVEST" or similar strategy
     * @param account  = account own
     * @param amount   = amount you send to FixedDeposit
     */
    @Override
    public void makeTransaction(ITransactionStrategy strategy, Account account, Double amount) {
        Account userAccount = accountRepository.findById(account.getId()).orElseThrow(
                () -> new TransactionException(ErrorList.OBJECT_NOT_FOUND.getMessage()));

        TransactionCreateDTO transDTO = new TransactionCreateDTO();
        transDTO.setAmount(amount);
        transDTO.setDescription("New FixedTermDeposit");
        transDTO.setAccount_id(userAccount.getId());
        Transaction newTrans = transactionMapper.transCreateDTO2Entity(transDTO);
        newTrans.setType(strategy.type());
        strategy.make(amount, userAccount);
        transactionRepository.save(newTrans);

    }

    @Override
    public void updateTransaction(TransactionUpdateDTO transDTO, Integer id, Integer user_id) {
        Optional<Transaction> optTrans = transactionRepository.findById(id);
        if (optTrans.isEmpty()) {
            throw new TransactionException(ErrorList.OBJECT_NOT_FOUND.getMessage());
        }
        Integer dbUserId = optTrans.get().getAccount().getUser().getId();

        if (dbUserId != user_id) {
            throw new TransactionException(ErrorList.NOT_MATCHING_IDS.getMessage());
        }
        String newDescription = transDTO.getDescription();
        transactionRepository.updateDescription(id, newDescription);
    }

    private Transaction checkIdBtwRequestUserAndUserTrans(Integer id, Integer user_id) {
        Optional<Transaction> optTrans = transactionRepository.findById(id);
        if (optTrans.isEmpty()) {
            throw new TransactionException(ErrorList.OBJECT_NOT_FOUND.getMessage());
        }
        Transaction transEntity = optTrans.get();
        Integer dbUserId = transEntity.getAccount().getUser().getId();
        if (dbUserId != user_id) {
            throw new TransactionException(ErrorList.NOT_MATCHING_IDS.getMessage());
        }
        return transEntity;
    }

    @Override
    public TransactionResponseDTO sendMoney(TransactionCreateDTO transaction, String bearerToken, String currency)
            throws ParseException {
        Account accountDestiny = accountRepository.findById(transaction.getAccount_id()).orElseThrow(
                () -> new TransactionException(ErrorList.OBJECT_NOT_FOUND.getMessage()));

        if (accountDestiny.getCurrency().name() == currency) {
            this.makeTransaction(transaction, new PaymentStrategy(), bearerToken);
            this.makeTransaction(transaction, new IncomeStrategy());
        } else {
            throw new TransactionException(ErrorList.ACCOUNTS_DIFERENT_CURRENCY.getMessage());
        }
        String token = bearerToken.substring("Bearer ".length());
        // llamo método estático
        Integer user_id = GetTokenData.getUserIdFromToken(token);

        List<Transaction> trans = transactionRepository.findByUserId(user_id);
        Transaction lastTrans = trans.get(0);
        TransactionResponseDTO result = transactionMapper.transEntity2ResponseDTO(lastTrans);
        return result;
    }
}
