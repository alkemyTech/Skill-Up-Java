package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "SELECT * FROM roles WHERE name LIKE :name", nativeQuery = true)
    Optional<Role> findByName(@Param("name") String name);
}
