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
import com.alkemy.wallet.service.IAuthService;
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
    private final IAuthService authService;
    private final CustomMessageSource messageSource;

    @Override
    public TransactionResponseDto sendMoneyIndicatingCurrency(String currency,
                                                              TransactionRequestDto transactionRequestDto) {
        User loggedUser = userService.getUserByEmail(authService.getEmailFromContext());
        Account senderAccount = accountService.getAccountByCurrencyAndUserId(currency, loggedUser.getId());
        Account receiverAccount = accountService.getAccountById(transactionRequestDto.getAccountId());
        User receiverUser = receiverAccount.getUser();

        if (receiverUser.equals(loggedUser))
            throw new InputMismatchException(messageSource.message("transaction.invalid-transaction", null));
        if (transactionRequestDto.getAmount() > senderAccount.getBalance())
            throw new InputMismatchException(messageSource.message("transaction.invalid-balance", null));
        if (transactionRequestDto.getAmount() > senderAccount.getTransactionLimit())
            throw new InputMismatchException(messageSource.message("transaction.limit", null));
        if (!senderAccount.getCurrency().equals(receiverAccount.getCurrency()))
            throw new InputMismatchException(messageSource.message("transaction.currency-mismatch", null));

        Double newBalanceSender = senderAccount.getBalance() - transactionRequestDto.getAmount();
        Double newBalanceReceiver = receiverAccount.getBalance() + transactionRequestDto.getAmount();
        accountService.editBalanceAndSave(senderAccount, newBalanceSender);
        accountService.editBalanceAndSave(receiverAccount, newBalanceReceiver);

        Transaction payment = setTransactionValues(
                transactionRequestDto.getAmount(), PAYMENT,
                transactionRequestDto.getDescription(), loggedUser, receiverAccount);
        Transaction income = setTransactionValues(
                transactionRequestDto.getAmount(), INCOME,
                transactionRequestDto.getDescription(), receiverUser, receiverAccount);

        repository.save(payment);
        repository.save(income);
        return mapper.entity2Dto(payment);
    }

    @Override
    public TransactionResponseDto updateTransaction(Long id, UpdateTransactionRequestDto transactionRequestDto) {
        User loggedUser = userService.getUserByEmail(authService.getEmailFromContext());
        Transaction transaction = getTransactionById(id);
        if (!loggedUser.getTransactions().contains(transaction))
            throw new IllegalArgumentException(
                    messageSource.message("entity.out-of-bound", new String[]{"transaction"}));
        if (transactionRequestDto.getDescription() != null && !transactionRequestDto.getDescription().trim().isEmpty())
            transaction.setDescription(transactionRequestDto.getDescription());
        return mapper.entity2Dto(transaction);
    }

    @Override
    public TransactionResponseDto getTransactionDetails(Long id) {
        User loggedUser = userService.getUserByEmail(authService.getEmailFromContext());
        Transaction transaction = getTransactionById(id);
        if (!transaction.getUser().equals(loggedUser))
            throw new IllegalArgumentException(
                    messageSource.message("entity.out-of-bound", new String[]{"transaction"}));
        return mapper.entity2Dto(transaction);
    }

    @Override
    public Transaction getTransactionById(Long id) {
        Optional<Transaction> transaction = repository.findById(id);
        return transaction.orElseThrow(() ->
                new NullPointerException(
                        messageSource.message("entity.not-found",
                                new String[]{"Transaction", "id", id.toString()}))
        );
    }

    @Override
    public TransactionResponseDto doPayment(TransactionRequestDto transactionRequestDto) {
        Account receiverAccount = accountService.getAccountById(transactionRequestDto.getAccountId());
        return sendMoneyIndicatingCurrency(receiverAccount.getCurrency().name(), transactionRequestDto);
    }

    @Override
    public TransactionResponseDto doDeposit(TransactionRequestDto transactionRequestDto) {
        Account receiverAccount = accountService.getAccountById(transactionRequestDto.getAccountId());
        User user = receiverAccount.getUser();

        if (!user.getEmail().equals(authService.getEmailFromContext()))
            throw new IllegalArgumentException(messageSource.message("transaction.deposit-mismatch", null));

        double newBalance = receiverAccount.getBalance() + transactionRequestDto.getAmount();
        accountService.editBalanceAndSave(receiverAccount, newBalance);

        Transaction transaction = setTransactionValues(
                transactionRequestDto.getAmount(), DEPOSIT,
                transactionRequestDto.getDescription(), user, receiverAccount);
        return mapper.entity2Dto(repository.save(transaction));
    }

    @Override
    public Page<TransactionResponseDto> getAllTransactions(Long userId, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        return repository.findTransactionsByUserId(userId, pageable).map(mapper::entity2Dto);
    }
}