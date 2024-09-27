package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.dto.validator.IValidatorDeposit;
import com.alkemy.wallet.dto.validator.IValidatorPayment;
import com.alkemy.wallet.dto.validator.IValidatorSendArsUsd;
import com.alkemy.wallet.exception.BankException;
import com.alkemy.wallet.model.TypeEnum;
import com.alkemy.wallet.model.AccountEntity;
import com.alkemy.wallet.model.TransactionEntity;
import com.alkemy.wallet.model.UserEntity;
import com.alkemy.wallet.repository.BankDAO;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.utils.DTOValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.alkemy.wallet.model.TransactionLimitEnum.ARS;
import static com.alkemy.wallet.model.TransactionLimitEnum.USD;
import static com.alkemy.wallet.model.TypeEnum.*;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements ITransactionService {
    private final BankDAO bankDAO;
    @Override
    public ResponseEntity<Object> saveDeposit(@RequestBody TransactionDTO transaction) {
        DTOValidator.validate(transaction, IValidatorDeposit.class);
        isTransactionAllowed(transaction, DEPOSIT, "Incorrect operation, only can be a deposit");
        if (!transaction.getCurrency().equalsIgnoreCase(ARS.getCurrency()) && !transaction.getCurrency().equalsIgnoreCase(USD.getCurrency())) {
            throw new BankException("Currency not permitted");
        }
        if (transaction.getAmount() <= 0) {
            throw new BankException("Invalid amount");
        }
        UserEntity user = bankDAO.findUserByEmail(bankDAO.returnUserName());
        AccountEntity account = bankDAO.getAccount(user.getUserId(), transaction.getCurrency().toUpperCase());
        account.setBalance(account.getBalance() + transaction.getAmount());
        return saveTransaction(transaction, DEPOSIT, account);
    }

    @Override
    public ResponseEntity<Object> savePayment(TransactionDTO transaction) {
        DTOValidator.validate(transaction, IValidatorPayment.class);
        isTransactionAllowed(transaction, PAYMENT, "Incorrect operation, only can be a payment");
        UserEntity user = bankDAO.findUserByEmail(bankDAO.returnUserName());
        AccountEntity account = bankDAO.getAccount(user.getUserId(), transaction.getCurrency().toUpperCase());
        if (!transaction.getCurrency().equalsIgnoreCase(ARS.getCurrency()) && !transaction.getCurrency().equalsIgnoreCase(USD.getCurrency())) {
            throw new BankException("Currency not permitted");
        }
        if (transaction.getAmount() <= 0) {
            throw new BankException("Invalid amount");
        }
        if (account.getBalance() <= 0 || !(account.getBalance() >= transaction.getAmount())) {
            throw new BankException("Insufficient funds");
        }
        account.setBalance(account.getBalance() - transaction.getAmount());
        return saveTransaction(transaction, PAYMENT, account);
    }

    @Override
    public ResponseEntity<Object> sendArs(TransactionDTO transaction) {
        DTOValidator.validate(transaction, IValidatorSendArsUsd.class);
        if (transaction.getAmount() <= 0) {
            throw new BankException("Invalid amount");
        }
        UserEntity user = bankDAO.findUserByEmail(bankDAO.returnUserName());
        Optional<AccountEntity> destinationAccount = bankDAO.getAccountById(transaction.getDestinationAccountId());
        if (destinationAccount.isPresent()) {
            if (destinationAccount.get().getCurrency().equals(ARS.getCurrency())) {
                AccountEntity originAccount = bankDAO.getAccount(user.getUserId(), ARS.getCurrency());
                if (originAccount.getAccountId() != destinationAccount.get().getAccountId()) {
                    if (originAccount.getBalance() >= transaction.getAmount()) {
                        destinationAccount.get().setBalance(destinationAccount.get().getBalance() + transaction.getAmount());
                        originAccount.setBalance(originAccount.getBalance() - transaction.getAmount());
                        saveTransaction(transaction, PAYMENT, originAccount);
                        return saveTransaction(transaction, INCOME, destinationAccount.get());
                    } else {
                        throw new BankException("Insufficient funds ");
                    }
                } else {
                    throw new BankException("the origin account cannot be the same as the destination account");
                }
            } else {
                throw new BankException("The destination account currency is not valid");
            }
        } else {
            throw new BankException("The destination account does not exist");
        }

    }

    @Override
    public ResponseEntity<Object> sendUsd(TransactionDTO transaction) {
        DTOValidator.validate(transaction, IValidatorSendArsUsd.class);
        if (transaction.getAmount() <= 0) {
            throw new BankException("Invalid amount");
        }
        UserEntity user = bankDAO.findUserByEmail(bankDAO.returnUserName());
        Optional<AccountEntity> destinationAccount = bankDAO.getAccountById(transaction.getDestinationAccountId());
        if (destinationAccount.isPresent()) {
            if (destinationAccount.get().getCurrency().equals(USD.getCurrency())) {
                AccountEntity originAccount = bankDAO.getAccount(user.getUserId(), USD.getCurrency());
                if (originAccount.getAccountId() != destinationAccount.get().getAccountId()) {
                    if (originAccount.getBalance() >= transaction.getAmount()) {
                        destinationAccount.get().setBalance(destinationAccount.get().getBalance() + transaction.getAmount());
                        originAccount.setBalance(originAccount.getBalance() - transaction.getAmount());
                        saveTransaction(transaction, PAYMENT, originAccount);
                        return saveTransaction(transaction, INCOME, destinationAccount.orElseThrow(() -> new BankException("Destination account does not exist")));
                    } else {
                        throw new BankException("Insufficient funds ");
                    }
                } else {
                    throw new BankException("the origin account cannot be the same as the destination account");
                }
            } else {
                throw new BankException("The destination account currency is not valid");
            }
        } else {
            throw new BankException("The destination account does not exist");
        }
    }

    @Override
    public ResponseEntity<Object> updateTransaction(Long id, TransactionDTO transactionDTO) {
        bankDAO.updateTransaction(id, transactionDTO);
        return new ResponseEntity<>("Update transaction", HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<TransactionEntity>> showAllTransactionsByUserId(Long userId) {
        Optional<UserEntity> opUser = bankDAO.getUserById(userId);
        if (opUser.isEmpty()) {
            throw new BankException("The requested user Id does not exist");
        }
        List<AccountEntity> accounts = bankDAO.getAllAccountByUser(opUser.get());
        List<TransactionEntity> transactions = new ArrayList<>();
        for (AccountEntity account : accounts) {
            transactions.addAll(bankDAO.getAllTransactionByAccount(account));
        }

        return ResponseEntity.ok(transactions);
    }

    private ResponseEntity<Object> saveTransaction(TransactionDTO transaction, TypeEnum type, AccountEntity accountEntity) {
        bankDAO.createTransaction(transaction, type, accountEntity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private static void isTransactionAllowed(TransactionDTO transaction, TypeEnum deposit, String message) {
        if (!transaction.getCurrency().equalsIgnoreCase(ARS.getCurrency()) && !transaction.getCurrency().equalsIgnoreCase(USD.getCurrency())) {
            throw new BankException("Currency not permitted");
        }
        if (!transaction.getType().equalsIgnoreCase(deposit.getType())) {
            throw new BankException(message);
        }
    }

    @Override
    public ResponseEntity<Page<TransactionEntity>> showTransactionPage(PageRequest pageRequest) {
        Page<TransactionEntity> pageTransactions = bankDAO.showTransactionPage(pageRequest);

        if (pageTransactions.isEmpty()){
            throw new BankException("Missing page number");
        }

        return ResponseEntity.ok(pageTransactions);
    }

    @Override
    public void addNavigationAttributesToModel(int pageNumber, Model model, PageRequest pageRequest) {
        Page<TransactionEntity> pageTransactions = bankDAO.showTransactionPage(pageRequest);

        int totalPages = pageTransactions.getTotalPages();
        if(totalPages > 0){
            List<Integer> pages = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
            model.addAttribute("current", pageNumber+1);
            model.addAttribute("next", pageNumber+2);
            model.addAttribute("prev", pageNumber);
            model.addAttribute("last", totalPages);
        }

        model.addAttribute("List", pageTransactions.getContent());
    }
}