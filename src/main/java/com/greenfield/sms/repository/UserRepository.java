package com.greenfield.sms.repository;

import com.greenfield.sms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository; // ✅ Import this

import java.util.List;
import java.util.Optional;

@Repository // ✅ Important: marks this interface as a Spring Bean
public interface UserRepository extends JpaRepository<User, Long> {

    // ======================================================
    // FIND USERS BY UNIQUE FIELDS
    // ======================================================

    Optional<User> findByUsername(String username); // For login and validation
    boolean existsByUsername(String username);      // Check if username exists

    Optional<User> findByEmail(String email);      // Check if email exists
    boolean existsByEmail(String email);           // Boolean check for email

    Optional<User> findByFullName(String fullName); // Optional search by full name

    // ======================================================
    // FIND USERS BY ROLE NAME
    // ======================================================

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name IN :roleNames")
    List<User> findByRoleNames(@Param("roleNames") List<String> roleNames);
}