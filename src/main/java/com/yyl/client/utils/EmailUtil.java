package com.yyl.client.utils;

import com.yyl.client.model.MailConf;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

/**
 * Created by yl on 2016/10/9.
 */
public class EmailUtil {

    private static String charset = "UTF-8";

    /**
     * 发送简单邮件
     *
     * @param mailInfo 邮件参数
     * @throws Exception
     */
    public static void sendSimpleMail(MailConf mailInfo) throws Exception {
        SimpleEmail email = new SimpleEmail();

        // 发送邮箱的邮件服务器
        email.setHostName(mailInfo.getSmtpServer());
        // 登陆邮件服务器的用户名和密码
        email.setAuthentication(mailInfo.getSenderAddress(), mailInfo.getSenderPassword());
        email.setSSLOnConnect(true);

        //设置接收人地址
        to(mailInfo, email);
        //抄送方
        cc(mailInfo, email);
        //秘密抄送方
        bcc(mailInfo, email);

        //设置发送人信息
        email.setFrom(mailInfo.getSenderAddress(), mailInfo.getSenderNick());
        //设置标题
        email.setSubject(mailInfo.getSubject());
        //填写邮件内容
        email.setMsg(mailInfo.getContent());

        email.send();

        System.out.println("发送成功:" + mailInfo.getToAddress());
    }

    /**
     * 发送 html 模板邮件
     *
     * @param mailInfo 邮件参数
     * @throws Exception
     */
    public static void sendHtmlMail(MailConf mailInfo) throws Exception {

        JavaMailSenderImpl email = new JavaMailSenderImpl();
        // 发送邮箱的邮件服务器
        email.setHost(mailInfo.getSmtpServer());
        // 建立邮件消息,发送简单邮件和html邮件的区别
        MimeMessage mailMessage = email.createMimeMessage();

        // 为防止乱码，添加编码集设置
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, "UTF-8");
        // 发送方邮箱
        messageHelper.setFrom(mailInfo.getSenderAddress(), mailInfo.getSenderNick());
        // 接收方邮箱
        to(mailInfo, messageHelper);

        // 设置标题
        messageHelper.setSubject(mailInfo.getSubject());
        // true 表示启动HTML格式的邮件
        messageHelper.setText(mailInfo.getContent(), true);


        Properties prop = new Properties();
        // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        prop.put("mail.smtp.auth", "true");
        // 超时时间
        prop.put("mail.smtp.timeout", "25000");

        // 添加验证
        Session session = Session.getDefaultInstance(prop, new MyAuthenticator(mailInfo.getSenderAddress(), mailInfo.getSenderPassword()));
        email.setSession(session);

        // 发送邮件
        email.send(mailMessage);
        System.out.println("发送成功:" + mailInfo.getToAddress());
    }

    /**
     * 发送附件
     *
     * @param mailInfo 邮件参数
     * @throws Exception
     */
    public static void sendAttachment(MailConf mailInfo) throws Exception {
        //创建能加附件内容为HTML文本的邮件、HTML直接内联图片但必须用setHtmlMsg()传邮件内容
        HtmlEmail email = new HtmlEmail();

        email.setHostName(mailInfo.getSmtpServer());
        // 登陆邮件服务器的用户名和密码
        email.setAuthentication(mailInfo.getSenderAddress(), mailInfo.getSenderPassword());
        email.setSSLOnConnect(true);      //开启SSL加密
        email.setStartTLSEnabled(true);   //开启TLS加密
        email.setCharset(charset);

        //设置发送人信息
        email.setFrom(mailInfo.getSenderAddress(), mailInfo.getSenderNick());
        //设置标题
        email.setSubject(mailInfo.getSubject());
        //填写邮件内容
        email.setHtmlMsg("<font color='red'>测试附件邮件发送功能！</font>");


        att(mailInfo, email);


        //设置接收人地址
        to(mailInfo, email);
        //抄送方
        cc(mailInfo, email);
        //秘密抄送方
        bcc(mailInfo, email);


        email.send();//发送邮件
        System.out.println("发送成功:" + mailInfo.getToAddress());
    }


    // 收件人
    private static void to(MailConf mailInfo, Email email) throws Exception {
        List<String> toAddress = mailInfo.getToAddress();
        if (null != toAddress && toAddress.size() > 0) {
            for (int i = 0; i < toAddress.size(); i++) {
                email.addTo(toAddress.get(i));
            }
        }
    }

    //抄送方
    private static void cc(MailConf mailInfo, Email email) throws Exception {
        List<String> ccList = mailInfo.getCcAddress();
        for (String cc : ccList) {
            email.addTo(cc);
        }
    }

    //秘密抄送方
    private static void bcc(MailConf mailInfo, Email email) throws Exception {
        List<String> bccList = mailInfo.getBccAddress();
        for (String bcc : bccList) {
            email.addTo(bcc);
        }
    }

    // 收件人
    private static void to(MailConf mailInfo, MimeMessageHelper messageHelper) throws Exception {
        List<String> toAddress = mailInfo.getToAddress();
        if (null != toAddress && toAddress.size() > 0) {
            for (int i = 0; i < toAddress.size(); i++) {
                messageHelper.setTo(toAddress.get(i));
            }
        }
    }


    // 添加附件
    private static void att(MailConf mailInfo, HtmlEmail email) throws Exception {
        List<EmailAttachment> atts = mailInfo.getAttachments();
        for (EmailAttachment att : atts) {
            email.attach(att);
        }
    }


    public static class MyAuthenticator extends Authenticator {
        String userName = null;
        String password = null;


        public MyAuthenticator(String username, String password) {
            this.userName = username;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userName, password);
        }
    }


}
