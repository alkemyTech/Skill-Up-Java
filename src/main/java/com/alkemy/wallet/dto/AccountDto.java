package com.alkemy.wallet.dto;

import java.util.Currency;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {

  private Long id;
  private Currency currency;
  private Double transactionLimit;
  private Double balance;
  private Date updateDate;
  private Date creationDate;

}
