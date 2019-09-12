package de.danielr1996.banking.infrastructure.spring;

import okhttp3.OkHttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;

import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;

@Configuration
public class SSlConfig {
  @Bean
  @Primary
  public RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
    SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build());

    CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();

    RestTemplate template = new RestTemplate();
    ((HttpComponentsClientHttpRequestFactory) template.getRequestFactory()).setHttpClient(httpClient);
    return template;
  }


  private static SSLContext getSSlContext() {


    final TrustManager[] trustAllCerts = new TrustManager[]{getTrustManager()};

    SSLContext sslContext = null;
    try {

      sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      e.printStackTrace();
    }


    return sslContext;

  }

  private static X509TrustManager getTrustManager() {

    final X509TrustManager trustManager = new X509TrustManager() {

      @Override
      public X509Certificate[] getAcceptedIssuers() {
        X509Certificate[] cArrr = new X509Certificate[0];
        return cArrr;
      }

      @Override
      public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // TODO Auto-generated method stub

      }

      @Override
      public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // TODO Auto-generated method stub

      }
    };

    return trustManager;
  }

  private static HostnameVerifier gethostnameVerifier() {

    HostnameVerifier hostnameVerifier = new HostnameVerifier() {

      @Override
      public boolean verify(String arg0, SSLSession arg1) {
        return true;
      }
    };

    return hostnameVerifier;

  }
}
