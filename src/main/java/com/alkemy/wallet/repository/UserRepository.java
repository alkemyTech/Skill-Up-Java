package com.alkemy.wallet.repository;

import com.alkemy.wallet.dto.UsersDTO;
import com.alkemy.wallet.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserByEmail(String email);

    @Query("SELECT email FROM UserEntity")
    List<String> findAllUsers();
}
