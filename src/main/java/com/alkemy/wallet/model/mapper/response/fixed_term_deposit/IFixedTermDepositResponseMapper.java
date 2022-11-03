package com.alkemy.wallet.model.mapper.response.fixed_term_deposit;

import com.alkemy.wallet.model.entity.FixedTermDeposit;
import com.alkemy.wallet.model.response.FixedTermDepositResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IFixedTermDepositResponseMapper {
    @Mapping(target = "userResponseDto", source = "user")
    @Mapping(target = "userId", source = "fkUserId")
    @Mapping(target = "accountResponseDto", source = "account")
    @Mapping(target = "accountId", source = "fkAccountId")
    FixedTermDepositResponseDto fixedTermDepositToFixedTermDepositResponse(FixedTermDeposit fixedTermDeposit);
}
