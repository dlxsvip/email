package com.yyl.client.model;

import com.yyl.client.utils.AESUtil;
import com.yyl.client.utils.FileUtil;
import com.yyl.client.utils.ReadUtil;
import org.apache.commons.mail.EmailAttachment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yl on 2016/10/20.
 */
public class MailConf {

    // 邮件配置文件
    public static final String CONFIG = "/conf/email.properties";

    // 收件人
    private List<String> toAddress = new ArrayList<String>();

    // 抄送人地址
    private List<String> ccAddress = new ArrayList<String>();

    // 密送人
    private List<String> bccAddress = new ArrayList<String>();

    // 附件信息
    private List<EmailAttachment> attachments = new ArrayList<EmailAttachment>();

    // 邮件主题
    private String subject;

    // 邮件的文本内容
    private String content;

    // vm 模板参数
    private Map<String, String> vm;

    /**
     * 发送邮箱服务器
     */
    private String smtpServer;

    /**
     * 发送人昵称
     */
    private String senderNick;

    /**
     * 发送人邮箱
     */
    private String senderAddress;

    /**
     * 发送人密码
     */
    private String senderPassword;

    /**
     * 发送人密码是否加密
     */
    private boolean isAES = true;

    private boolean debugger;

    // 追加接收人
    public void addTo(String toAddress) {
        this.toAddress.add(toAddress);
    }

    // 追加接收人列表
    public void addTo(List<String> toAddress) {
        this.toAddress.addAll(toAddress);
    }

    // 追加抄送人列表
    public void addCc(List<String> ccAddress) {
        if (null != ccAddress && ccAddress.size() > 0)
            this.ccAddress.addAll(ccAddress);
    }

    // 追加附件
    public void addAtt(EmailAttachment att) {
        this.attachments.add(att);
    }

    // 追加附件列表
    public void addAtt(List<EmailAttachment> att) {
        if (null != att && att.size() > 0) {
            this.attachments.addAll(att);
        }

    }

    public void addAtt(String filePath) {
        // 创建附件
        EmailAttachment att = new EmailAttachment();
        att.setPath(filePath);                         //本地附件，绝对路径
        att.setName(FileUtil.getFileName(filePath));   //附件名称
        att.setDisposition(EmailAttachment.ATTACHMENT);
        //att.setDescription();                        //附件描述

        addAtt(att);
    }


    public void readConf() {
        try {
            String confPath = "";
            if (debugger) {
                String workPath = System.getProperty("user.dir");
                confPath = workPath + "/build" + CONFIG;
            } else {
                String path = FileUtil.getJarParentPath();
                path = FileUtil.getParentPath(path);
                confPath = path + CONFIG;
            }
            confPath = FileUtil.switchPath(confPath);
            File ftpCfg = new File(confPath);
            Map<String, String> map = ReadUtil.read(ftpCfg, "UTF-8");

            setAES(Boolean.valueOf(map.get("isAES")));
            setSmtpServer(map.get("smtpServer"));
            setSenderAddress(map.get("emailName"));
            setSenderNick(map.get("nickName"));
            setSenderPassword(map.get("emailPassword"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getToAddress() {
        return toAddress;
    }

    public void setToAddress(List<String> toAddress) {
        this.toAddress = toAddress;
    }

    public List<String> getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(List<String> ccAddress) {
        this.ccAddress = ccAddress;
    }

    public List<String> getBccAddress() {
        return bccAddress;
    }

    public void setBccAddress(List<String> bccAddress) {
        this.bccAddress = bccAddress;
    }

    public List<EmailAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<EmailAttachment> attachments) {
        this.attachments = attachments;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getSenderNick() {
        return senderNick;
    }

    public void setSenderNick(String senderNick) {
        this.senderNick = senderNick;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getSenderPassword() {
        return senderPassword;
    }

    public void setSenderPassword(String senderPassword) {
        if (isAES) {
            this.senderPassword = AESUtil.getInstance().decrypt(senderPassword);
        } else {
            this.senderPassword = senderPassword;
        }

    }

    public boolean isAES() {
        return isAES;
    }

    public void setAES(boolean isAES) {
        this.isAES = isAES;
    }

    public Map<String, String> getVm() {
        return vm;
    }

    public void setVm(Map<String, String> vm) {
        this.vm = vm;
    }

    public boolean isDebugger() {
        return debugger;
    }

    public void setDebugger(boolean debugger) {
        this.debugger = debugger;
    }
}
