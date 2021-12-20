package com.document.manager.service.impl;

import com.document.manager.service.FileService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;


@Service
public class FileServiceImpl implements FileService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String readFile(MultipartFile multipartFile) {
        try {
            byte[] b = multipartFile.getBytes();
            InputStream inputStream = multipartFile.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String result;
            while ((result = bufferedReader.readLine()) != null) {
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
        File convFile = new File("C:\\Users\\nguye\\Desktop\\KLTN\\KLTN_KTDV\\DocumentManager\\src\\main\\resources\\uploads"
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
    public String saveFile(String dir, String fileName, byte[] bytes) throws IOException {
        try {
            Date date = new Date();
            String link = dir + date.getTime() + "_" + fileName;
            Path path = Paths.get(link);
            File file = new File(dir);
            if (!file.exists()) {
                file.mkdirs();
            }
            Path des = Files.write(path, bytes);
            if (des == null) {
                throw new IOException("Save file failed");
            }
            return link;
        } catch (IOException e) {
            logger.info("Save file {} to location {} failed", fileName, dir);
            throw new IOException("Save file failed");
        }
    }

    @Override
    public String readContentDocument(File file) throws IOException {
        PDDocument document = null;
        try {
            document = PDDocument.load(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (Exception e) {
            throw new IOException("Read report failed");
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
}
