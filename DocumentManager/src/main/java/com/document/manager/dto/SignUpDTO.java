package com.document.manager.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO implements Serializable {

    private String userCode;
    private String firstName;
    private String lastName;
    private String gender;
    private Date dob;
    private String phoneNumber;
    private String email;
    private String  username;
    private String password;
}
