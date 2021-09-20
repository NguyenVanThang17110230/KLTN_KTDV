package com.document.manager.util;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseData {
    private String status;
    private String message;
    private Object data;
}
