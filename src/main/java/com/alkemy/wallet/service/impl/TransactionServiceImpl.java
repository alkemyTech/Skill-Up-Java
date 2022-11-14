package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.dto.request.TransactionRequestDto;
import com.alkemy.wallet.model.dto.response.TransactionResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.TransactionMapper;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IAuthService;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;

import static com.alkemy.wallet.model.entity.TransactionTypeEnum.*;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionMapper mapper;
    private final ITransactionRepository repository;
    private final IAccountService accountService;
    private final IUserService userService;
    private final IAuthService authService;

    @Override
    public TransactionResponseDto sendMoneyIndicatingCurrency(String currency, TransactionRequestDto request, String token) {
        User loggedUser = authService.getUserFromToken(token);
        Account senderAccount = accountService.getByCurrencyAndUserId(currency, loggedUser.getId());
        Account receiverAccount = accountService.getAccountById(request.getAccountId());
        User receiverUser = userService.getEntityById(receiverAccount.getUser().getId());

        if (receiverUser.equals(loggedUser))
            throw new IllegalCallerException("Trying to make a PAYMENT to one of your accounts");
        if (request.getAmount() > senderAccount.getBalance() || request.getAmount() > senderAccount.getTransactionLimit())
            throw new InputMismatchException("Not enough money to send or transaction limit reached");
        if (!senderAccount.getCurrency().equals(receiverAccount.getCurrency()))
            throw new IllegalArgumentException(String.format("Trying to send money from an %s account to an %s account", senderAccount.getCurrency(), receiverAccount.getCurrency()));

        Double newBalanceSender = senderAccount.getBalance() - request.getAmount();
        Double newBalanceReceiver = receiverAccount.getBalance() + request.getAmount();
        accountService.editBalanceAndSave(senderAccount, newBalanceSender);
        accountService.editBalanceAndSave(receiverAccount, newBalanceReceiver);

        Transaction payment = mapper.dto2Entity(request, PAYMENT, loggedUser, receiverAccount);
        Transaction income = mapper.dto2Entity(request, INCOME, receiverUser, receiverAccount);

        repository.save(payment);
        repository.save(income);
        return mapper.entity2Dto(payment);
    }

    @Override
    public TransactionResponseDto doTransaction(TransactionRequestDto request, String token) {
        User loggedUser = authService.getUserFromToken(token);
        Account receiverAccount = accountService.getAccountById(request.getAccountId());

        double newBalance;
        Transaction transaction;

        if (loggedUser.getAccounts().contains(receiverAccount)) {
            newBalance = receiverAccount.getBalance() + request.getAmount();
            transaction = mapper.dto2Entity(request, DEPOSIT, loggedUser, receiverAccount);
        } else {
            newBalance = receiverAccount.getBalance() - request.getAmount();
            transaction = mapper.dto2Entity(request, PAYMENT, loggedUser, receiverAccount);
        }
        accountService.editBalanceAndSave(receiverAccount, newBalance);
        return mapper.entity2Dto(repository.save(transaction));
    }

    @Override
    public List<TransactionResponseDto> listTransactionsByUserId(Long userId) {
        List<Transaction> transactions = repository.findTransactionsByUserId(userId);
        if (transactions.isEmpty())
            throw new NoSuchElementException(String.format("The user with ID %s does not have transactions yet", userId));
        return mapper.entityList2DtoList(transactions);
    }
}