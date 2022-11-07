package com.alkemy.wallet.repository;

import com.alkemy.wallet.enumeration.RoleList;
import com.alkemy.wallet.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(RoleList roleName);
}
