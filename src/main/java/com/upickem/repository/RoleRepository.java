package com.upickem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upickem.model.Role;
import com.upickem.model.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleName roleName);
	
}
