package com.sourceforge.apklib.foundation.http;

import com.sourceforge.apklib.foundation.utils.FilesystemUtils;
import com.sourceforge.apklib.foundation.utils.HttpUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import android.support.v4.util.ArrayMap;
/**
 * Http 连接
 */
public class HttpRequestConnector {
    /**
     * 是否自动重定向3xx的返回码
     */
    private boolean followRedirects;
    /**
     * 字符编码
     */
    private String charset;
    /**
     * 链接超时时长
     */
    private int socketTimeout;
    /**
     * 上一次获取时间
     */
    private long modifiedSince;
    /**
     * 读取超时时长
     */
    private int readWriteTimeout;
    /**
     * 是否启用Gzip压缩
     */
    private boolean enableGzip;
    /**
     * 本次请求随机值
     */
    private String requestToken;


    /**
     * 构造http请求
     */
    public HttpRequestConnector(){
        this.followRedirects=true;
        this.charset="utf-8";
        this.socketTimeout=5000;
        this.modifiedSince=0;
        this.readWriteTimeout=10000;
        this.enableGzip=true;
        this.requestToken= UUID.randomUUID().toString();
    }

    /**
     * 执行http结果解析
     * @param request http请求
     */
    public HttpResponse doRequest(final HttpRequest request){
        HttpResponse resp=new HttpResponse();
        resp.setRequest(request);
        resp.setRequestToken(this.requestToken);
        resp.setHttpMethod(request.getRequestMethod());
        resp.setRequestIsSuccess(false);
        HttpURLConnection connect = null;
        try {
            URL url = new URL(request.getUrl());
            SSLContext sslContext=request.getSSLContext();
            if(sslContext==null){
                connect=(HttpURLConnection) url.openConnection();
            }else{
                connect=(HttpsURLConnection)url.openConnection();
                ((HttpsURLConnection)connect).setSSLSocketFactory(sslContext.getSocketFactory());
                if(!request.hostnameVerifier(connect,sslContext)){
                    resp.setThrowable(new MalformedURLException("Host Name verify error."));
                    return resp;
                }
            }
            HttpsURLConnection.setFollowRedirects(followRedirects);
            addHeader(connect,request);
            HttpMethod method=request.getRequestMethod();
            if(method.equals(HttpMethod.POST)){
                InputStream in=request.getParameterInputStream(connect,this.charset);
                connect.setDoOutput(true);
                FilesystemUtils.writeInputToOutput(in, connect.getOutputStream(),1024);
                FilesystemUtils.close(in);
            }
            //读取返回
            if(!connect.getDoInput()){
                connect.setDoInput(true);
            }
            resp.setStatus(HttpStatus.valueOf(connect.getResponseCode()));
            Map<String,List<String>> headers=connect.getHeaderFields ();
            if(resp.getStatus().equals(HttpStatus.FOUND)){
                //返回302重定向,重新进行请求
                return doRequest(request);
            }else{
                String encoding=connect.getContentEncoding();
                resp.setContentEncoding(encoding);
                String cookieString=connect.getHeaderField(HttpHeaderNames.SET_COOKIE.toString());
                if(cookieString!=null){
                    resp.setCookies(HttpUtils.parseCookies(cookieString));
                }
                resp.setHeaders(HttpUtils.convertHeader(connect.getHeaderFields()));
                InputStream resultStream;
                if(encoding!=null&&encoding.equalsIgnoreCase("gzip")&&enableGzip){
                    resultStream=new GZIPInputStream(connect.getInputStream());
                }else{
                    resultStream=connect.getInputStream();
                }
                String resultCharset=connect.getContentType();
                resultCharset=HttpUtils.getCharset(resultCharset);
                resp.setPageCharset(resultCharset);
                resp.setLastModified(connect.getLastModified());
                resp.setMimeType(HttpUtils.getMimeType(connect.getContentType()));
                resp.setRequestIsSuccess(true);
                resp.setRequestResult(request.getHttpRequestListener()
                        .onParseMessage(resultStream,resultCharset));
                FilesystemUtils.close(resultStream);
                return resp;
            }
        } catch (Exception e) {
            resp.setThrowable(e);
        } finally{
            if(connect!=null){
                connect.disconnect();
            }
        }
        return resp;
    }
    //添加请求头
    private void addHeader(HttpURLConnection connect,HttpRequest request) throws ProtocolException {
        connect.setConnectTimeout(socketTimeout);
        connect.setReadTimeout(readWriteTimeout);
        connect.setRequestMethod(request.getRequestMethod().name());
        connect.setIfModifiedSince(modifiedSince);
        ArrayMap<String,String> requestHeader=request.getHeaders();
        Set<String> keys=requestHeader.keySet();
        for(String key:keys){
            connect.setRequestProperty(key,requestHeader.get(key));
        }
        connect.setRequestProperty(HttpHeaderNames.CONNECTION.toString(), "close");
        if(enableGzip) {
            connect.setRequestProperty(HttpHeaderNames.ACCEPT_ENCODING.toString(), "gzip");
        }
        if(request.getCookies()!=null&&!request.getCookies().isEmpty()){
            //添加cookie
            String cookieValue=HttpUtils.getCookiesString(request.getCookies(),this.charset);
            connect.setRequestProperty(HttpHeaderNames.COOKIE.toString(),cookieValue);
        }
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public long getModifiedSince() {
        return modifiedSince;
    }

    public void setModifiedSince(long modifiedSince) {
        this.modifiedSince = modifiedSince;
    }

    public int getReadWriteTimeout() {
        return readWriteTimeout;
    }

    public void setReadWriteTimeout(int readWriteTimeout) {
        this.readWriteTimeout = readWriteTimeout;
    }

    public boolean isEnableGzip() {
        return enableGzip;
    }

    public void setEnableGzip(boolean enableGzip) {
        this.enableGzip = enableGzip;
    }
}
