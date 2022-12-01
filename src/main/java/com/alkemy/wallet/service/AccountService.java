<<<<<<< HEAD
package com.alkemy.wallet.service;
=======
>>>>>>> de34d5630299564348cc9f34d9606d51c84044f6

import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.repository.IAccountRepository;
<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    Mapper mapper;

    @Autowired
    IAccountRepository accountRepository;

    public List<AccountDto> getByUserId(@Valid Long user_id) {
        return accountRepository.findByUserId(user_id).stream().map(account -> mapper.getMapper().map(account, AccountDto.class)).collect(Collectors.toList());
    }
=======
import com.alkemy.wallet.service.interfaces.IAccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
public class AccountService implements IAccountService {


    private IAccountRepository accountRepository;


    private ModelMapper mapper;

    public AccountService(IAccountRepository accountRepository, ModelMapper mapper) {
        this.accountRepository = accountRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAccountsByUserId( Long userId ) throws EmptyResultDataAccessException{
        List<Account> accounts = accountRepository.getAccountsByUser(userId);

        if (accounts.isEmpty()){
            throw new EmptyResultDataAccessException("El usuario no posee cuentas",1 );
        }
        return accounts.stream().map(account ->
                    mapper.map(account, AccountDto.class)
        ).toList();

    }

>>>>>>> de34d5630299564348cc9f34d9606d51c84044f6
}
