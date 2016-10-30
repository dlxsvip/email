package com.yyl.client.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yl on 2016/10/9.
 */
public class CacheMapHolder {
    private CacheEntity cache;

    // 自定义缓存map
    private static Map<String, CacheEntity> cacheMap = new HashMap<String, CacheEntity>();



    public static CacheEntity getCacheMap() {
        if (null == cacheMap.get("cache")) {
            cacheMap.put("cache", new CacheEntity());
        }

        return cacheMap.get("cache");
    }







}
