package com.yyl.client.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yl on 2016/10/9.
 */
public class DateUtil {

    // 默认的时间格式
    public static String TMP_1 = "yyyy-MM-dd HH:mm:ss";
    public static String TMP_2 = "yyyy-MM-dd";

    // 实例
    private static DateUtil INSTANCE;

    // 私有化构造函数
    private DateUtil() {
    }

    // 对外暴漏获取实例的方法
    public static DateUtil getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new DateUtil();
        }

        return INSTANCE;
    }

    // date --> str
    public static String date2str(Date inputDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TMP_1);

        return dateFormat.format(inputDate);
    }

    // date --> str
    public static String date2str(Object obj, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(obj);
    }

    // str --> date
    public static Date str2date(String dateStr) {
        Date date = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(TMP_1);
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    // str --> date
    public static Date str2date(String dateStr, String format) {
        Date date = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}
