package com.document.manager.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileService {

    String readFile(MultipartFile multipartFile);

    File convertFile(MultipartFile multipartFile);

    String saveFile(String dir, String fileName, byte[] bytes) throws IOException;

    String readContentDocument(File file) throws IOException;
}
