package com.sourceforge.apklib.foundation.http.request;
import com.sourceforge.apklib.foundation.http.HttpRequestListener;

import java.net.URLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class HttpsGet extends HttpGet {
    private HttpsHelper httpsHelper;
    public HttpsGet(HttpRequestListener listener) {
        super(listener);
        httpsHelper=new HttpsHelper();
    }

    public HttpsGet(HttpRequestListener listener,KeyManager[] keyManagers, TrustManager[] trustManagers) {
        super(listener);
        httpsHelper=new HttpsHelper(keyManagers,trustManagers);
    }

    public boolean isHostnameVerifier() {
        return httpsHelper.isHostnameVerifier();
    }

    public void setHostnameVerifier(boolean hostnameVerifier) {
        httpsHelper.setHostnameVerifier(hostnameVerifier);
    }

    public String getDomain() {
        return httpsHelper.getDomain();
    }

    public void setDomain(String domain) {
        httpsHelper.setDomain(domain);
    }

    public String getHost() {
        return httpsHelper.getHost();
    }

    public void setHost(String host) {
        httpsHelper.setHost(host);
    }

    public int getPort() {
        return httpsHelper.getPort();
    }

    public void setPort(int port) {
        httpsHelper.setPort(port);
    }


    @Override
    public SSLContext getSSLContext() {
        return httpsHelper.getSSLContext();
    }

    @Override
    public boolean hostnameVerifier(URLConnection connect,SSLContext context) {
        return httpsHelper.hostnameVerifier(connect,context);
    }
}
