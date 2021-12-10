package com.document.manager.service.impl;

import com.document.manager.service.DocumentService;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static com.document.manager.dto.constants.Constants.UPLOADED_FOLDER;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    @Override
    public String readFile(MultipartFile multipartFile) {
        try {
            byte[] b = multipartFile.getBytes();
            InputStream inputStream = multipartFile.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String result;
            while (( result = bufferedReader.readLine()) != null) {
                System.out.println(result);
            }
            String x = new String(b);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public File convertFile(MultipartFile multipartFile) {
        File convFile = new File( "C:\\Users\\nguye\\Desktop\\KLTN\\KLTN_KTDV\\DocumentManager\\src\\main\\resources\\uploads"
                + multipartFile.getOriginalFilename());
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

    @Override
    public void saveFile(MultipartFile multipartFile) {
        try {
            byte[] bytes = multipartFile.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + multipartFile.getOriginalFilename());
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
