package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u.userId FROM User u WHERE u.email = ?1")
    int findUserIdByEmail(String email);
}
