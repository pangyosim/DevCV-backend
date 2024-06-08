package com.devcv.member.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Service
@NoArgsConstructor(force = true)
public class MailService {

    @Autowired
    private final JavaMailSender javamailSender;
    private static final String senderEmail = "oy1666919@gmail.com";
    private static int number;

    public static void createNumber() {
        number = (int)(Math.random() * (90000)) + 100000;
    }

    public MimeMessage CreateMail(String mail) throws UnsupportedEncodingException{
        createNumber();
        MimeMessage message = javamailSender.createMimeMessage();
        try {
            message.setFrom(new InternetAddress(senderEmail, "DevCV"));
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다" + "</h3>";
            body += "<h1>" + number + "</h1>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.fillInStackTrace();
        }

        return message;

    }

    public int sendMail(String mail) throws UnsupportedEncodingException {

        MimeMessage message = CreateMail(mail);

        Objects.requireNonNull(javamailSender).send(message);

        return number;
    }
}
