package com.greenfield.sms.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import de.codecentric.boot.admin.server.config.EnableAdminServer;

@EnableAdminServer // Enables Spring Boot Admin server functionality
@SpringBootApplication // Marks this as a Spring Boot application
public class AdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminServerApplication.class, args); // Launches the server
    }
}