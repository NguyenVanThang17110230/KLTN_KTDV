package com.document.manager.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class PlagiarismDocumentDTO implements Serializable {

    private Long documentId;
    private boolean status;
    private String message;
    private float rate;
    private List<PlagiarismSentencesDTO> plagiarism;
}
