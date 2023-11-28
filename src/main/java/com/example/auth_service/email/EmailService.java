package com.example.auth_service.email;

import java.nio.file.Path;

public interface EmailService {
    void sendEmail(String subject, String content, String emailTo, boolean isHtmlFormat);

    void sendEmail(String subject, String content, String emailTo);

    void sendEmail(String subject, String content, String emailTo, boolean isHtmlFormat, String attachmentName, Path attachmentPath);

    void sendEmail(String subject, String content, String emailTo, String attachmentName, Path attachmentPath);
}