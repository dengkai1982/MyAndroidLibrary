package com.sourceforge.apklib.foundation.utils;
import android.net.Uri;
import android.support.v4.util.ArrayMap;
import com.sourceforge.apklib.foundation.http.HttpCookie;
import com.sourceforge.apklib.foundation.http.HttpHeaderNames;
import com.sourceforge.apklib.foundation.http.HttpRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * http 工具类
 */
public class HttpUtils {
    /**
     * 获取Http相应的编码
     * @param contentType
     * @return
     */
    public static String getCharset(String contentType){
        if(contentType!=null){
            String[] ct= contentType.split(";");
            if(ct.length==2){
                return ct[1].split("=")[1].trim();
            }
        }
        return "utf-8";
    }
    public static String getMimeType(String contentType){
        if(contentType!=null){
            String[] ct= contentType.split(";");
            if(ct.length==2){
                return ct[0];
            }
        }
        return "text";
    }
    /**
     * 转换cookie为字符串
     * @param cookies
     * @param charset
     * @return
     */
    public static final String getCookiesString(Collection<HttpCookie> cookies, String charset){
        if(cookies==null||cookies.isEmpty())return "";
        StringBuilder sb=new StringBuilder();
        int index=0;
        for(HttpCookie cookie:cookies){
            try {
                sb.append(cookie.getName()).append("=")
                        .append(URLEncoder.encode(cookie.getValue(),charset));
                if(index<cookies.size()-1){
                    sb.append(";");
                }
                index++;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 将服务器返回的cookie字符串解析为HttpCookie
     * Set-Cookie:bDpK1_c804_lastact=1461686991%09index.php%09;expires=Wed, 27-Apr-2016 16:09:51 GMT;
     * path=/; domain=.zg163.net
     * @param cookieString
     * @return
     */
    public static final Set<HttpCookie> parseCookies(String cookieString){
        Set<HttpCookie> cookies=new HashSet<>();
        String[] split=cookieString.split(";");
        if(split!=null){
            for(String spt:split){
                String[] entry=spt.split("=");
                if(entry.length==2){
                    HttpCookie cookie=new HttpCookie();
                    cookie.setName(entry[0].trim());
                    cookie.setValue(entry[1].trim());
                    cookies.add(cookie);
                }

            }
        }
        return cookies;
    }

    public static final ArrayMap<HttpHeaderNames,String> convertHeader(Map<String,List<String>> header){
        ArrayMap<HttpHeaderNames,String> mh=new ArrayMap<>();
        Set<String> keys=header.keySet();
        for(String key:keys){
            if(key!=null){
                HttpHeaderNames names=HttpHeaderNames.getHeaderByName(key);
                List<String> values=header.get(key);
                StringBuilder builder=new StringBuilder();
                if(values!=null) {
                    for(int i=0;i<values.size();i++){
                        builder.append(values.get(i));
                        if(i<values.size()-1){
                            builder.append(",");
                        }
                    }
                    mh.put(names,builder.toString());
                }
            }

        }
        return mh;
    }

    /**
     * 构建http请求的唯一标示,请求地址加请求参数
     * @param request
     * @return
     */
    public static final String buildRequestKey(HttpRequest request){
        String url=request.getUrl();
        return CoderUtils.MD5UseCache(url);
    }

    public static final String getHostNameByUrl(String url){
        Uri uri=Uri.parse(url);
        return uri.getHost();
    }
}
