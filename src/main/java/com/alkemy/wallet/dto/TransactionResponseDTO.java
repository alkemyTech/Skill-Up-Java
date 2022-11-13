package com.alkemy.wallet.dto;

import com.alkemy.wallet.enumeration.TypeList;
import lombok.Data;

@Data
public class TransactionResponseDTO {

    Double amount;
    TypeList type;
    String description;
}
