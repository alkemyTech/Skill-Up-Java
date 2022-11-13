package com.alkemy.wallet.service.implementation;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.exception.*;
import com.alkemy.wallet.mapper.AccountMapper;
import com.alkemy.wallet.mapper.TransactionMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.security.JWTUtil;
import com.alkemy.wallet.service.AccountService;
import com.alkemy.wallet.service.TransactionService;
import com.alkemy.wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public TransactionDetailDto getTransactionDetailById(Integer transactionId, String userToken ) throws ResourceNotFoundException {
        var transaction = transactionRepository.findById(transactionId);
        if(transaction.isPresent()){
            User user = getUserByTransactionId(transactionId);
            userService.matchUserToToken(user.getUserId(),userToken);
            return transactionMapper.convertToTransactionDetailDto(transaction.get());
        }else{
            throw new ResourceNotFoundException("Transaction does not exist");
        }
    }

    @Override
    public TransactionDepositDto createDeposit(TransactionDepositRequestDto transactionDepositRequestDto, String token) {
        AccountDto accountDto = accountService.getAccountById(transactionDepositRequestDto.getAccountId());
        String username = jwtUtil.extractClaimUsername(token.substring(7));
        User user = userService.loadUserByUsername(username);

        if(!accountService.hasUserAccountById(user.getUserId(), accountDto.id())){
            throw new ForbiddenAccessException("The user with id " + user.getUserId() + " has no permission to access account with id " + accountDto.id());
        }

        Double newTransactionAmount = transactionDepositRequestDto.getAmount();

        if(newTransactionAmount <= 0) {
            throw new InvalidAmountException("The amount must be greater than 0");
        }

        if(newTransactionAmount > accountDto.transactionLimit()){
            throw new TransactionLimitExceededException("The transaction limit of " + accountDto.transactionLimit() + " was exceeded by a deposit of " + newTransactionAmount);
        }

        accountDto = accountService.increaseBalance(accountDto.id(), newTransactionAmount);

        TransactionDepositDto transactionDepositDto = new TransactionDepositDto(
                newTransactionAmount,
                transactionDepositRequestDto.getDescription());

        transactionDepositDto.setAccount(accountMapper.convertToEntity(accountDto));
        Transaction newTransaction = transactionRepository.save(transactionMapper.convertToEntity(transactionDepositDto));

        return transactionMapper.convertToTransactionDepositDto(newTransaction);
    }

    @Override
    public List<TransactionDetailDto> getTransactions(Integer userId) {
        List<Transaction> transactions = transactionRepository.findAll();
        List<Transaction> transactionsOfUser = transactions
                .stream()
                .filter(transaction ->
                        transaction.getAccount().getUser().getUserId().equals(userId))
                .toList();

        if(transactionsOfUser.isEmpty()){
            throw new ResourceNotFoundException("The user with id " +  userId +" has no transactions");
        }

        return convertTransactionListToDto(transactionsOfUser);

    }
    @Override
    public List<TransactionDetailDto> getTransactionsByAccount(Integer accountId) {
        List<Transaction> transactionsOfAccount = transactionRepository.findAllByAccountId(new Account(accountId));
        return convertTransactionListToDto(transactionsOfAccount);
    }


    @Override
    public TransactionDetailDto sendArs(String token, TransactionTransferRequestDto transactionTransferRequestDto) {
        return sendCurrency(token, transactionTransferRequestDto, Currency.ARS);
    }

    @Override
    public TransactionDetailDto sendUsd(String token, TransactionTransferRequestDto transactionTransferRequestDto) {
        return sendCurrency(token, transactionTransferRequestDto, Currency.USD);
    }

    private TransactionDetailDto sendCurrency(String token, TransactionTransferRequestDto transactionTransferRequestDto, Currency currency){
        String username = jwtUtil.extractClaimUsername(token.substring(7));
        User user = userService.loadUserByUsername(username);
        AccountDto accountReceiverDto = accountService.getAccountById(transactionTransferRequestDto.getAccountId());

        if(!accountReceiverDto.currency().equals(currency)){
            throw new InvalidAccountCurrencyException("The transfer currency must be equal to the currency of the receiver account.");
        }

        Account accountSenderDto = accountService.findAccountByUserIdAndCurrency(user, currency);

        TransactionPaymentRequestDto transactionPaymentRequestDto = new TransactionPaymentRequestDto(
                transactionTransferRequestDto.getAmount(),
                transactionTransferRequestDto.getDescription(),
                accountSenderDto.getAccountId()
        );

        TransactionPaymentDto transactionPayment = createPayment(transactionPaymentRequestDto, token);

        TransactionIncomeRequestDto transactionIncomeRequestDto = new TransactionIncomeRequestDto(
                transactionTransferRequestDto.getAmount(),
                transactionTransferRequestDto.getDescription(),
                accountReceiverDto.id()
        );
        createIncome(transactionIncomeRequestDto);
        return transactionMapper.convertPaymentDtoToDetailDto(transactionPayment);
    }

    @Override
    public TransactionPaymentDto createPayment(TransactionPaymentRequestDto transactionPaymentRequestDto, String token) {
        AccountDto accountDto = accountService.getAccountById(transactionPaymentRequestDto.getAccountId());
        String username = jwtUtil.extractClaimUsername(token.substring(7));
        User user = userService.loadUserByUsername(username);

        if(!accountService.hasUserAccountById(user.getUserId(), accountDto.id())){
            throw new ForbiddenAccessException("The user with id " + user.getUserId() + " has no permission to access account with id " + accountDto.id());
        }

        Double newTransactionAmount = transactionPaymentRequestDto.getAmount();
        accountDto = accountService.reduceBalance(accountDto.id(), newTransactionAmount);

        TransactionPaymentDto transactionPaymentDto = new TransactionPaymentDto(
                newTransactionAmount,
                transactionPaymentRequestDto.getDescription());


        if (newTransactionAmount <= 0) {
            throw new InvalidAmountException("The amount must be greater than 0");
        }

        if (newTransactionAmount > accountDto.transactionLimit()) {
            throw new TransactionLimitExceededException("The transaction limit of " + accountDto.transactionLimit() + " was exceeded by a payment of " + newTransactionAmount);
        }

        transactionPaymentDto.setAccount(accountMapper.convertToEntity(accountDto));
        Transaction newTransaction = transactionRepository.save(transactionMapper.convertToEntity(transactionPaymentDto));

        return transactionMapper.convertToTransactionPaymentDto(newTransaction);
    }

    public TransactionDetailDto createIncome(TransactionIncomeRequestDto transactionIncomeRequestDto) {
        Double newTransactionAmount = transactionIncomeRequestDto.getAmount();
        accountService.increaseBalance(transactionIncomeRequestDto.getAccountId(), newTransactionAmount);

        AccountDto accountDto = accountService.getAccountById(transactionIncomeRequestDto.getAccountId());
        TransactionIncomeDto transactionIncomeDto = new TransactionIncomeDto(
                newTransactionAmount,
                transactionIncomeRequestDto.getDescription()
        );

        transactionIncomeDto.setAccount(accountMapper.convertToEntity(accountDto));
        Transaction newTransaction = transactionMapper.convertToEntity(transactionIncomeDto);
        return transactionMapper.convertToTransactionDetailDto(transactionRepository.save(newTransaction));
    }

    public User getUserByTransactionId(Integer id) {
        var t = transactionRepository.findById(id);
        if(t.isPresent()){
            return t.get().getAccount().getUser();
        }else {
            throw new ResourceNotFoundException("Transaction does not exist");
        }
    }

    private List<TransactionDetailDto> convertTransactionListToDto(List<Transaction> transactions){
        return transactions
                .stream()
                .map(transaction -> transactionMapper.convertToTransactionDetailDto(transaction))
                .toList();

    }

    @Override
    public TransactionDetailDto updateTransaction(TransactionPatchDto transactionPatch, Integer transactionId, String userToken) throws ResourceNotFoundException {
        var transaction = transactionRepository.findById(transactionId);
        if(transaction.isPresent()) {
            userService.matchUserToToken(getUserByTransactionId(transactionId).getUserId(), userToken);
            transaction.get().setDescription(transactionPatch.description());
            return transactionMapper.convertToTransactionDetailDto(transactionRepository.save(transaction.get()));
        }else{
            throw new ResourceNotFoundException("Transaction does not exist");
        }
        }

    @Override
    public TransactionPaginatedDto paginateTransactionsByUser(Integer page, Integer userId, String token) {

        User user = userService.loadUserByUsername(jwtUtil.extractUserName(token.substring(7)));

        if(user.getRole().getName().name()=="USER"){
            throw new ForbiddenAccessException("Can only access if you are an Admin");
        }

        Pageable pageable = PageRequest.of(page,10);

        Page <Transaction> pagination = transactionRepository.findByAccount_User_UserId(userId,pageable);
        List<TransactionDetailDto> finalList = new ArrayList<>();

        for(Transaction transaction: pagination)
        {
            finalList.add(transactionMapper.convertToTransactionDetailDto(transaction));
        }


        TransactionPaginatedDto transactionPaginatedDto = new TransactionPaginatedDto();
        transactionPaginatedDto.setTransactionDetailDtoList(finalList);

        String url = "http://localhost:8080/transactions/all/"+userId+"?page=";

        if(pagination.hasNext())
            transactionPaginatedDto.setUrlNext(url+(page+1));
        else
            transactionPaginatedDto.setUrlNext("");

        if(pagination.hasPrevious())
            transactionPaginatedDto.setUrlPrevious(url+(page-1));
        else
            transactionPaginatedDto.setUrlPrevious("");

        return transactionPaginatedDto;
    }
}