package com.document.manager.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IndexPart implements Serializable {

    private Integer startTarget;
    private Integer endTarget;
    private Integer startMatching;
    private Integer endMatching;
}
