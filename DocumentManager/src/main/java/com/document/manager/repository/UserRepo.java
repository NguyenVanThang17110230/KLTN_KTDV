package com.document.manager.repository;

import com.document.manager.domain.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepo extends JpaRepository<UserApp, Long> {

    UserApp findByEmail(String email);

    UserApp findByUserCode(String code);

    @Query(value = "SELECT COUNT(u.*) FROM user_app u WHERE u.id IN (SELECT ur.user_id FROM user_role ur " +
            "LEFT JOIN role r ON ur.role_id = r.id  WHERE r.name NOT LIKE 'ROLE_USER') AND u.is_active = :isActive", nativeQuery = true )
    long countUserByIsActive(boolean isActive);
}
