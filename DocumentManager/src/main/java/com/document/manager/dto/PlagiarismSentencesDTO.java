package com.document.manager.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class PlagiarismSentencesDTO implements Serializable {

    private String target;
    private String matching;
    private float rate;
    private List<IndexDTO> tokenizerPlagiarism;
    private List<IndexPart> targetPlagiarism;
    private List<IndexPart> matchingPlagiarism;
}
