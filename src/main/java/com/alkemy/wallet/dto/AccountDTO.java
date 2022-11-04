package com.alkemy.wallet.dto;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
public class AccountDTO {

    private Integer id;
    private String currency;
    private Double transactionLimit;
    private Double balance;
    private Instant updateDate;
    private Instant creationDate;
    private boolean softDelete = Boolean.FALSE;
}