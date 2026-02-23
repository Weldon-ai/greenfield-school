package com.greenfield.sms.repository;

import com.greenfield.sms.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.greenfield.sms.model.User;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    
    Optional<Admin> findByUserId(Long userId);
    Optional<Admin> findByUser(User user); 

}
