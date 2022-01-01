package com.document.manager.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class UploadDocumentDTO implements Serializable {

    private String title;
    private String note;
    private Double mark;
    private MultipartFile multipartFile;
}
