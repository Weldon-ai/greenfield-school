package com.greenfield.sms.repository;

import com.greenfield.sms.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.greenfield.sms.model.User;
import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {

    // Find a parent by their linked User
    Optional<Parent> findByUserId(Long userId);

    // Find a parent by username via linked user
    Optional<Parent> findByUserUsername(String username);
    Optional<Parent> findByUser(User user); // ✅ Add this
}
