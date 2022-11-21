package com.alkemy.wallet.model.mapper;

import com.alkemy.wallet.model.dto.response.FixedTermDepositResponseDto;
import com.alkemy.wallet.model.dto.response.list.FixedTermDepositListResponseDto;
import com.alkemy.wallet.model.entity.FixedTermDeposit;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public FixedTermDepositListResponseDto entityList2DtoList(List<FixedTermDeposit> entityList) {
        return FixedTermDepositListResponseDto.builder()
                .fixedTermDeposits(entityList.stream().map(this::entity2Dto).collect(Collectors.toList()))
                .build();
    }
}