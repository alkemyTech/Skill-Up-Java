package com.alkemy.wallet.dto;

import lombok.Getter;
import lombok.Setter;


import java.time.Instant;
@Getter
@Setter
public class AccountDTO {

    private Integer id;

    private String currency;

    private Double transactionLimit;

    private Double balance;

    private Instant updateDate;

    private Instant creationDate;

    public AccountDTO() {
    }
}
