/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.alkemy.wallet.repository;

import com.alkemy.wallet.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author marti
 */
public interface IRoleRepository extends JpaRepository<Role, String> {

}
