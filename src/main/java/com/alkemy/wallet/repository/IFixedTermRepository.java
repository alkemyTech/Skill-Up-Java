package com.alkemy.wallet.repository;

import com.alkemy.wallet.dto.FixedTermDto;
import com.alkemy.wallet.model.FixedTermDeposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFixedTermRepository extends JpaRepository<FixedTermDeposit, Long> {

    List<FixedTermDeposit> findAllByAccount_Id(Long id);
}
