package com.document.manager.repository;

import com.document.manager.domain.DocumentApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepo extends JpaRepository<DocumentApp, Long> {

    @Query(value = "select d.* from document d where d.created_id =:userId", nativeQuery = true)
    List<DocumentApp> findByUserId(Long userId);
}
