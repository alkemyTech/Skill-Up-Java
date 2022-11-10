package com.alkemy.wallet.dto;

import lombok.Data;

import java.util.List;

@Data
public class TransactionPaginatedDto {

    List<TransactionDetailDto> transactionDetailDtoList;
    String urlNext;
    String urlPrevious;

}
