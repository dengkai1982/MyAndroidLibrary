package com.sourceforge.apklib.foundation.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * 字符编码工具类
 */
public class CoderUtils {
    private static final Logger logger=Logger.getLogger(CoderUtils.class);
    /**
     * 默认编码utf-8
     */
    public static final String DEFAULT_CHARSET="utf-8";
    /**
     * 获取指定的字符编码,无法获取时返回默认编码utf-8
     * @param cs
     * @return
    */
    public static final Charset getCharset(String cs){
        return Charset.forName(cs);
    }
    /**
     * url解码
     * @param encode
     * @return
     */
    public static final String urlDecode(String encode){
        return urlDecode(encode, DEFAULT_CHARSET.toString());
    }
    /**
     * url解码
     * @param encode
     * @param charset 字符集
     * @return
     */
    public static final String urlDecode(String encode,String charset){
        try {
            return URLDecoder.decode(encode, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encode;
    }
    /**
     * URL编码
     * @param url
     * @return
     */
    public static final String urlEncode(String url){
        return urlEncode(url, DEFAULT_CHARSET.toString());
    }
    /**
     * URL编码
     * @param url
     * @param charset 字符集
     * @return
     */
    public static final String urlEncode(String url,String charset){
        try {
            return URLEncoder.encode(url,charset);
        } catch (UnsupportedEncodingException e) {
            logger.e(e.getMessage(),e);
        }
        return url;
    }
    /**
     * base64编码，编码失败返回原字符
     * @param text
     * @return
     */
    public static final String androidBase64Encode(String text){
        return androidBase64Encode(text,DEFAULT_CHARSET.toString());
    }
    /**
     * base64编码，编码失败返回原字符
     * @param text
     * @param charset 字符集
     * @return
     * @throws UnsupportedEncodingException
     */
    public static final String androidBase64Encode(String text,String charset){
        try {
            byte[] bytes = android.util.Base64.encode(text.getBytes(charset), android.util.Base64.DEFAULT);
            return new String(bytes);
        } catch (UnsupportedEncodingException e) {
            logger.e(e.getMessage(),e);
        }
        return text;
    }
    /**
     * base64解码,解码失败返回原字符
     * @param b64
     * @return
     */
    public static final String androidBase64Decode(String b64){
        return androidBase64Decode(b64,DEFAULT_CHARSET.toString());
    }
    /**
     * base64解码,解码失败返回原字符
     * @param b64
     * @param charset 字符集
     * @return
     * @throws UnsupportedEncodingException
     */
    public static final String androidBase64Decode(String b64,String charset){
        try {
            byte[] bytes=android.util.Base64.decode(b64, android.util.Base64.DEFAULT);
            return new String(bytes,charset);
        } catch (UnsupportedEncodingException e) {
        }
        return b64;
    }
    static final HashMap<String, String> md5Cache;
    static{
        md5Cache=new HashMap<String, String>();
    }
    /**
     * md5编码，编码失败返回原字符
     * @param str
     * @return
     */
    public static final String MD5(String str){
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("md5");
            byte[] bytes=digest.digest(str.getBytes(DEFAULT_CHARSET.toString()));
            return toHexString(bytes);
        } catch (NoSuchAlgorithmException e) {
            logger.e(e.getMessage(),e);
        } catch (UnsupportedEncodingException e) {
            logger.e(e.getMessage(),e);
        }
        return str;
    }
    /**
     * md5编码，并使用内存缓存
     * @param str
     * @return
     */
    public static final String MD5UseCache(String str){
        try {
            String result=md5Cache.get(str);
            if(result==null){
                MessageDigest digest=MessageDigest.getInstance("md5");
                byte[] bytes=digest.digest(str.getBytes("utf-8"));
                result=toHexString(bytes);
                md5Cache.put(str,result);
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            logger.e(e.getMessage(),e);
        } catch (UnsupportedEncodingException e) {
            logger.e(e.getMessage(),e);
        }
        return "";
    }
    /**
     * 显示二进制
     * @param bytes
     * @return
     */
    public static final String toHexString(byte[] bytes){
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
