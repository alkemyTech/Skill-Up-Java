package com.alkemy.wallet.service.impl;

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
import com.alkemy.wallet.utils.CustomMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.InputMismatchException;
import java.util.Optional;

import static com.alkemy.wallet.model.constant.TransactionTypeEnum.*;
import static com.alkemy.wallet.utils.PageUtil.PAGE_SIZE;
import static com.alkemy.wallet.utils.TransactionUtil.setTransactionValues;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionMapper mapper;
    private final ITransactionRepository repository;
    private final IAccountService accountService;
    private final IUserService userService;
    private final IAuthenticationService authService;
    private final CustomMessageSource messageSource;

    @Override
    public TransactionResponseDto sendMoneyIndicatingCurrency(String currency, TransactionRequestDto request) {
        User loggedUser = userService.getByEmail(authService.getEmailFromContext());
        Account senderAccount = accountService.getByCurrencyAndUserId(currency, loggedUser.getId());
        Account receiverAccount = accountService.getById(request.getAccountId());
        User receiverUser = receiverAccount.getUser();

        if (receiverUser.equals(loggedUser))
            throw new InputMismatchException(messageSource.message("transaction.payment-error", null));
        if (request.getAmount() > senderAccount.getBalance())
            throw new InputMismatchException(messageSource.message("transaction.no-balance", null));
        if (request.getAmount() > senderAccount.getTransactionLimit())
            throw new InputMismatchException(messageSource.message("transaction.limit", null));
        if (!senderAccount.getCurrency().equals(receiverAccount.getCurrency()))
            throw new InputMismatchException(messageSource.message("transaction.input-mismatch", null));

        Double newBalanceSender = senderAccount.getBalance() - request.getAmount();
        Double newBalanceReceiver = receiverAccount.getBalance() + request.getAmount();
        accountService.editBalanceAndSave(senderAccount, newBalanceSender);
        accountService.editBalanceAndSave(receiverAccount, newBalanceReceiver);

        Transaction payment = setTransactionValues(
                request.getAmount(), PAYMENT, request.getDescription(), loggedUser, receiverAccount);
        Transaction income = setTransactionValues(
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
            throw new IllegalArgumentException(messageSource.message("transaction.out-of-bound", null));
        if (request.getDescription() != null && !request.getDescription().trim().isEmpty())
            transaction.setDescription(request.getDescription());
        return mapper.entity2Dto(transaction);
    }

    @Override
    public TransactionResponseDto getDetails(Long id) {
        User loggedUser = userService.getByEmail(authService.getEmailFromContext());
        Transaction transaction = getById(id);
        if (!transaction.getUser().equals(loggedUser))
            throw new IllegalArgumentException(messageSource.message("transaction.out-of-bound", null));
        return mapper.entity2Dto(transaction);
    }

    @Override
    public Transaction getById(Long id) {
        Optional<Transaction> transaction = repository.findById(id);
        return transaction.orElseThrow(() ->
                new NullPointerException(messageSource.message("transaction.not-found", new Long[] {id}))
        );
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
            throw new IllegalArgumentException(messageSource.message("transaction.deposit-error", null));

        double newBalance = receiverAccount.getBalance() + request.getAmount();
        accountService.editBalanceAndSave(receiverAccount, newBalance);

        Transaction transaction = setTransactionValues(
                request.getAmount(), DEPOSIT, request.getDescription(), user, receiverAccount);
        return mapper.entity2Dto(repository.save(transaction));
    }

    @Override
    public Page<TransactionResponseDto> paginateTransactions(Long userId, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        return repository.findTransactionsByUserId(userId, pageable).map(mapper::entity2Dto);
    }
}