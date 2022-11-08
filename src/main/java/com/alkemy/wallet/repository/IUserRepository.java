package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE email LIKE :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);
}
