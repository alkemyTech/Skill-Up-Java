package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.dto.BalanceDTO;
import com.alkemy.wallet.model.UserEntity;
import com.alkemy.wallet.repository.BankDAO;
import com.alkemy.wallet.service.IBalanceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BalanceServiceImpl implements IBalanceService {
    private final BankDAO bankDAO;
    @Override
    public List<BalanceDTO> getBalance() {
        UserEntity user = bankDAO.findUserByEmail(bankDAO.returnUserName());
        return user.getAccount().stream().map(accountEntity -> new BalanceDTO(accountEntity)).collect(Collectors.toList());
    }
}
