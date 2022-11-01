package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.FixedTermDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FixedTermDepositRepository extends JpaRepository<FixedTermDeposit,Integer> {


}
