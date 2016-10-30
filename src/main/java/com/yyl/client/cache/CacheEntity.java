package com.yyl.client.cache;

import java.util.Map;

/**
 * Created by yl on 2016/10/9.
 */
public class CacheEntity {

    /**
     * 缓存发件人地址(第一次时调用缓存)
     */
    private Map<String, String> emailFrom;


    public Map<String, String> getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(Map<String, String> emailFrom) {
        this.emailFrom = emailFrom;
    }
}
