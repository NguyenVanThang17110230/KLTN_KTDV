package com.document.manager.repository;

import com.document.manager.domain.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserApp, Long> {

    UserApp findByEmail(String email);

    UserApp findByUserCode(String code);
}
