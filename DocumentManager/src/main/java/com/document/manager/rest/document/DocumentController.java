package com.document.manager.rest.document;

import com.document.manager.dto.ResponseData;
import com.document.manager.service.DocumentService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static com.document.manager.dto.constants.Constants.UPLOADED_FOLDER;

@Controller
@RequestMapping(value = "/api/document")
@AllArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    private final ServletContext context;

    private final ResourceLoader resourceLoader;

    @PostMapping(value = "/files")
    public ResponseEntity<ResponseData> uploadFile(@RequestParam(value = "file") MultipartFile file) throws IOException {
        try
        {
            System.out.println(new String (file.getBytes(), "UTF-8"));
            System.out.println(documentService.readFile(file));
           documentService.saveFile(file);
           File newFile = new File(UPLOADED_FOLDER + file.getOriginalFilename());
            PDDocument document = PDDocument.load(newFile);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            System.out.println(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(ResponseData.builder().message("Success").build(), HttpStatus.OK);
    }

//    @PostMapping(value = "/file")
//    public ResponseEntity<ResponseData> upload(@RequestBody File file) throws IOException {
//        System.out.println(file);
//
//        return new ResponseEntity<>(ResponseData.builder().message("Success").build(), HttpStatus.OK);
//    }

//    @PostMapping(value = "/upload")
//    public ResponseEntity<ResponseData> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
//        if (uploadFormDTO == null) {
//            return new ResponseEntity<>(ResponseData.builder()
//                    .status(ERROR.toString())
//                    .message("Data is empty").build(), BAD_REQUEST);
//        }
//        MultipartFile multipartFile = uploadFormDTO.getFile();

//        Resource resource = resourceLoader.getResource("classpath:/");
//        URI uri = resource.getURI();
//
//        File file = new File(uri + File.pathSeparator + multipartFile.getOriginalFilename());
//        multipartFile.transferTo(file);
//        PDDocument doc = PDDocument.load(file);
//
//        String content = new PDFTextStripper().getText(doc);
//
//        return new ResponseEntity<>(ResponseData.builder()
//                .status(SUCCESS.toString())
//                .message(content).build(), OK);
//    }
}
