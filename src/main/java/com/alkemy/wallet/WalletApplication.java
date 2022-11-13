package com.alkemy.wallet;

import com.alkemy.wallet.model.Account;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Spring Bugs", version = "1.0.0", description = "Wallet API"))
public class WalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletApplication.class, args);

	}
}
