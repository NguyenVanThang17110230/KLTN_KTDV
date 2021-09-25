package com.document.manager.dto.mapper;

import com.document.manager.domain.UserApp;
import com.document.manager.dto.SignUpDTO;

public interface DTOMapper {

    UserApp toUser(SignUpDTO signUpDTO);
}
