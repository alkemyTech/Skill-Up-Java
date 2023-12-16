package com.alkemy.wallet.model.mapper;

import com.alkemy.wallet.model.dto.response.FixedTermDepositResponseDto;
import com.alkemy.wallet.model.entity.FixedTermDeposit;
import org.springframework.stereotype.Component;

@Component
public class FixedTermDepositMapper {

    public FixedTermDepositResponseDto entity2Dto(FixedTermDeposit entity) {
        return FixedTermDepositResponseDto.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .userId(entity.getUser().getId())
                .accountId(entity.getAccount().getId())
                .interest(entity.getInterest())
                .createdAt(entity.getCreationDate())
                .closingDate(entity.getClosingDate())
                .build();
    }
}