package com.document.manager.service.impl;

import com.document.manager.dto.MailRequest;
import com.document.manager.dto.MailResponse;
import com.document.manager.service.MailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private Configuration configuration;

    @Autowired
    private Environment env;

    public MailResponse sendMail(MailRequest request, String templateName, Map<String, Object> mapData) {
        logger.info("User mail: " + env.getProperty("spring.mail.username"));
        logger.info("Pass mail: " + env.getProperty("spring.mail.password"));
        MailResponse mailResponse = new MailResponse();
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
//            messageHelper.addAttachment("logo.png", new ClassPathResource("logo.png"));

            Template template = configuration.getTemplate(templateName);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mapData);

            messageHelper.setTo(request.getTo());
            messageHelper.setText(html, true);
            messageHelper.setSubject(request.getSubject());
            messageHelper.setFrom(request.getFrom());
            sender.send(messageHelper.getMimeMessage());

            logger.info("Mail sending to: " + request.getTo());
            mailResponse.setMessage("Mail sending to: " + request.getTo());
            mailResponse.setStatus(Boolean.TRUE);
        } catch (Exception e) {
            logger.info("Mail sending failed: " + e.getMessage());
            mailResponse.setMessage("Mail sending failed: " + e.getMessage());
            mailResponse.setStatus(Boolean.FALSE);
        }
        return mailResponse;
    }

    @Override
    public void sendMailRegister(String to, String name, Map<String, Object> mapData) {
        MailRequest mailRequest = new MailRequest(name, to, env.getProperty("spring.mail.username"), "Xác nhận đăng ký tài khoản");
        sendMail(mailRequest, "mail-template-register.ftl", mapData);
    }

    @Override
    public void sendMailForgotPassword(String to, String name, Map<String, Object> mapData) {
        MailRequest mailRequest = new MailRequest(name, to, env.getProperty("spring.mail.username"), "Quên mật khẩu");
        sendMail(mailRequest, "mail-template-forgot-password.ftl", mapData);
    }
}
