package com.example.BKS_Backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String token) {
        String url = "http://localhost:8080/api/auth/verify?token=" + token;
        System.out.println("\n\n=======================================================");
        System.out.println("Vui lòng click vào link sau để kích hoạt tài khoản: " + to);
        System.out.println(url);
        System.out.println("=======================================================\n\n");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Kích hoạt tài khoản Hust Social Network");
        message.setText("Click vào link sau để kích hoạt: " + url + "\n(Link có hiệu lực 15 phút)");
        mailSender.send(message);
    }
}
