package com.document.manager.dto.mapper.impl;

import com.document.manager.domain.DocumentApp;
import com.document.manager.domain.UserApp;
import com.document.manager.dto.DocumentDTO;
import com.document.manager.dto.SignUpDTO;
import com.document.manager.dto.UserAppDTO;
import com.document.manager.dto.constants.Constants;
import com.document.manager.dto.mapper.DTOMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.document.manager.dto.enums.Gender.FEMALE;
import static com.document.manager.dto.enums.Gender.MALE;

@Component
public class DTOMapperImpl implements DTOMapper {

    @Override
    public UserApp toUser(SignUpDTO signUpDTO) {
        UserApp userApp = new UserApp();
        if (!StringUtils.isEmpty(signUpDTO.getUserCode())) {
            userApp.setUserCode(signUpDTO.getUserCode());
        }
        if (!StringUtils.isEmpty(signUpDTO.getFirstName())) {
            userApp.setFirstName(signUpDTO.getFirstName());
        }
        if (!StringUtils.isEmpty(signUpDTO.getLastName())) {
            userApp.setLastName(signUpDTO.getLastName());
        }
        if (!StringUtils.isEmpty(signUpDTO.getGender())) {
            userApp.setGender(signUpDTO.getGender().equalsIgnoreCase("Male") ? MALE : FEMALE);
        }
        if (signUpDTO.getDob() != null) {
            userApp.setDob(signUpDTO.getDob());
        }
        if (!StringUtils.isEmpty(signUpDTO.getPhoneNumber())) {
            userApp.setPhoneNumber(signUpDTO.getPhoneNumber());
        }
        if (!StringUtils.isEmpty(signUpDTO.getEmail())) {
            userApp.setEmail(signUpDTO.getEmail());
        }
        if (!StringUtils.isEmpty(signUpDTO.getPassword())) {
            userApp.setPassword(signUpDTO.getPassword());
        }
        return userApp;
    }

    @Override
    public UserAppDTO toUserAppDTO(UserApp userApp) throws IOException {
        UserAppDTO userAppDTO = new UserAppDTO();
        if (userApp.getId() != null) {
            userAppDTO.setId(userApp.getId());
        }
        if (!StringUtils.isEmpty(userApp.getUserCode())) {
            userAppDTO.setUserCode(userApp.getUserCode());
        }
        if (!StringUtils.isEmpty(userApp.getFirstName())) {
            userAppDTO.setFirstName(userApp.getFirstName());
        }
        if (!StringUtils.isEmpty(userApp.getLastName())) {
            userAppDTO.setLastName(userApp.getLastName());
        }
        if (userApp.getGender() != null) {
            userAppDTO.setGender(userApp.getGender().toString());
        }
        if (userApp.getDob() != null) {
            userAppDTO.setDob(userApp.getDob());
        }
        if (!StringUtils.isEmpty(userApp.getPhoneNumber())) {
            userAppDTO.setPhoneNumber(userApp.getPhoneNumber());
        }
        if (!StringUtils.isEmpty(userApp.getEmail())) {
            userAppDTO.setEmail(userApp.getEmail());
        }
        if (userApp.getIsActive() != null) {
            userAppDTO.setIsActive(userApp.getIsActive());
        }
        if (userApp.getCreatedStamp() != null) {
            userAppDTO.setCreatedStamp(userApp.getCreatedStamp());
        }
        if (userApp.getModifiedStamp() != null) {
            userAppDTO.setModifiedStamp(userApp.getModifiedStamp());
        }
        if (userApp.getRoleApps() != null && userApp.getRoleApps().size() > 0) {
            userAppDTO.setRoleApps(userAppDTO.getRoleApps());
        }
        if (!StringUtils.isEmpty(userApp.getAvatar())) {
            File image = new File(Constants.DIR_UPLOADED_USER + userApp.getAvatar());
            byte[] fileContent = IOUtils.toByteArray(new FileInputStream(image));
            MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
            String mimeType = fileTypeMap.getContentType(image.getName());
            String contentOfImage = Base64.getEncoder().encodeToString(fileContent);
            String base64 = "data:" + mimeType + ";base64," + contentOfImage;
            userAppDTO.setAvatar(base64);
        }
        if (userApp.getRoleApps() != null && userApp.getRoleApps().size() > 0) {
            userAppDTO.setRoleApps(userApp.getRoleApps());
        }
        return userAppDTO;
    }

    @Override
    public DocumentDTO toDocumentDTO(DocumentApp documentApp) {
        DocumentDTO documentDTO = new DocumentDTO();
        if (documentApp != null) {
            if (documentApp.getId() != null) {
                documentDTO.setDocumentId(documentApp.getId());
            }
            if (StringUtils.isNotEmpty(documentApp.getTitle())) {
                documentDTO.setTitle(documentApp.getTitle());
            }
            if (StringUtils.isNotEmpty(documentApp.getNote())) {
                documentDTO.setNote(documentApp.getNote());
            }
            if (documentApp.getMark() != null) {
                documentDTO.setMark(documentApp.getMark());
            }
            if (documentApp.getCreatedStamp() != null) {
                documentDTO.setCreatedStamp(documentApp.getCreatedStamp());
            }
        }
        return documentDTO;
    }

    @Override
    public List<DocumentDTO> toDocumentDTO(List<DocumentApp> documentApps) {
        List<DocumentDTO> documentDTOS = new ArrayList<>();
        if (documentApps != null && documentApps.size() > 0) {
            documentApps.forEach(d -> documentDTOS.add(this.toDocumentDTO(d)));
        }
        return documentDTOS;
    }
}
