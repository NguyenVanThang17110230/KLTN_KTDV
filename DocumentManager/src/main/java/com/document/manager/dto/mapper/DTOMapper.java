package com.document.manager.dto.mapper;

import com.document.manager.domain.User;
import com.document.manager.dto.SignUpDTO;

public interface DTOMapper {

    User toUser(SignUpDTO signUpDTO);
}
