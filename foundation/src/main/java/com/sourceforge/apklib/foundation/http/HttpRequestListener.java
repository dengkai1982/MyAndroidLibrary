package com.sourceforge.apklib.foundation.http;

import java.io.InputStream;

/**
 * http 请求坚挺
 */
public interface HttpRequestListener<T>{
    /**
     * http请求出现异常时调用,通过HttpRequestExecutor调用，此方法在主线程中可用
     * @param request
     * @param e
     */
    void onThrowException(HttpRequest request, Throwable e);
    /**
     * 请求完毕后调用,通过HttpRequestExecutor调用，此方法在主线程中可用
     * @param request
     * @param response
     */
    void onMessageReceived(HttpRequest request, HttpResponse response, T result);
    /**
     * 解析http请求放回结果,通过HttpRequestExecutor调用，此方法不可在主线程中调用
     * @param in
     * @return
     */
    T onParseMessage(InputStream in, String charset);
}
