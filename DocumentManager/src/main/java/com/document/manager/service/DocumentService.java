package com.document.manager.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface DocumentService {

    String readFile(MultipartFile multipartFile);

    File convertFile(MultipartFile multipartFile);

    void saveFile(MultipartFile multipartFile);
}
