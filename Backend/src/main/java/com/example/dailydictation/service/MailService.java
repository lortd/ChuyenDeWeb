package com.example.dailydictation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;


    public void sendVerificationEmail(String to, String token) {
        String subject = "Xác nhận tài khoản của bạn";
        String verificationUrl = "http://localhost:8080/auth/verify?token=" + token;
        String body = "Nhấn vào đây để xác nhận tài khoản: " + verificationUrl;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
       mailSender.send(message);
    }

}
