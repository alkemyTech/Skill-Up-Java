package com.alkemy.wallet.dto;

import com.alkemy.wallet.model.TypeEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class TypeDTO {
    private TypeEnum type;
}
