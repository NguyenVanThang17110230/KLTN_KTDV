package com.document.manager.repository;

import com.document.manager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByUserCode(String code);
}
