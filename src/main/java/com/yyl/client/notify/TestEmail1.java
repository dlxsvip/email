package com.yyl.client.notify;

import com.yyl.client.model.MailConf;
import com.yyl.client.utils.DateUtil;
import com.yyl.client.utils.EmailUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yl on 2016/10/9.
 */
public class TestEmail1 extends BaseSend {

    // vm模板
    private static final String TEMPLATE = "email1.vm";

    private static MailConf mailInfo;


    public TestEmail1(MailConf mailInfo) {
        TestEmail1.mailInfo = mailInfo;
    }

    @Override
    public void run() {
        sendMail();
    }


    private void sendMail() {
        Date currentTime = new Date();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);
        try {

            // 设置邮件主题
            mailInfo.setSubject(mailInfo.getVm().get("cashPoolTitle"));

            // 实例化一个vm
            VelocityEngine ve = initVelocity();

            // 填充vm模板
            VelocityContext context = new VelocityContext();

            context.put("accurateDate", DateUtil.date2str(currentTime, DateUtil.TMP_1));
            context.put("email", mailInfo.getToAddress());
            context.put("balance", currencyFormat.format(new BigDecimal(mailInfo.getVm().get("balance"))));
            context.put("threshHold", currencyFormat.format(new BigDecimal(mailInfo.getVm().get("threshHold"))));

            context.put("date", DateUtil.date2str(currentTime, "yyyy年MM月dd日"));
            context.put("company", mailInfo.getVm().get("company"));

            StringWriter writer = new StringWriter();

            ve.mergeTemplate(TEMPLATE, "utf-8", context, writer);

            // 邮件正文
            mailInfo.setContent(writer.toString());

            EmailUtil.sendSimpleMail(mailInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
