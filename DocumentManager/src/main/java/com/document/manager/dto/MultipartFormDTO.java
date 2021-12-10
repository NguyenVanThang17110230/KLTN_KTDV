package com.document.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MultipartFormDTO implements Serializable {

    private String fileName;

    private String contentType;

    private byte[] content;
}
