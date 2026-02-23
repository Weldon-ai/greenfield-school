package com.greenfield.sms.security;

import com.greenfield.sms.model.User;
import com.greenfield.sms.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // ===== Find user by username =====
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // ===== Map roles to GrantedAuthority =====
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> {
                    // Ensure ROLE_ prefix exists for Spring Security
                    String roleName = role.getName().startsWith("ROLE_") ? role.getName() : "ROLE_" + role.getName();
                    return new SimpleGrantedAuthority(roleName);
                })
                .collect(Collectors.toSet());

        // ===== Build Spring Security UserDetails =====
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
    }
}
