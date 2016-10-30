package com.yyl.client.notify;

import com.yyl.client.model.MailConf;
import com.yyl.client.utils.EmailUtil;

/**
 * Created by yl on 2016/10/9.
 */
public class TestEmail3 extends BaseSend {

    private static MailConf mailInfo;

    public TestEmail3(MailConf mailInfo) {
        TestEmail3.mailInfo = mailInfo;
    }

    @Override
    public void run() {
        sendMail();
    }


    private void sendMail() {
        try {

            EmailUtil.sendAttachment(mailInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
