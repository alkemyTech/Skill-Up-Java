package com.alkemy.wallet.service.interfaces;

import com.alkemy.wallet.dto.FixedTermDto;
import com.alkemy.wallet.dto.SimulatedFixedTermDto;

public interface IFixedTermService {

    FixedTermDto createFixedTerm(FixedTermDto fixedTermDto);

    SimulatedFixedTermDto simulateFixedTerm(SimulatedFixedTermDto fixedTermDto);
}
