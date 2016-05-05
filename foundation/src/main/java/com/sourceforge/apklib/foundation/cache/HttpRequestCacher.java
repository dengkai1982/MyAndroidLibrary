package com.sourceforge.apklib.foundation.cache;

import com.sourceforge.apklib.foundation.http.HttpCookie;
import com.sourceforge.apklib.foundation.http.HttpRequest;
import com.sourceforge.apklib.foundation.http.HttpResponse;

import java.util.Set;

/**
 * http 结果缓存
 */
public interface HttpRequestCacher extends Cacher<HttpRequest,HttpResponse>{
    /**
     * 获取上次请求时间
     * @return
     */
    public long getModifiedSince(HttpRequest request);

    public void storageCookies(String hostName, Set<HttpCookie> cookies);

    public Set<HttpCookie> getCookies(String hostName);
}
