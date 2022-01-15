package com.document.manager.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StateDTO implements Serializable {

    private Integer start;
    private Integer length;
}
