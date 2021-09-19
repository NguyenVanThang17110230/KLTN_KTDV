package com.document.manager.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO implements Serializable {

    private String userName;
    private String password;
}
