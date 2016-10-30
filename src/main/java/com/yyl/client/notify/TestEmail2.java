package com.yyl.client.notify;

import com.yyl.client.model.MailConf;
import com.yyl.client.utils.DateUtil;
import com.yyl.client.utils.EmailUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Date;

/**
 * Created by yl on 2016/10/9.
 */
public class TestEmail2 extends BaseSend {

    // vm模板
    private static final String TEMPLATE = "email2.vm";

    private static MailConf mailInfo;



    public TestEmail2(MailConf mailInfo) {
        TestEmail2.mailInfo = mailInfo;
    }

    @Override
    public void run() {
        sendMail();
    }


    private void sendMail() {
        Date currentTime = new Date();
        try {

            // 实例化一个vm
            VelocityEngine ve = initVelocity();

            // 设置邮件主题
            mailInfo.setSubject(mailInfo.getVm().get("registerTitle"));

            // 填充vm模板
            VelocityContext context = new VelocityContext();
            context.put("email", mailInfo.getToAddress());
            //context.put("password", mailInfo.getVm().get("userInitPwd"));
            context.put("welcomeUrl", mailInfo.getVm().get("welcomeUrl"));

            context.put("company", mailInfo.getVm().get("company"));
            context.put("date", DateUtil.date2str(currentTime, "yyyy年MM月dd日"));

            StringWriter writer = new StringWriter();
            ve.mergeTemplate(TEMPLATE, "utf-8", context, writer);


            // 邮件正文
            mailInfo.setContent(writer.toString());

            EmailUtil.sendHtmlMail(mailInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
