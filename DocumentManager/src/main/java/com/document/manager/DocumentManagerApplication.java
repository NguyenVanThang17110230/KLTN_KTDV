package com.document.manager;

import com.document.manager.domain.Role;
import com.document.manager.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DocumentManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DocumentManagerApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            if (userService.findRoleByName("ROLE_USER") == null) {
                userService.save(new Role(null, "ROLE_USER"));
            }
            if (userService.findRoleByName("ROLE_ADMIN") == null) {
                userService.save(new Role(null, "ROLE_ADMIN"));
            }
        };
    }
}
