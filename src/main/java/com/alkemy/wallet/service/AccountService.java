
import com.alkemy.wallet.dto.AccountDto;
import com.alkemy.wallet.mapper.Mapper;
import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.repository.IAccountRepository;
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

}
