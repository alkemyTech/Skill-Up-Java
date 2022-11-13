package com.alkemy.wallet.dto;


import lombok.Data;



/**
 *  AccountDTOSlim class
 *  This class is used to show in JSON the attributes of the
 *  Account Class with the idea of only representing the balance and type.
 *  Since they are not necessary and it is not a good practice
 *  @Author Guido Molina
 */


@Data
public class AccountDTOSlim {
    private String currency;
    private Double balance;
}
