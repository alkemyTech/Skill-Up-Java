package com.alkemy.wallet.model.mapper.request;

import com.alkemy.wallet.model.dto.request.FixedTermDepositRequestDto;
import com.alkemy.wallet.model.entity.FixedTermDeposit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IFixedTermDepositRequestMapper {
    @Mapping(target = "fkUserId", source = "userId")
    @Mapping(target = "fkAccountId", source = "accountId")
    FixedTermDeposit fixedTermDepositRequestToFixedTermDeposit(FixedTermDepositRequestDto fixedTermDepositRequestDto);
}
