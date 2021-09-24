package com.document.manager.repository;

import com.document.manager.domain.UserReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserReferenceRepo extends JpaRepository<UserReference, Long> {

    UserReference findByUuid(String uuid);

    List<UserReference> findByEmail(String email);
}
