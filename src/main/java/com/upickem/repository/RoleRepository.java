package com.upickem.repository;

import com.upickem.model.Role;
import com.upickem.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleName roleName);
	
}
