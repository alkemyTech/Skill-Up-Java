package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.FixedTermDepositResponseDTO;
import com.alkemy.wallet.model.FixedTermDeposit;
import org.springframework.stereotype.Component;

@Component
public class FixedTermDepositMapper {

    public FixedTermDeposit dtoToEntity(FixedTermDepositResponseDTO responseDTO) {
        FixedTermDeposit fixedTermDeposit = new FixedTermDeposit();
        fixedTermDeposit.setAmount(responseDTO.getAmount());
        fixedTermDeposit.setInterest(responseDTO.getInterest());
        fixedTermDeposit.setAccount(responseDTO.getAccount());
        fixedTermDeposit.setCreationDate(responseDTO.getCreationDate());
        fixedTermDeposit.setUpdateDate(responseDTO.getClosingDate());
        return fixedTermDeposit;
    }

}
