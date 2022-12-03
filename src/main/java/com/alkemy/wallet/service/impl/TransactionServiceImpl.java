package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.constant.TransactionTypeEnum;
import com.alkemy.wallet.model.dto.request.TransactionRequestDto;
import com.alkemy.wallet.model.dto.request.UpdateTransactionRequestDto;
import com.alkemy.wallet.model.dto.response.TransactionResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.Transaction;
import com.alkemy.wallet.model.entity.User;
import com.alkemy.wallet.model.mapper.TransactionMapper;
import com.alkemy.wallet.repository.ITransactionRepository;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IAuthenticationService;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Optional;

import static com.alkemy.wallet.model.constant.FinalValue.PAGE_SIZE;
import static com.alkemy.wallet.model.constant.TransactionTypeEnum.*;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionMapper mapper;
    private final ITransactionRepository repository;
    private final IAccountService accountService;
    private final IUserService userService;
    private final IAuthenticationService authService;

    @Override
    public TransactionResponseDto sendMoneyIndicatingCurrency(String currency, TransactionRequestDto request) {
        User loggedUser = userService.getByEmail(authService.getEmailFromContext());
        Account senderAccount = accountService.getByCurrencyAndUserId(currency, loggedUser.getId());
        Account receiverAccount = accountService.getById(request.getAccountId());
        User receiverUser = receiverAccount.getUser();

        if (receiverUser.equals(loggedUser))
            throw new InputMismatchException("Trying to make a PAYMENT to one of your accounts");
        if (request.getAmount() > senderAccount.getBalance())
            throw new InputMismatchException("Not enough money to send");
        if (request.getAmount() > senderAccount.getTransactionLimit())
            throw new InputMismatchException("Transaction limit reached");
        if (!senderAccount.getCurrency().equals(receiverAccount.getCurrency()))
            throw new IllegalArgumentException(String.format("Trying to send money from an %s account to an %s account",
                            senderAccount.getCurrency(), receiverAccount.getCurrency()));

        Double newBalanceSender = senderAccount.getBalance() - request.getAmount();
        Double newBalanceReceiver = receiverAccount.getBalance() + request.getAmount();
        accountService.editBalanceAndSave(senderAccount, newBalanceSender);
        accountService.editBalanceAndSave(receiverAccount, newBalanceReceiver);

        Transaction payment = setValues(
                request.getAmount(), PAYMENT, request.getDescription(), loggedUser, receiverAccount);
        Transaction income = setValues(
                request.getAmount(), INCOME, request.getDescription(), receiverUser, receiverAccount);

        repository.save(payment);
        repository.save(income);
        return mapper.entity2Dto(payment);
    }

    @Override
    public TransactionResponseDto update(Long id, UpdateTransactionRequestDto request) {
        User loggedUser = userService.getByEmail(authService.getEmailFromContext());
        Transaction transaction = getById(id);
        if (!loggedUser.getTransactions().contains(transaction))
            throw new IllegalArgumentException("The transaction does not belong to the current logged user");
        if (request.getDescription() != null && !request.getDescription().trim().isEmpty())
            transaction.setDescription(request.getDescription());
        return mapper.entity2Dto(transaction);
    }

    @Override
    public TransactionResponseDto getDetails(Long id) {
        User loggedUser = userService.getByEmail(authService.getEmailFromContext());
        Transaction transaction = getById(id);
        if (!transaction.getUser().equals(loggedUser))
            throw new IllegalArgumentException("This transaction does not belong to current user");
        return mapper.entity2Dto(transaction);
    }

    @Override
    public Transaction getById(Long id) {
        Optional<Transaction> transaction = repository.findById(id);
        return transaction.orElseThrow(() ->
                new NullPointerException(String.format("Could not found the transaction with id %s", id)));
    }

    @Override
    public TransactionResponseDto payment(TransactionRequestDto request) {
        Account receiverAccount = accountService.getById(request.getAccountId());
        return sendMoneyIndicatingCurrency(receiverAccount.getCurrency().name(), request);
    }

    @Override
    public TransactionResponseDto deposit(TransactionRequestDto request) {
        Account receiverAccount = accountService.getById(request.getAccountId());
        User user = receiverAccount.getUser();

        if (!user.getEmail().equals(authService.getEmailFromContext()))
            throw new IllegalArgumentException("The receiver user is different that the sender");

        double newBalance = receiverAccount.getBalance() + request.getAmount();
        accountService.editBalanceAndSave(receiverAccount, newBalance);

        Transaction transaction = setValues(
                request.getAmount(), DEPOSIT, request.getDescription(), user, receiverAccount);
        return mapper.entity2Dto(repository.save(transaction));
    }

    @Override
    public Page<TransactionResponseDto> paginateTransactions(Long userId, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        return repository.findTransactionsByUserId(userId, pageable).map(mapper::entity2Dto);
    }

    protected Transaction setValues(Double amount, TransactionTypeEnum transactionType,
                                    String description, User user, Account account) {
        return Transaction.builder()
                .amount(amount)
                .type(transactionType)
                .description(description)
                .transactionDate(LocalDateTime.now())
                .user(user)
                .account(account)
                .build();
    }
}