package com.sourceforge.apklib.foundation.http.request;
import com.sourceforge.apklib.foundation.data.DataTyper;
import com.sourceforge.apklib.foundation.http.HttpMethod;
import com.sourceforge.apklib.foundation.http.HttpRequest;
import com.sourceforge.apklib.foundation.http.HttpRequestListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import android.support.v4.util.ArrayMap;
/**
 * http get 请求
 */
public class HttpGet extends HttpRequest {

    public HttpGet(HttpRequestListener listener) {
        super(listener);
    }

    @Override
    public HttpMethod getRequestMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected ArrayMap<String, DataTyper> getRequestParameter() {
        return null;
    }

    @Override
    protected InputStream getParameterInputStream(HttpURLConnection connection, String charset) {
        return null;
    }
}
