package com.alkemy.wallet.model.mapper;

import com.alkemy.wallet.model.dto.request.FixedTermDepositRequestDto;
import com.alkemy.wallet.model.dto.response.FixedTermDepositResponseDto;
import com.alkemy.wallet.model.dto.response.list.FixedTermDepositListResponseDto;
import com.alkemy.wallet.model.entity.Account;
import com.alkemy.wallet.model.entity.FixedTermDeposit;
import com.alkemy.wallet.model.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public FixedTermDeposit dto2Entity(FixedTermDepositRequestDto dto, Double interest, LocalDate closingDate, Account account, User user) {
        return FixedTermDeposit.builder()
                .amount(dto.getAmount())
                .interest(interest)
                .creationDate(LocalDateTime.now())
                .closingDate(closingDate)
                .account(account)
                .user(user)
                .build();

    }

    public FixedTermDepositListResponseDto entityList2DtoList(List<FixedTermDeposit> entityList) {
        return FixedTermDepositListResponseDto.builder()
                .fixedTermDeposits(entityList.stream().map(this::entity2Dto).collect(Collectors.toList()))
                .build();
    }
}