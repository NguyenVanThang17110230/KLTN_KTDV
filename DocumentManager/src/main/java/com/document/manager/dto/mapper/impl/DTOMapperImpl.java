package com.document.manager.dto.mapper.impl;

import com.document.manager.domain.UserApp;
import com.document.manager.dto.SignUpDTO;
import com.document.manager.dto.mapper.DTOMapper;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Component;

import static com.document.manager.dto.enums.Gender.FEMALE;
import static com.document.manager.dto.enums.Gender.MALE;

@Component
public class DTOMapperImpl implements DTOMapper {

    @Override
    public UserApp toUser(SignUpDTO signUpDTO) {
        UserApp userApp = new UserApp();
        if (!GenericValidator.isBlankOrNull(signUpDTO.getUserCode())) {
            userApp.setUserCode(signUpDTO.getUserCode());
        }
        if (!GenericValidator.isBlankOrNull(signUpDTO.getFirstName())) {
            userApp.setFirstname(signUpDTO.getFirstName());
        }
        if (!GenericValidator.isBlankOrNull(signUpDTO.getLastName())) {
            userApp.setLastname(signUpDTO.getLastName());
        }
        if (!GenericValidator.isBlankOrNull(signUpDTO.getGender())) {
            userApp.setGender(signUpDTO.getGender().equalsIgnoreCase("Male") ? MALE : FEMALE);
        }
        if (signUpDTO.getDob() != null) {
            userApp.setDob(signUpDTO.getDob());
        }
        if (!GenericValidator.isBlankOrNull(signUpDTO.getPhoneNumber())) {
            userApp.setPhoneNumber(signUpDTO.getPhoneNumber());
        }
        if (!GenericValidator.isBlankOrNull(signUpDTO.getEmail())) {
            userApp.setEmail(signUpDTO.getEmail());
        }
        if (!GenericValidator.isBlankOrNull(signUpDTO.getPassword())) {
            userApp.setPassword(signUpDTO.getPassword());
        }
        return userApp;
    }
}
