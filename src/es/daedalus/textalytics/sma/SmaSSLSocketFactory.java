/**
 * SmaSSLSocketFactory.java
 * 
 * @version 1.0
 * @author zdepablo
 * @contact    -- http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  -- Copyright (c) 2013, DAEDALUS S.A. All rights reserved.
 */
package es.daedalus.textalytics.sma;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

/**
 * SmaSSLSocketFactory
 * 
 * A SocketFactory that accepts single-signed se4rver certificates 
 * Implemented for simplicity in distribution 
 * 
 * Register textalytics.com your keystore and use default SokecktFactory 
 * or your desired strategy or authentication. 
 * 
 * @author zdepablo
 *
 */
public class SmaSSLSocketFactory extends SSLSocketFactory {
	
	SSLContext sslContext;
	
	public SmaSSLSocketFactory(KeyStore truststore) throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
		super(truststore);
	
		TrustManager tm = new X509TrustManager() {			
			public X509Certificate[] getAcceptedIssuers() {return null;}
			
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
			
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
		};
		
		this.sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, new TrustManager[]{tm}, null);
	}

	@Override
	public Socket createSocket(Socket s, String host, int port,	boolean autoClose) throws IOException {
		return this.sslContext.getSocketFactory().createSocket(s, host, port, autoClose);
	}




}
