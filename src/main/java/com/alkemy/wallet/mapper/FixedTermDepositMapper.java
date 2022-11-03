package com.alkemy.wallet.mapper;

import com.alkemy.wallet.dto.FixedTermDepositDto;
import com.alkemy.wallet.model.FixedTermDeposit;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class FixedTermDepositMapper {

    private final ModelMapper mapper;

    public FixedTermDepositDto convertToDto(FixedTermDeposit fixedTermDeposit ) {
        return mapper.map( fixedTermDeposit, FixedTermDepositDto.class );
    }

    public FixedTermDeposit convertToEntity(FixedTermDepositDto fixedTermDepositDto) {
        return mapper.map(fixedTermDepositDto, FixedTermDeposit.class );
    }
}
