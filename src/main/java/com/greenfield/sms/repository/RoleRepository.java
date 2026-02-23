package com.greenfield.sms.repository;

import com.greenfield.sms.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Repository for Role entity
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
