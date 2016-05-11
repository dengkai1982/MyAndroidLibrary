package com.sourceforge.apklib.foundation.http;

import com.sourceforge.apklib.foundation.cache.HttpRequestCacher;
import com.sourceforge.apklib.foundation.http.HttpMethod;
import com.sourceforge.apklib.foundation.http.HttpRequest;
import com.sourceforge.apklib.foundation.http.HttpRequestConnector;
import com.sourceforge.apklib.foundation.http.HttpResponse;
import com.sourceforge.apklib.foundation.http.HttpStatus;
import com.sourceforge.apklib.foundation.thread.ThreadExecutor;
import com.sourceforge.apklib.foundation.utils.HttpUtils;

/**
 * Http请求执行器,调用doRequest方法执行http请求
 */
public class HttpRequestExecutor extends ThreadExecutor<HttpRequest,Void, HttpResponse> {

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
     * http缓存
     */
    private HttpRequestCacher httpCacher;
    /**
     * 执行http请求
     * @param request
     */
    public void doRequest(HttpRequest request){
        this.executThread(request);
    }


    /**
     * 执行http请求
     * @param request
     */
    public void doRequest(HttpRequest request, HttpRequestCacher httpCacher){
        if(httpCacher!=null&&request.getRequestMethod().equals(HttpMethod.GET)){
            this.modifiedSince=httpCacher.getModifiedSince(request);
            this.httpCacher=httpCacher;
            request.setCookies(httpCacher.getCookies(HttpUtils.getHostNameByUrl(request.getUrl())));
        }
    }

    public HttpRequestExecutor(){
        charset="utf-8";
        enableGzip=true;
        followRedirects=true;
        socketTimeout=5000;
        readWriteTimeout=10000;
        modifiedSince=0;
    }

    @Override
    protected void onPostExecute(HttpResponse response) {
        HttpRequest request=response.getRequest();
        if(response.isRequestIsSuccess()){
            if(response.getStatus().equals(HttpStatus.OK)){
                //处理请求成功
                request.getHttpRequestListener().onMessageReceived(request,response,response.getRequestResult());
                if(httpCacher!=null){
                    httpCacher.storage(request,response);
                    httpCacher.storageCookies(HttpUtils.getHostNameByUrl(response.getRequest().getUrl()),
                            response.getCookies());
                }
            }else if(response.getStatus().equals(HttpStatus.FOUND)){
                //处理302请求
                if(httpCacher!=null){
                    request.getHttpRequestListener().onMessageReceived(request,response,
                            httpCacher.recovery(request));
                }
            }
        }else if(response.getThrowable()!=null){
            request.getHttpRequestListener().onThrowException(request,response.getThrowable());
        }
    }

    @Override
    protected HttpResponse doInBackground(HttpRequest httpRequest) throws InterruptedException {
        HttpRequestConnector connector=new HttpRequestConnector();
        connector.setCharset(charset);
        connector.setEnableGzip(enableGzip);
        connector.setFollowRedirects(followRedirects);
        connector.setReadWriteTimeout(readWriteTimeout);
        connector.setSocketTimeout(socketTimeout);
        return connector.doRequest(httpRequest);
    }


    public void setEnableGzip(boolean enableGzip) {
        this.enableGzip = enableGzip;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public void setModifiedSince(long modifiedSince) {
        this.modifiedSince = modifiedSince;
    }

    public void setReadWriteTimeout(int readWriteTimeout) {
        this.readWriteTimeout = readWriteTimeout;
    }
}
