package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    // TODO: 8/12/2022 check if this method can be used
    @Query(value = "SELECT CASE WHEN COUNT(u.*) > 0 THEN " +
            "'true' ELSE 'false' END " +
            "FROM wallet.users u " +
            "WHERE u.email = ?", nativeQuery = true)
    Boolean selectExistsEmail(String email);

    @Query(value = "SELECT * FROM users WHERE email LIKE :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);
}
