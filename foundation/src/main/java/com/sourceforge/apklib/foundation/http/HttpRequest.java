package com.sourceforge.apklib.foundation.http;

import android.support.v4.util.ArrayMap;
import com.sourceforge.apklib.foundation.data.DataTyper;
import com.sourceforge.apklib.foundation.http.listener.StringHttpRequestListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.net.ssl.SSLContext;

/**
 * http请求
 */
public abstract class HttpRequest {
    private  String url;//请求url,类似于http://www.apple.com
    private ArrayMap<HttpHeaderNames,String> headerMap;
    private Set<HttpCookie> cookies;
    private HttpRequestListener listener;

    public HttpRequest(HttpRequestListener listener){
        this.listener=listener;
        cookies=new HashSet<>();
        setDefaultRequestHeader();
    }

    public HttpRequestListener getHttpRequestListener(){
        if(this.listener==null){
            return new StringHttpRequestListener() {
                @Override
                public void onThrowException(HttpRequest request, Throwable e) {

                }

                @Override
                public void onMessageReceived(HttpRequest request, HttpResponse response, String result) {

                }
            };
        }else {
            return this.listener;
        }
    }

    //设置默认请求头
    private void setDefaultRequestHeader(){
        headerMap=new ArrayMap<>();
        headerMap.put(HttpHeaderNames.USER_AGENT,"Mozilla/5.0 (Windows NT 6.3; WOW64) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.124 Safari/537.36");
        headerMap.put(HttpHeaderNames.ACCEPT_LANGUAGE,"zh-CN,zh;q=0.8");
        headerMap.put(HttpHeaderNames.ACCEPT,"text/html,application/xhtml+xml,application/json,text/css,text/javascript,application/xml;q=0.9,image/webp,*/*;q=0.8\"");
    }
    /**
     * 添加http请求头
     * @param headerName 请求头名称
     * @param value 请求头值
     */
    public void addRequestHeader(HttpHeaderNames headerName,String value){
        headerMap.put(headerName,value);
    }
    /**
     * 获取请求头
     */
    public ArrayMap<String,String> getHeaders(){
        ArrayMap<String,String> header=new ArrayMap<>();
        Set<HttpHeaderNames> httpHeaderNames = headerMap.keySet();
        for(HttpHeaderNames name:httpHeaderNames){
            header.put(name.toString(),headerMap.get(name));
        }
        return header;
    }
    /**
     * 获取请求方法
     */
    public abstract HttpMethod getRequestMethod();
    /**
     * 获取http安全连接
     */
    public SSLContext getSSLContext(){
        return null;
    }

    /**
     * 获取请求参数,如果请求方式为get,此处返回null
     */
    protected abstract ArrayMap<String,DataTyper> getRequestParameter();
    /**
     * 获取参数输入流
     * @return
     */
    protected abstract InputStream getParameterInputStream(HttpURLConnection connection,String charset) throws IOException;
    /**
     * 执行主机名验证
     * @param context
     * @return
     */
    public  boolean hostnameVerifier(URLConnection connect,SSLContext context){
        return context!=null;
    }
    /**
     * 添加cookies
     * @param name
     * @param value
     */
    public void addCookie(String name,String value){
        this.cookies.add(new HttpCookie(name,value));
    }

    /**
     * 添加cookies
     * @param cookies
     */
    public void addCookies(Collection<HttpCookie> cookies){
        for(HttpCookie cookie:cookies){
            this.cookies.add(cookie);
        }
    }
    /**
     * 添加cookie
     * @param cookies
     */
    public void addCookies(HttpCookie[] cookies){
        for(HttpCookie cookie:cookies){
            this.cookies.add(cookie);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayMap<HttpHeaderNames, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(ArrayMap<HttpHeaderNames, String> headerMap) {
        this.headerMap = headerMap;
    }

    public Set<HttpCookie> getCookies() {
        return cookies;
    }

    public void setCookies(Set<HttpCookie> cookies) {
        this.cookies = cookies;
    }
}

