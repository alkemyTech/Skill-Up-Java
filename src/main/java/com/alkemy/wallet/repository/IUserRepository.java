package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.alkemy.wallet.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends PagingAndSortingRepository<User, Long>, JpaRepository<User, Long> {

    Boolean existsByEmail(String username);

    User findByEmail(String email);

    Optional<User> findOptionalByEmail(String email);

    Boolean existsById(long id);

    List<User> findAll();

    Page<User> findAll(Pageable pageable);

}
