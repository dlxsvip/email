package com.yyl.client.notify;

import com.yyl.client.cache.CacheMapHolder;
import com.yyl.client.model.MailConf;
import com.yyl.client.utils.AESUtil;
import com.yyl.client.utils.DateUtil;
import com.yyl.client.utils.FileUtil;
import com.yyl.client.utils.ReadUtil;
import org.apache.velocity.app.VelocityEngine;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by yl on 2016/10/9.
 */
public abstract class BaseSend implements Runnable {


    // 邮件配置文件
    protected static String MAIL_PROPERTIES = "email.properties";

    // 编码
    protected static String ENCODING = "UTF-8";

    // vm 模板路径
    protected static String VM_FOLDER = "vm";

    // 默认的时间格式
    protected static String DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // 解密工具
    protected static AESUtil aes = AESUtil.getInstance();

    // 配置缓存
    protected static Map<String, String> cache = CacheMapHolder.getCacheMap().getEmailFrom();


    protected VelocityEngine initVelocity() throws Exception {

        //String fileDir = Thread.currentThread().getContextClassLoader().getResource(VM_FOLDER).getPath();
        String fileDir = FileUtil.getClassRootPath()+VM_FOLDER+"/";
        //String jarPath = ReadUtil.getJarPath();
        // 读取jar包里的配置文件
        //InputStream in = ReadUtil.getJarFile(jarPath+VM_FOLDER);
        //String fileDir = jarPath + File.separator + VM_FOLDER;
        System.out.println("jar_vm:" + fileDir);


        VelocityEngine ve = new VelocityEngine();
        Properties pros = new Properties();
        pros.setProperty(ve.FILE_RESOURCE_LOADER_PATH, fileDir);
        ve.init(pros);

        return ve;
    }

    // 读取配置文件
    protected void readConf(Map<String, String> param) {

        readCache();

        param.putAll(cache);

        if (param.containsKey("isAES") && "true".equals(param.get("isAES"))) {
            String strValue = param.get("emailPassword");
            strValue = aes.decrypt(strValue);
            param.put("emailPassword", strValue);
        }
    }



    private void readCache() {
        try {
            // 读缓存
            if (isCache()) {
                cache = new HashMap<String, String>();
                // 读取配置文件
                cache = ReadUtil.read(MAIL_PROPERTIES, ENCODING);
                // 刷新时间
                cache.put("cacheDate", DateUtil.date2str(new Date(), DATA_FORMAT));
                // 设置缓存
                CacheMapHolder.getCacheMap().setEmailFrom(cache);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 是否刷新缓存
    private boolean isCache() {
        try {

            if (null == cache) {
                return true;
            }

            // 上次刷新缓存的时间
            String cacheDateStr = cache.get("cacheDate");
            Date cacheDate = DateUtil.str2date(cacheDateStr, DATA_FORMAT);

            // 现在时间
            Date nowDate = new Date();

            // 现在时间和上次刷新缓存的时间差
            long diff = nowDate.getTime() - cacheDate.getTime();

            if (cache.containsKey("cacheTime")) {
                long cacheTime = 0;
                String granularity = "0";
                String tmp = cache.get("cacheTime").trim();
                if ("0".equals(tmp)) {
                    return false;
                } else if (tmp.contains("s")) {
                    granularity = tmp.replace("s", "");
                    cacheTime = Long.parseLong(granularity) * 1000;
                } else if (tmp.contains("m")) {
                    granularity = tmp.replace("m", "");
                    cacheTime = Long.parseLong(granularity) * 1000 * 60;
                } else if (tmp.contains("h")) {
                    granularity = tmp.replace("h", "");
                    cacheTime = Long.parseLong(granularity) * 1000 * 60 * 60;
                } else if (tmp.contains("d")) {
                    granularity = tmp.replace("d", "");
                    cacheTime = Long.parseLong(granularity) * 1000 * 60 * 60 * 24;
                } else {
                    return false;
                }

                if (diff > cacheTime) {
                    // 重新读配置文件
                    return true;
                }
            }

        } catch (Exception e) {
            // 异常，则重新读取配置文件
            return true;
        }

        return false;
    }


}
