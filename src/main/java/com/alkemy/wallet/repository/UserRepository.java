package com.alkemy.wallet.repository;

import com.alkemy.wallet.entity.UserEntity;
import javax.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  UserEntity findByUsername(String email);

}
