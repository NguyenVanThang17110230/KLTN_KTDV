package com.document.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DocumentManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DocumentManagerApplication.class, args);
    }

//    @Bean
//    CommandLineRunner run(UserService userService) throws Exception {
//        return args -> {
//            Role roleAdmin = new Role(null, "ROLE_ADMIN");
//            Role roleUser = new Role(null, "ROLE_USER");
//
//            if (userService.findRoleByName("ROLE_USER") == null) {
//                userService.save(roleUser);
//            }
//            if (userService.findRoleByName("ROLE_ADMIN") == null) {
//                userService.save(roleAdmin);
//            }
//            if (userService.findByEmail("admin@yopmail.com") == null) {
//                List<Role> roles = new ArrayList<>();
//                roles.add(roleAdmin);
//                roles.add(roleUser);
//
//                userService.save(new User(null,
//                        "10000000",
//                        "A",
//                        "Admin",
//                        Gender.MALE,
//                        new Date("01/01/1999"),
//                        "1111111111",
//                        "admin@yopmail.com",
//                        "12345678",
//                        true,
//                        new Timestamp(System.currentTimeMillis()),
//                        null,
//                        roles));
//            }
//            if (userService.findByEmail("user@yopmail.com") == null) {
//                List<Role> roles = new ArrayList<>();
//                roles.add(roleUser);
//
//                userService.save(new User(null,
//                        "10000001",
//                        "U",
//                        "User",
//                        Gender.MALE,
//                        new Date("02/02/2000"),
//                        "2222222222",
//                        "user@yopmail.com",
//                        "12345678",
//                        true,
//                        new Timestamp(System.currentTimeMillis()),
//                        null,
//                        roles));
//            }
//        };
//    }
}
