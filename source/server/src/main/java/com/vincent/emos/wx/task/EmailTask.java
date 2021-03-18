package com.vincent.emos.wx.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.Serializable;


/**
 *
 * 邮件实体类
 *
 */
@Component
@Scope("prototype")
public class EmailTask implements Serializable {
    @Autowired
    private JavaMailSender javaMailSender;

    // 系统管理员邮箱
    @Value("${emos.mail.system}")
    private String mailbox;

    @Async
    public void sendAsync(SimpleMailMessage mailMessage){
        mailMessage.setFrom(mailbox);
        javaMailSender.send(mailMessage);
    }
}
