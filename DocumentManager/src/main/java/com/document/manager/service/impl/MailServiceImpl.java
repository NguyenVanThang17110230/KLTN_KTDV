package com.document.manager.service.impl;

import com.document.manager.dto.MailResponse;
import com.document.manager.dto.MailRequest;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private Configuration configuration;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public MailResponse sendMail(MailRequest request, Map<String, Object> mapData) {
        MailResponse mailResponse = new MailResponse();
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            messageHelper.addAttachment("logo.png", new ClassPathResource("logo.png"));

            Template template = configuration.getTemplate("email-template.ftl");
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
}
