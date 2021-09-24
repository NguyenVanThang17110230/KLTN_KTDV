package com.document.manager.dto.mapper.impl;

import com.document.manager.domain.User;
import com.document.manager.dto.SignUpDTO;
import com.document.manager.dto.mapper.DTOMapper;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Component;

import static com.document.manager.dto.enums.Gender.FEMALE;
import static com.document.manager.dto.enums.Gender.MALE;

@Component
public class DTOMapperImpl implements DTOMapper {

    @Override
    public User toUser(SignUpDTO signUpDTO) {
        User user = new User();
        if (!GenericValidator.isBlankOrNull(signUpDTO.getUserCode())) {
            user.setUserCode(signUpDTO.getUserCode());
        }
        if (!GenericValidator.isBlankOrNull(signUpDTO.getFirstName())) {
            user.setFirstname(signUpDTO.getFirstName());
        }
        if (!GenericValidator.isBlankOrNull(signUpDTO.getLastName())) {
            user.setLastname(signUpDTO.getLastName());
        }
        if (!GenericValidator.isBlankOrNull(signUpDTO.getGender())) {
            user.setGender(signUpDTO.getGender().equalsIgnoreCase("Male") ? MALE : FEMALE);
        }
        if (signUpDTO.getDob() != null) {
            user.setDob(signUpDTO.getDob());
        }
        if (!GenericValidator.isBlankOrNull(signUpDTO.getPhoneNumber())) {
            user.setPhoneNumber(signUpDTO.getPhoneNumber());
        }
        if (!GenericValidator.isBlankOrNull(signUpDTO.getEmail())) {
            user.setEmail(signUpDTO.getEmail());
        }
        if (!GenericValidator.isBlankOrNull(signUpDTO.getPassword())) {
            user.setPassword(signUpDTO.getPassword());
        }
        return user;
    }
}
