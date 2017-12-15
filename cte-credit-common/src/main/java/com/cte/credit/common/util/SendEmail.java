package com.cte.credit.common.util;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cte.credit.common.template.PropertyUtil;

import java.util.Date;
import java.util.Properties;

/**
 * 邮件发送
 * */
@Service
public class SendEmail {
	@Autowired
	PropertyUtil propertyEngine;
    // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
    // 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
    public static String myEmailSMTPHost = "smtp.163.com";
    /**
     * 发送邮件
     *  @param receiveMails 收件人邮箱,以,分隔
     * @param content 发送内容
     * @param title 邮件主题
     * @return
     * @throws Exception
     */
    public  void sendEmails(String receiveMails,
    		String content,String title) 
    		throws Exception {
    	String sendId = propertyEngine.readById("sys_public_email_sendId");
		String sendPwd = propertyEngine.readById("sys_public_email_sendPwd");
        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证

        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);                                 // 设置为debug模式, 可以查看详细的发送 log

        // 3. 创建一封邮件
        MimeMessage message = createMimeMessage(session, 
        		sendId, receiveMails,title,content);

        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();
        transport.connect(sendId, sendPwd);

        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());

        // 7. 关闭连接
        transport.close();
    }

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session 和服务器交互的会话
     * @param sendMail 发件人邮箱
     * @param receiveMail 收件人邮箱,以,分隔
     * @return
     * @throws Exception
     */
    public static MimeMessage createMimeMessage(Session session,
    		String sendMail, String receiveMails,
    		String title,String content) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
        message.setFrom(new InternetAddress(sendMail, "CTE科技", "UTF-8"));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        for(String receiveMail:receiveMails.split(",")){
        	message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "CTE人员", "UTF-8"));
        }
        // 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
        message.setSubject(title, "UTF-8");

        // 5. Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发送内容）
        message.setContent(content, "text/html;charset=UTF-8");

        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
        message.saveChanges();

        return message;
    }
}
