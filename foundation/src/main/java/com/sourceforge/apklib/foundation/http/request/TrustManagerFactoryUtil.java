package com.sourceforge.apklib.foundation.http.request;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class TrustManagerFactoryUtil {
	/**
	 * 获取Keystore记录的信任证书
	 * @return
	 * @throws KeyStoreException 
	 */
	public static final TrustManagerFactory getKeystoreFactory(KeyStore keyStore) throws KeyStoreException{
//		TrustManagerFactory factory=InsecureTrustManagerFactory.INSTANCE;
//		try {
//			factory = TrustManagerFactory.getInstance("PKIX");
//			factory.init(keyStore);
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//		return factory;
		TrustManagerFactory factory=null;
		try {
			factory=TrustManagerFactory.getInstance("PKIX");
			factory.init(keyStore);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return factory;
	}
	/**
	 * 获取默认不进行任何验证的证书管理器
	 * @return
     */
	public static final TrustManager getDefaultTrustManager(){
		return new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

			}

			@Override
			public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		};
	}

}
