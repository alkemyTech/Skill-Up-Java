package com.alkemy.wallet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alkemy.wallet.model.User;

import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String username);

    User findByEmail(String email);

    Boolean existsById(long id);

    List<User> findAll();

    Page<User> findAll(Pageable pageable);

}
