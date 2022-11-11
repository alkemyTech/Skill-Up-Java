package com.alkemy.wallet.service.impl;


import com.alkemy.wallet.dto.TransactionCreateDTO;
import com.alkemy.wallet.dto.TransactionDTO;
import com.alkemy.wallet.dto.TransactionUpdateDTO;
import com.alkemy.wallet.enumeration.ErrorList;
import com.alkemy.wallet.exception.TransactionException;
import com.alkemy.wallet.mapper.TransactionMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.service.ITransactionService;
import com.alkemy.wallet.service.impl.transaction.util.ITransactionStrategy;
import com.alkemy.wallet.service.impl.transaction.util.PaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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


    @Override
    public TransactionDTO getTransactionById(Integer id) {
        Optional<Transaction> optTrans = transactionRepository.findById(id);
        if (optTrans.isEmpty()){
            throw new TransactionException(ErrorList.OBJECT_NOT_FOUND.getMessage());
        }
        Transaction transEntity = optTrans.get();
        return transactionMapper.transEntity2DTO(transEntity);
    }

    @Override
    public List<TransactionDTO> getAllByUserId(Integer userId) {
        //TODO : Check if user:id is the same user logged.
        // - For this task need wait JWT implementation
        // - Take the username(email) in Token and after this find all transactions
        Pageable pageWithTenElements = PageRequest.of(0, 10);
        //List<Transaction> allTransByUser = transactionRepository.findAllByUser(userId, pageWithTenElements);
        //List<TransactionDTO> transList = transactionMapper.transListEntity2ListDTO(allTransByUser);
        return null;
    }

    @Override
    public void makeTransaction(TransactionCreateDTO transDTO, ITransactionStrategy strategy) {
        //EN DTO VIENE LA CUENTA ORIGEN DEL PAYMENT / DESTINO DEL DEPOSIT
        Optional<Account> optAcc = accountRepository.findById(transDTO.getAccount_id());
        if (optAcc.isEmpty()){
            throw new TransactionException(ErrorList.OBJECT_NOT_FOUND.getMessage());
        }
        Account account = optAcc.get();

        strategy.make(transDTO.getAmount(), account);
        Transaction newTrans = transactionMapper.transCreateDTO2Entity(transDTO);
        newTrans.setType(strategy.type());
        transactionRepository.save(newTrans);
    }
    @Override
    public void makeTransaction(TransactionCreateDTO transDTO, ITransactionStrategy strategy,String accDestinyEmail) {
        // EN DTO VIENE LA CUENTA ORIGEN DEL INCOME
        Optional<Account> optAcc = accountRepository.findById(transDTO.getAccount_id());
        if (optAcc.isEmpty()){
            throw new TransactionException(ErrorList.OBJECT_NOT_FOUND.getMessage());
        }
        Account accDestiny = optAcc.get();
        if(!accountRepository.findById(1).get().getCurrency().name()
                .equals(accountRepository.findById(transDTO.getAccount_id()).get().getCurrency().name())){
            throw new TransactionException(ErrorList.ACCOUNTS_DIFERENT_CURRENCY.getMessage());
        };

        strategy.make(transDTO.getAmount(), accountRepository.findById(1).get());//accDestiny
        Transaction newTrans = transactionMapper.transCreateDTO2Entity(transDTO);
        newTrans.setAccount_id(1);//AccDestiny
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
    public void updateTransaction(TransactionUpdateDTO transDTO, Integer id) {
        Optional<Transaction> optTrans = transactionRepository.findById(id);
        if (optTrans.isEmpty()){
            throw new TransactionException(ErrorList.OBJECT_NOT_FOUND.getMessage());
        }
        String newDescription = transDTO.getDescription();
        transactionRepository.updateDescription(id , newDescription);
    }
    /*


    */
}
