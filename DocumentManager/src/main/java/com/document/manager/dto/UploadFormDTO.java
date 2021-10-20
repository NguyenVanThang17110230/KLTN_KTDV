package com.document.manager.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFormDTO {

    @NotBlank
    private String title;
    private String description;

    @Max(value = 10)
    @Min(value = 0)
    private BigDecimal mark;

    @NotNull
    private MultipartFile file;
}
