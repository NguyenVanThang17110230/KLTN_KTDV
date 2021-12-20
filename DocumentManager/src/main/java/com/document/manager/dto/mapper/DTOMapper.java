package com.document.manager.dto.mapper;

import com.document.manager.domain.UserApp;
import com.document.manager.dto.SignUpDTO;
import com.document.manager.dto.UserAppDTO;

public interface DTOMapper {

    UserApp toUser(SignUpDTO signUpDTO);

    UserAppDTO toUserAppDTO(UserApp userApp);
}
