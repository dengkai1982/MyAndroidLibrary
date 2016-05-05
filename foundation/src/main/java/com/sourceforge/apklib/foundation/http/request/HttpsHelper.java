package com.sourceforge.apklib.foundation.http.request;

import java.io.IOException;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * Created by kaiyi on 16/4/27.
 */
public class HttpsHelper {
    private KeyManager[] keyManagers;
    private TrustManager[] trustManagers;
    private boolean isHostnameVerifier;
    private String domain;
    private String host;
    private int port=443;
    public HttpsHelper(){
        trustManagers=new TrustManager[]{
                TrustManagerFactoryUtil.getDefaultTrustManager()
        };
        isHostnameVerifier=false;
    }
    public boolean isHostnameVerifier() {
        return isHostnameVerifier;
    }

    public void setHostnameVerifier(boolean hostnameVerifier) {
        isHostnameVerifier = hostnameVerifier;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public HttpsHelper(KeyManager[] keyManagers, TrustManager[] trustManagers) {
        this.keyManagers = keyManagers;
        this.trustManagers = trustManagers;
        isHostnameVerifier=false;
    }
    public SSLContext getSSLContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(keyManagers,trustManagers,new SecureRandom());
            return sslContext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean hostnameVerifier(URLConnection connect, SSLContext context) {
        if(isHostnameVerifier){
            try {
                SSLSocketFactory sslSocketFactory=context.getSocketFactory();
                SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(domain,port);
                ((HttpsURLConnection)connect).setSSLSocketFactory(context.getSocketFactory());
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                SSLSession session=socket.getSession();
                if(!hv.verify(host, session)){
                    return true;
                    //throw new SSLHandshakeException("Expected mail.google.com, found " + session.getPeerPrincipal());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
