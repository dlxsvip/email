package com.yyl.client;

import com.yyl.client.job.JobWorker;
import com.yyl.client.model.MailConf;
import com.yyl.client.notify.TestEmail1;
import com.yyl.client.notify.TestEmail2;
import com.yyl.client.notify.TestEmail3;
import com.yyl.client.utils.AESUtil;
import com.yyl.client.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by yl on 2016/10/9.
 */
public class RunMail {

    /**
     * 本地调试开关
     */
    private static boolean localDebugger = true;

    private static MailConf conf = new MailConf();

    private static String type;

    private static String txt;

    public static void main(String[] args) {

        if (localDebugger) {
            //args = "-e 416110".split(" ");
            //args = "-d 6d74d8c7e11a0514af45c6b3ab10c664".split(" ");
            args = "-m -j".split(" ");
            //args = "-m 1 -to ylyan126@126.com -b 100 -th 200".split(" ");
        }

        if ("-e".equals(args[0]) || "-d".equals(args[0])) {
            aesParams(args);
        } else if ("-m".equals(args[0])) {
            if (localDebugger) {
                conf.setDebugger(true);
            }

            //
            conf.readConf();

            if (args.length > 1 && "-j".equals(args[1])) {
                //交互
                select();
            } else {
                //指令
                params(args);
            }
        }

    }


    private static void aesParams(String[] args) {

        // 加密解密参数解析
        for (int i = 0; i < args.length; i++) {
            type = args[0];

            if ("-t".equals(args[i])) {
                txt = args[++i];
            }
        }
        if (StringUtil.isEmpty(txt)) {
            if (args.length == 1) {

            } else if (args.length == 2) {
                // 不带前缀的 最后一位参数
                txt = args[1];
            }
        }

        AESUtil aes = AESUtil.getInstance();
        if ("-e".equals(type)) {
            System.out.println(aes.encrypt(txt));
        } else if ("-d".equals(type)) {
            System.out.println(aes.decrypt(txt));
        }

    }

    private static void params(String[] args) {


        Map<String, String> vm = new HashMap<String, String>();

        String type = "";
        for (int i = 0; i < args.length; i++) {
            if ("-m".equals(args[i])) {
                type = args[(++i)];
            }

            if ("-to".equals(args[i])) {
                conf.addTo(args[(++i)]);
            }

            if ("-att".equals(args[i])) {
                conf.addAtt(args[(++i)]);
            }

            if ("-b".equals(args[i])) {
                vm.put("balance", args[(++i)]);
            }

            if ("-th".equals(args[i])) {
                vm.put("threshHold", args[(++i)]);
            }

            if ("-url".equals(args[i])) {
                vm.put("url", args[(++i)]);
            }

            if ("-pwd".equals(args[i])) {
                vm.put("pwd", args[(++i)]);
            }
        }

        conf.setVm(vm);

        if ("-q".equals(type.trim()) || "q".equals(type.trim())) {
            System.exit(0);
        }

        if ("1".equals(type))
            sendMail(conf);
        else if ("2".equals(type))
            sendMail2(conf);
        else if ("3".equals(type))
            sendMail3(conf);
    }

    private static void select() {
        while (true) {
            String m_type = scln("【1】:余额告警,【2】:用户注册,【3】:附件");
            if (("q".equals(m_type)) || ("-q".equals(m_type))) {
                System.exit(0);
            }
            if ("1".equals(m_type)) {
                select_1();
            } else if ("2".equals(m_type)) {
                select_2();
            } else if ("3".equals(m_type)) {
                select_3();
            }
        }
    }

    private static void select_1() {
        conf.addTo(scn("接收人地址:"));

        Map<String, String> vm = new HashMap<String, String>();
        vm.put("balance", scn("余额:"));
        vm.put("threshHold", scn("告警值:"));
        conf.setVm(vm);

        sendMail(conf);
    }

    private static void select_2() {
        conf.addTo(scn("接收人地址:"));

        Map<String, String> vm = new HashMap<String, String>();
        vm.put("url", scn("平台域名:"));
        vm.put("pwd", scn("pwd:"));
        conf.setVm(vm);

        sendMail2(conf);
    }

    private static void select_3() {

        conf.addTo(scn("接收人地址:"));
        conf.addAtt(scn("附件地址:"));

        sendMail3(conf);
    }

    private static String scn(String str) {
        Scanner sc1 = new Scanner(System.in);

        String result = "";
        while (true) {
            if (StringUtil.isEmpty(result)) {
                System.out.println();
                System.out.print(str);
                result = sc1.nextLine();
            } else {
                break;
            }
        }

        return result;
    }

    private static String scln(String str) {
        Scanner sc1 = new Scanner(System.in);

        String result = "";
        while (true) {
            if (StringUtil.isEmpty(result)) {
                System.out.println(str);
                result = sc1.nextLine();
            } else {
                break;
            }
        }

        return result;
    }

    private static void sendMail(MailConf mailInfo) {
        try {
            // 添加到邮件通知队列
            JobWorker.addJob(new TestEmail1(mailInfo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendMail2(MailConf mailInfo) {
        try {
            // 添加到邮件通知队列
            JobWorker.addJob(new TestEmail2(mailInfo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendMail3(MailConf mailInfo) {
        try {
            // 添加到邮件通知队列
            JobWorker.addJob(new TestEmail3(mailInfo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
