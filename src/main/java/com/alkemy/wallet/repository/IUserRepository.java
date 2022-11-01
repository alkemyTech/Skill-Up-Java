package com.alkemy.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alkemy.wallet.model.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

}
