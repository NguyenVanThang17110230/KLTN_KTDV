package com.document.manager.service;

import com.document.manager.domain.DocumentApp;
import com.document.manager.dto.DocumentDTO;
import com.document.manager.dto.PlagiarismDocumentDTO;
import com.document.manager.dto.UploadDocumentDTO;
import javassist.NotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DocumentService {

    DocumentApp save(DocumentApp documentApp);

    PlagiarismDocumentDTO uploadDocument(UploadDocumentDTO uploadDocumentDTO) throws IOException;

    String[] divisionToSentences(String content) throws Exception;

    DocumentApp findById(Long id) throws NotFoundException;

    List<DocumentApp> findByUserId(Long userId);

    List<DocumentApp> findAll();

    PlagiarismDocumentDTO getPlagiarism(String[] target, Map<Integer, List<String>> tokenizerOfTarget);

    List<DocumentDTO> getDocumentOfCurrentUser() throws NotFoundException;
}
