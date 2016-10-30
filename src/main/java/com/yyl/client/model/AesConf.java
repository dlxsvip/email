package com.yyl.client.model;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yl on 2016/10/20.
 */
public class AesConf {

    /**
     * 密钥长度 128,192,256  默认: 128
     */
    private int keySize = 128;

    /**
     * 迭代次数
     */
    private int iterationCount = 1000;


    /**
     * 算法/模式/填充
     */
    private String padding = "AES/CBC/PKCS5Padding";


    /**
     * 加密解密
     */
    private Cipher cipher = initCipher();


    /**
     * 默认密钥
     */
    private String key = "1234567890asdq@#";

    /**
     * 默认盐值
     */
    private String salt = "0123456789abcdef";

    /**
     * 默认向量
     */
    private String iv = "0123456789abcdef";


    private Cipher initCipher() {
        try {
            return Cipher.getInstance(padding);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getKeySize() {
        return keySize;
    }


    public int getIterationCount() {
        return iterationCount;
    }


    public String getPadding() {
        return padding;
    }


    public Cipher getCipher() {
        return cipher;
    }


    public String getKey() {
        return key;
    }


    public String getSalt() {
        return salt;
    }


    public String getIv() {
        return iv;
    }


}
