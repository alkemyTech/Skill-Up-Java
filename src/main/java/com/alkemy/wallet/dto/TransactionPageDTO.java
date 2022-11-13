package com.alkemy.wallet.dto;


import lombok.Data;

import java.util.List;

@Data
public class TransactionPageDTO {


    String nextPage;
    String previusPage;
    int totalPages;
    List<TransactionDTO> transDTOList;
}
