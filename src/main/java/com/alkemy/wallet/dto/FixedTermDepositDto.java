package com.alkemy.wallet.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixedTermDepositDto {

  private Long id;
  private Double amount;
  private Long userId;
  private Long accountId;
  private Double interest;
  private Date creationDate;
  private Date closingDate;
}
