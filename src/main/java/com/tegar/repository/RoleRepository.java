package com.tegar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tegar.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	Role findByRoleName(String roleName);

}
