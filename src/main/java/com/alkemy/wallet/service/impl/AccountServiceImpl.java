package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.*;
import com.alkemy.wallet.exceptions.BadRequestException;
import com.alkemy.wallet.exceptions.ResourceNotFoundException;
import com.alkemy.wallet.mapper.IAccountMapper;
import com.alkemy.wallet.mapper.IFixedTermDepositMapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IUserRepository;
import com.alkemy.wallet.security.service.IJwtUtils;
import com.alkemy.wallet.service.IAccountService;
import com.alkemy.wallet.service.IFixedDepositService;
import com.alkemy.wallet.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.alkemy.wallet.model.FixedTermDeposit;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.alkemy.wallet.model.ECurrency.ARS;
import static com.alkemy.wallet.model.ECurrency.USD;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService {
    public static final Double LIMIT_ARS = 300000.00;
    public static final Double LIMIT_USD = 1000.00;
    private final IAccountRepository iAccountRepository;
    private final IJwtUtils jwtUtils;
    private final IUserRepository userRepository;
    private final IUserService iUserService;
    private final IAccountMapper accountMapper;
    private static final Integer ACCOUNTSFORPAGE = 10;
    private IFixedDepositService fixedDepositService;
    private IFixedTermDepositMapper fixedTermDepositMapper;


    @Override
    public List<ResponseAccountDto> findAllByUser(Long id) throws ResourceNotFoundException {
        User user = iUserService.findById(id).orElseThrow(()-> new ResourceNotFoundException("Not found User with number id: "+ id));
        return accountMapper.accountsToAccountsDto(iAccountRepository.findAllByUserId(user.getId()));
    }

    @Override
    public Account findById(Long id) throws ResourceNotFoundException {
        return iAccountRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Not found Account with number id: "+ id));
    }

    @Override
    public ResponseAccountDto updateAccount(Long accountId, UpdateAccountDto requestAccount, String token) throws ResourceNotFoundException {
        String userName =  jwtUtils.extractUsername(jwtUtils.getJwt(token));
        Account account = this.findById(accountId);
        if (!account.getUser().getEmail().equals(userName))
                throw new AccessDeniedException("You don't have permission to access this resource");
        account.setTransactionLimit(requestAccount.getTransactionLimit());
        return accountMapper.accountToAccountDto(iAccountRepository.save(account));
    }

	@Override
	public ResponseUserBalanceDto getBalance(String token) {
		Long userId = jwtUtils.extractUserId(jwtUtils.getJwt(token));
		//User user = iUserService.getUserById(userId); TODO if foreach function is correct, delete this line
		ResponseUserBalanceDto dto = new ResponseUserBalanceDto();
		
		dto.setId(userId);
		
		for(Account account : iAccountRepository.findAllByUserId(userId)) {
		  AccountBalanceDto balanceDto = accountMapper.accountToBalanceDto(account);
		  
		  Double allDeposit = .0;
		  for(FixedTermDeposit deposit: fixedDepositService.findAllBy(account)) {
		    dto.getDepositDtos()
		      .add(fixedTermDepositMapper.toResponseFixedDepositDto(deposit));
		    
		    allDeposit += deposit.getAmount();
		  }
		  
		  balanceDto.setBalance(balanceDto.getBalance() - allDeposit);
		  
          dto.getAccountBalanceDtos()
            .add(balanceDto);
      
		}
		return dto;
	}


    @Override
    public String addAccount(String email, CurrencyDto currency) throws Exception {
        int countByUserId = iAccountRepository.countByUserId(userRepository.findByEmail(email).getId()).intValue();
        if (countByUserId < 0 || countByUserId >1){
            throw new ResourceNotFoundException("You have 2 associated accounts");
        }else{
            if (iAccountRepository.getReferenceByUserId(userRepository.findByEmail(email).getId()) != null) {
                Account accRegistered = iAccountRepository.getReferenceByUserId(userRepository.findByEmail(email).getId());
                if (accRegistered.getCurrency() == currency.getCurrency()) {
                    throw new ResourceNotFoundException("You already have an " + currency.getCurrency() + " associated account.");
                }
            }
            Account account = createAccount(currency);
            User userLogged = userRepository.findByEmail(email);
            account.setUser(userLogged);
            iAccountRepository.save(account);
            return HttpStatus.CREATED.getReasonPhrase();
        }
    }

    @Override
    public Account createAccount(CurrencyDto currency) {
        Account account = new Account();
        ECurrency currencyDto = currency.getCurrency();
        if(currencyDto == ARS){
            account.setCurrency(ARS);
            account.setTransactionLimit(LIMIT_ARS);
        }else{
            account.setCurrency(USD);
            account.setTransactionLimit(LIMIT_USD);
        }
        account.setDeleted(false);
        account.setBalance(0.00);
        return account;
    }

    @Override
    public ResponseAccountsDto findAll(Integer page, HttpServletRequest httpServletRequest) {
        ResponseAccountsDto dto = new ResponseAccountsDto();
        if (page == null) {
            dto.setAccountsDto(
                    accountMapper.accountsToAccountsDto(
                            iAccountRepository.findAll()));
            return dto;
        }

        Page<Account> accounts = iAccountRepository.findAll(
                PageRequest.of(page, ACCOUNTSFORPAGE));

        if (accounts.isEmpty())
            throw new BadRequestException();

        dto.setAccountsDto(
                accountMapper.accountsToAccountsDto(
                        accounts.toList()));

        // url
        String url = httpServletRequest
                .getRequestURL().toString() + "?" + "page=";

        if(accounts.hasPrevious()) {
            int previousPage = accounts.getNumber() - 1;
            dto.setPreviousPage(url + previousPage);
        }

        if (accounts.hasNext()) {
            int nextPage = accounts.getNumber() + 1;
            dto.setNextpage(url + nextPage);
        }
        return dto;
    }
}
