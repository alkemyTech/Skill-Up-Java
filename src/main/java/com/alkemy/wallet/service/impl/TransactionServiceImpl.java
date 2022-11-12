package com.alkemy.wallet.service.impl;


import com.alkemy.wallet.dto.TransactionCreateDTO;
import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.dto.TransactionUpdateDTO;
import com.alkemy.wallet.enumeration.ErrorList;
import com.alkemy.wallet.exception.TransactionException;
import com.alkemy.wallet.mapper.TransactionMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.security.config.JwtTokenProvider;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.impl.transaction.util.ITransactionStrategy;
import com.alkemy.wallet.service.impl.transaction.util.PaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public TransactionDTO getTransactionById(Integer id , Integer user_id ) {

        Transaction transEntity = this.checkIdBtwRequestUserAndUserTrans(id, user_id);

        return transactionMapper.transEntity2DTO(transEntity);
    }

    @Override
    public List<TransactionDTO> findAllByUserId(Integer userId, Integer page) {
        Pageable pageWithTenElements = PageRequest.of(page, 10);

        //Traer cuentas de userId
        List<Account> accounts = accountRepository.findAccountsByUserID(userId);
        //Obtener todos los id de las cuentas
        List<Integer> accIds = new ArrayList<>();
        accounts.forEach((Account acc) ->
                accIds.add(acc.getId()));

        //buscar las transacciones de esas cuentas de ese usuario
        List<Transaction> allTransByUser = transactionRepository.findAllByAccountsId(accIds, pageWithTenElements);
        List<TransactionDTO> transList = transactionMapper.transListEntity2ListDTO(allTransByUser);
        return transList;
    }

    @Override
    public void makeTransaction(TransactionCreateDTO transDTO, ITransactionStrategy strategy) {
        //EN DTO VIENE LA CUENTA DESTINO DEL INCOME / DESTINO DEL DEPOSIT
        Account recieverAccount = accountRepository.findById(transDTO.getAccount_id()).orElseThrow(
                () -> new TransactionException(ErrorList.OBJECT_NOT_FOUND.getMessage()));

        strategy.make(transDTO.getAmount(), recieverAccount);
        Transaction newTrans = transactionMapper.transCreateDTO2Entity(transDTO);
        newTrans.setType(strategy.type());
        transactionRepository.save(newTrans);
    }
    @Override
    public void makeTransaction(TransactionCreateDTO transDTO, ITransactionStrategy strategy,String token) {
        //Retrieve user on token:
        Authentication authentication = jwtTokenProvider.getAuthentication(token.substring(7));
        User senderUser = userRepository.findByEmail(authentication.getName());


        Account recieverAccount = accountRepository.findById(transDTO.getAccount_id()).orElseThrow(
                () -> new TransactionException(ErrorList.OBJECT_NOT_FOUND.getMessage()));


        List<Account> senderAccs = accountRepository.findAccountsByUserID(senderUser.getId());
        Account senderAccount = senderAccs.stream().filter(
                        acc -> acc.getCurrency().equals(recieverAccount.getCurrency()))
                .findFirst()
                .orElseThrow(() -> new TransactionException(ErrorList.OBJECT_NOT_FOUND.getMessage()));

        if (recieverAccount.getId().equals(senderAccount.getId())) { throw new TransactionException(ErrorList.SAME_ACCOUNT.getMessage());}


        strategy.make(transDTO.getAmount(), senderAccount);//accOrigin
        Transaction newTrans = transactionMapper.transCreateDTO2Entity(transDTO);
        newTrans.setAccount_id(senderAccount.getId());//AccOrigin
        newTrans.setType(strategy.type());
        transactionRepository.save(newTrans);
    }

    @Override
    public void makeTransaction(ITransactionStrategy strategy, Account account, Double amount) {
        TransactionCreateDTO transDTO = new TransactionCreateDTO();
        transDTO.setAmount(amount);
        transDTO.setDescription("New FixedTermDeposit");
        transDTO.setAccount_id(account.getId());
        Transaction newTrans = transactionMapper.transCreateDTO2Entity(transDTO);
        newTrans.setType(strategy.type());
        strategy.make(amount, account);
        transactionRepository.save(newTrans);

    }

    @Override
    public void updateTransaction(TransactionUpdateDTO transDTO, Integer id, Integer user_id){
        Optional<Transaction> optTrans = transactionRepository.findById(id);
        if (optTrans.isEmpty()){
            throw new TransactionException(ErrorList.OBJECT_NOT_FOUND.getMessage());
        }
        Integer dbUserId = optTrans.get().getAccount().getUser().getId();

        if( dbUserId != user_id){
            throw new TransactionException(ErrorList.NOT_MATCHING_IDS.getMessage());
        }
        String newDescription = transDTO.getDescription();
        transactionRepository.updateDescription(id , newDescription);
    }

    private Transaction checkIdBtwRequestUserAndUserTrans(Integer id, Integer user_id){
        Optional<Transaction> optTrans = transactionRepository.findById(id);
        if (optTrans.isEmpty()){
            throw new TransactionException(ErrorList.OBJECT_NOT_FOUND.getMessage());
        }
        Transaction transEntity = optTrans.get();
        Integer dbUserId = transEntity.getAccount().getUser().getId();

        if( dbUserId != user_id){
            throw new TransactionException(ErrorList.NOT_MATCHING_IDS.getMessage());
        }
        return transEntity;
    }
}
