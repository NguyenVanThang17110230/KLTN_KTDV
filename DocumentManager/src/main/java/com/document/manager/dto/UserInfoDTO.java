package com.document.manager.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO implements Serializable {

    private String firstName;
    private String lastName;
    private String gender;
    private String phoneNumber;
    private String userCode;
}
