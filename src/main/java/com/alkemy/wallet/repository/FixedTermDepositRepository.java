package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.FixedTermDeposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FixedTermDepositRepository extends JpaRepository<FixedTermDeposit, Integer> {
}
