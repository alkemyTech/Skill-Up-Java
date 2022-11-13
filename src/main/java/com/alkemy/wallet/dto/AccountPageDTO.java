package com.alkemy.wallet.dto;

import lombok.Data;

import java.util.List;
@Data
public class AccountPageDTO {
    String nextPage;
    String previusPage;
    int totalPages;
    List<AccountDTO> userDTOList;
}
