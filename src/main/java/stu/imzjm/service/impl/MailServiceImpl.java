package stu.imzjm.service.impl;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import stu.imzjm.service.MailService;

@Service
public class MailServiceImpl implements MailService {
    @Resource
    private JavaMailSenderImpl javaMailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    //发送简单邮件
    public void sendSimpleEmail(String mailto, String title, String content) {
        if (mailto == null)
            mailto = mailFrom;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(mailto);
        message.setSubject(title);
        message.setText(content);

        //发送
        javaMailSender.send(message);
    }
}
