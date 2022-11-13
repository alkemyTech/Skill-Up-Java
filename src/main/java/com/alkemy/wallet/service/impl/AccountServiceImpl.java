package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.dto.AccountDTOSlim;
import com.alkemy.wallet.dto.AccountPageDTO;
import com.alkemy.wallet.enumeration.CurrencyList;
import com.alkemy.wallet.enumeration.ErrorList;
import com.alkemy.wallet.exception.NotFoundException;
import com.alkemy.wallet.exception.TransactionException;
import com.alkemy.wallet.mapper.AccountMapper;
import com.alkemy.wallet.mapper.TransactionMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.TransactionRepository;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.security.config.JwtTokenProvider;
import com.alkemy.wallet.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private TransactionMapper transacctionMapper;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public AccountDTO createAccount(int userId, String currency) {
        // Encuentro la lista de cuentas con el UserId pasado por parametro
        List<Account> accountsByUserId = this.accountRepository.findAccountsByUserID(userId);
        // List<Account> accountsByUserId = this.accountRepository.findByUserID(userId);
        // TODO:Probar metodo para remplazar el de arriba
        // Busco si alguna de las cuentas pertenecientes al UserId ya tiene la currency
        // igual a la pasada por parametro
        boolean repeatedAccount = accountsByUserId.stream()
                .anyMatch(i -> currency.equals(i.getCurrency().name()));

        if (!repeatedAccount) {
            Account accountEntity = new Account();
            accountEntity.setCurrency(CurrencyList.valueOf(currency));
            accountEntity.setCreationDate(Instant.now());
            accountEntity.setBalance(0.0);
            if (currency.equals("USD"))
                accountEntity.setTransactionLimit(1000.0);
            else
                accountEntity.setTransactionLimit(300000.0);

            accountEntity.setUpdateDate(Instant.now());
            accountEntity.setUser(userRepository.findById(userId).get());
            accountRepository.save(accountEntity);

            return accountMapper.convertToAccountDTO(accountEntity);

        } else {
            return  null;
        }
    }

    @Override
    public List<AccountDTO> getAccountsByUser(Integer id) {
        if(userRepository.findById(id).isEmpty())
            throw new NotFoundException(ErrorList.OBJECT_NOT_FOUND.getMessage());

        List<Account> result = this.accountRepository.findAccountsByUserID(id);

        return accountMapper.accountEntityList2DTOList(result);
    }

    @Override
    public Map<String, Object> getAccounts() {
        List<Account> accounts = accountRepository.findAll();
        Map accountsMap = new HashMap<String, List<Account>>() {
            {
                put("Accounts", accounts);
            }
        };
        return accountsMap;
    }

    @Override
    public AccountPageDTO getAccountsByPage(Integer page) {
        Pageable pageWithTenElements = PageRequest.of(page - 1, 10);
        Page<Account> accounts = accountRepository.findAll(pageWithTenElements);
        List<Account> accountsList = accounts.getContent();

        AccountPageDTO accountPageDTO = new AccountPageDTO();
        accountPageDTO.setTotalPages(accounts.getTotalPages());

        ServletUriComponentsBuilder nextPageBuilder = ServletUriComponentsBuilder.fromCurrentRequestUri();
        nextPageBuilder.scheme("http");
        nextPageBuilder.replaceQueryParam("page", page + 1);

        ServletUriComponentsBuilder previousPageBuilder = ServletUriComponentsBuilder.fromCurrentRequestUri();
        previousPageBuilder.scheme("http");
        previousPageBuilder.replaceQueryParam("page", page - 1);

        accountPageDTO.setNextPage(accounts.getTotalPages() == page ? null : nextPageBuilder.build().toUriString());
        accountPageDTO.setPreviusPage(page == 1 ? null : previousPageBuilder.build().toUriString());

        accountPageDTO.setUserDTOList(accountMapper.accountEntityList2DTOList(accountsList));

        return accountPageDTO;
    }

    @Override
    public AccountDTO createAccountWithToken(String token, String currency) {
        //Retrieve user on token:
        Authentication authentication = jwtTokenProvider.getAuthentication(token.substring(7));
        User authUser = userRepository.findByEmail(authentication.getName());

        // Encuentro la lista de cuentas con el token pasado por parametro
        List<Account> accountsByUserId = this.accountRepository.findAccountsByUserID(authUser.getId());

        // Busco si alguna de las cuentas pertenecientes al UserId ya tiene la currency igual a la pasada por parametro
        boolean repeatedAccount = accountsByUserId.stream()
                .anyMatch(i -> currency.equals(i.getCurrency().name()));

        if (!repeatedAccount) {
            Account accountEntity = new Account();
            accountEntity.setCurrency(CurrencyList.valueOf(currency));
            accountEntity.setCreationDate(Instant.now());
            accountEntity.setBalance(0.0);
            if (currency.equals("USD"))
                accountEntity.setTransactionLimit(1000.0);
            else
                accountEntity.setTransactionLimit(300000.0);

            accountEntity.setUpdateDate(Instant.now());
            accountEntity.setUser(userRepository.findById(authUser.getId()).get());
            accountRepository.save(accountEntity);

            return accountMapper.convertToAccountDTO(accountEntity);

        } else {
            throw new TransactionException(ErrorList.ACCOUNT_UNIQUE.getMessage());
        }

    }

}
