package com.document.manager.rest.document;

import com.document.manager.dto.ResponseData;
import com.document.manager.dto.UploadDocumentDTO;
import com.document.manager.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.document.manager.dto.enums.ResponseDataStatus.ERROR;
import static com.document.manager.dto.enums.ResponseDataStatus.SUCCESS;

@Controller
@RequestMapping(value = "/api/document")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(value = "/upload")
    public ResponseEntity<ResponseData> uploadDocument(UploadDocumentDTO uploadDocumentDTO) {
        try {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(SUCCESS.name())
                    .message("Upload report successful")
                    .data(documentService.uploadDocument(uploadDocumentDTO))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseData.builder()
                    .status(ERROR.name()).message(e.getMessage())
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }
}
