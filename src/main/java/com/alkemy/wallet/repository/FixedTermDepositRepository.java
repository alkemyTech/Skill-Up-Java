package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.entity.FixedTermDeposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FixedTermDepositRepository extends JpaRepository<FixedTermDeposit, Long> {
}
