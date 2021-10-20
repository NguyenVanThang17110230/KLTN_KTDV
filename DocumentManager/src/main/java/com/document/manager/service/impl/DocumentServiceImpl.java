package com.document.manager.service.impl;

import com.document.manager.service.DocumentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    @Override
    public String readFile(MultipartFile multipartFile) {
        try {
            byte[] b = multipartFile.getBytes();
            InputStream inputStream = multipartFile.getInputStream();

            String result = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));
//            String result = new String(b);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public File convertFile(MultipartFile multipartFile) {
        File convFile = new File(multipartFile.getOriginalFilename());
        try {
            convFile.createNewFile();
            try (InputStream is = multipartFile.getInputStream()) {
                Files.copy(is, convFile.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convFile;
    }
}
