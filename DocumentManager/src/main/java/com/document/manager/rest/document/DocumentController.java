package com.document.manager.rest.document;

import com.document.manager.dto.ResponseData;
import com.document.manager.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import static com.document.manager.dto.enums.ResponseDataStatus.SUCCESS;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/api/document")
@AllArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    private final ServletContext context;

    private final ResourceLoader resourceLoader;

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
