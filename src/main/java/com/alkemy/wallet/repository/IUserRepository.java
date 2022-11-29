package com.alkemy.wallet.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.alkemy.wallet.model.User;

@Repository
public interface IUserRepository extends CrudRepository<User, Long>{
	
}
