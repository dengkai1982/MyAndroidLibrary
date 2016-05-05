package com.sourceforge.apklib.foundation.http;

import android.support.v4.util.ArrayMap;

import com.sourceforge.apklib.foundation.data.DataTyper;

import java.io.InputStream;
import java.util.Set;

/**
 * http相应
 */
public class HttpResponse{
    /**
     * http请求tooken
     */
    private String requestToken;
    /**
     * http请求头
     */
    private ArrayMap<HttpHeaderNames,String> headers;
    /**
     * Cookies
     */
    private Set<HttpCookie> cookies;
    /**
     * http返回码
     */
    private HttpStatus status;
    /**
     * http contentEncoding
     */
    private String contentEncoding;
    /**
     * 响应mimeType
     */
    private String mimeType;
    /**
     * http请求方法
     */
    private HttpMethod httpMethod;
    /**
     * http返回内容编码
     */
    private String pageCharset;

    private HttpRequest request;
    //请求结果
    private Object requestResult;
    //请求是否成功
    private boolean requestIsSuccess;
    //请求失败异常
    private Throwable throwable;

    private long lastModified;
    /**
     * 根据名称获取cookie的值
     * 无法获取返回null
     * @param name
     */
    public String getCookieByName(String name){
        if(cookies!=null){
            for(HttpCookie cookie:cookies){
                if(cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public ArrayMap<HttpHeaderNames, String> getHeaders() {
        return headers;
    }

    public void setHeaders(ArrayMap<HttpHeaderNames, String> headers) {
        this.headers = headers;
    }

    public Set<HttpCookie> getCookies() {
        return cookies;
    }

    public void setCookies(Set<HttpCookie> cookies) {
        this.cookies = cookies;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getPageCharset() {
        return pageCharset;
    }

    public void setPageCharset(String pageCharset) {
        this.pageCharset = pageCharset;
    }


    public Object getRequestResult(){
        return requestResult;
    }

    public void setRequestResult(Object requestResult) {
        this.requestResult = requestResult;
    }

    public boolean isRequestIsSuccess() {
        return requestIsSuccess;
    }

    public void setRequestIsSuccess(boolean requestIsSuccess) {
        this.requestIsSuccess = requestIsSuccess;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}
