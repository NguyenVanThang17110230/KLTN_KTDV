package com.document.manager.service.impl;

import com.document.manager.dto.MailResponse;
import com.document.manager.dto.MailRequest;

import javax.mail.MessagingException;
import java.util.Map;

public interface MailService {

    //MailResponse sendMail(MailRequest request, String templateName, Map<String, Object> mapData) throws MessagingException;

    void sendMailRegister(String to, String name, Map<String, Object> mapData);

    void sendMailForgotPassword(String to, String name, Map<String, Object> mapData);

}
