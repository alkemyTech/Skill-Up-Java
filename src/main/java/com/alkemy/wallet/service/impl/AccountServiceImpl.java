package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.AccountDTO;
import com.alkemy.wallet.enumeration.CurrencyList;
import com.alkemy.wallet.mapper.AccountMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.repository.AccountRepository;
import com.alkemy.wallet.repository.UserRepository;
import com.alkemy.wallet.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private AccountMapper accountMapper;

    @Override
    public AccountDTO createAccount(int userId, String currency) {
        // Encuentro la lista de cuentas con el UserId pasado por parametro
        List<Account> accountsByUserId = this.accountRepository.findByUser(userRepository.findById(userId).get());
        //List<Account> accountsByUserId = this.accountRepository.findByUserID(userId); TODO:Probar metodo para remplazar el de arriba
        // Busco si alguna de las cuentas pertenecientes al UserId ya tiene la currency igual a la pasada por parametro
        boolean repeatedAccount = accountsByUserId.stream()
                .anyMatch(i -> currency.equals(i.getCurrency().name()));

        if(!repeatedAccount){
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
        List<Account> result = accountRepository.findByUser(userRepository.findById(id).get());
        return accountMapper.accountEntityList2DTOList(result);
    }
    @Override
    public Map<String, Object>getAccounts() {
        List<Account> accounts = accountRepository.findAll();
        Map accountsMap = new HashMap<String, List<Account>>(){{put("Accounts",accounts);}};
        return accountsMap;
    }

    @Override
    public Map<String, Object> getAccountsByPage(Integer page) {
/*        String host = "";
        Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
        if (inputStream!=null) {
            try {
                properties.load(inputStream);
                host = properties.getProperty("server.port");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        *//*UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http").host("www.localhost:" + host + "/accounts/pages")
                .path("/").query("q={keyword}").buildAndExpand("baeldung");*/

        Pageable pageWithTenElements = PageRequest.of(page - 1, 10);
        pageWithTenElements.next();
        Page<Account> accounts =  accountRepository.findAll(pageWithTenElements);
        List<Account> accountsList = accounts.getContent();
        List<AccountDTO> accountDTOList = accountMapper.accountEntityList2DTOList(accountsList);
        Map<String, Object> response = new HashMap<>();
        if (accounts.hasNext() || accounts.hasPrevious()) {
            ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
            builder.scheme("https");
            if (accounts.hasNext()) {
                builder.replaceQueryParam("page", accounts.getPageable().getPageNumber() + 1);
                response.put("Next page url", builder.build().toUri());
            }
            if (accounts.hasPrevious()){
                builder.replaceQueryParam("page", accounts.getPageable().getPageNumber());
                response.put("Prevoius page url", builder.build().toUri());
            }
        }
        response.put("Accounts", accountDTOList);
/*        response.put("currentPage", accounts.getNumber());
        response.put("totalItems", accounts.getTotalElements());
        response.put("totalPages", accounts.getTotalPages());
        response.put("Next page", accounts.hasNext());
        response.put("Has previous page", accounts.hasPrevious());
        response.put("Has previous page", accounts.getPageable().getPageNumber());*/
        return response;
    }

}
