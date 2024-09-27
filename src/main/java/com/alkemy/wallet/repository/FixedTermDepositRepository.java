package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.FixedTermDepositEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FixedTermDepositRepository extends JpaRepository<FixedTermDepositEntity, Long> {
}
