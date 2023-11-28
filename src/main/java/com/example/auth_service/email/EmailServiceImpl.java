package com.example.auth_service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Async
    @Override
    public void sendEmail(String subject, String content, String emailTo) {
        send(subject, content, emailTo, false);
    }

    @Async
    @Override
    public void sendEmail(String subject, String content, String emailTo, boolean isHtmlFormat, String attachmentName, Path attachmentPath) {
        send(subject, content, emailTo, isHtmlFormat, attachmentName, attachmentPath);
    }

    @Async
    @Override
    public void sendEmail(String subject, String content, String emailTo, String attachmentName, Path attachmentPath) {
        send(subject, content, emailTo, false, attachmentName, attachmentPath);
    }

    @Async
    @Override
    public void sendEmail(String subject, String content, String emailTo, boolean isHtmlFormat) {
        send(subject, content, emailTo, isHtmlFormat);
    }

    private void send(String subject, String content, String emailTo, boolean isHtmlFormat) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");

            message.setSubject(subject);
            message.setText(content, isHtmlFormat);
            message.setTo(emailTo);

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email to " + emailTo);
        }
    }

    private void send(String subject, String content, String emailTo, boolean isHtmlFormat, String attachmentName, Path attachmentPath) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);

            message.setTo(emailTo);
            message.setSubject(subject);
            message.setText(content, isHtmlFormat);

            FileSystemResource file = new FileSystemResource(attachmentPath);
            message.addAttachment(attachmentName, file);

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email to " + emailTo);
        }
    }
}
