package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {
    @Query("SELECT r FROM RoleEntity r WHERE r.roleId = :roleId")
    Optional<RoleEntity> getRoleById(@Param("roleId") int roleId);
}
