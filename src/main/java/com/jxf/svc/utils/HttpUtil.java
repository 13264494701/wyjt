package com.jxf.svc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUtil {
    private static TrustManager myX509TrustManager = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    public HttpUtil() {
    }

    public static String sendHttpsPOST(String url, String data) {
        String result = null;

        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init((KeyManager[]) null, new TrustManager[]{myX509TrustManager}, (SecureRandom) null);
            URL requestUrl = new URL(url);
            HttpsURLConnection httpsConn = (HttpsURLConnection) requestUrl.openConnection();
            httpsConn.setSSLSocketFactory(sslcontext.getSocketFactory());
            httpsConn.setRequestMethod("POST");
            httpsConn.setDoOutput(true);
            OutputStream out = httpsConn.getOutputStream();
            if (data != null) {
                out.write(data.getBytes("UTF-8"));
            }

            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(httpsConn.getInputStream()));
            int code = httpsConn.getResponseCode();
            if (200 == code) {
                for (String temp = in.readLine(); temp != null; temp = in.readLine()) {
                    if (result != null) {
                        result = result + temp;
                    } else {
                        result = temp;
                    }
                }
            }
        } catch (KeyManagementException var10) {
            var10.printStackTrace();
        } catch (NoSuchAlgorithmException var11) {
            var11.printStackTrace();
        } catch (MalformedURLException var12) {
            var12.printStackTrace();
        } catch (ProtocolException var13) {
            var13.printStackTrace();
        } catch (IOException var14) {
            var14.printStackTrace();
        }

        return result;
    }

    public static String sendHttpsGET(String url) {
        String result = null;

        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init((KeyManager[]) null, new TrustManager[]{myX509TrustManager}, (SecureRandom) null);
            URL requestUrl = new URL(url);
            HttpsURLConnection httpsConn = (HttpsURLConnection) requestUrl.openConnection();
            httpsConn.setSSLSocketFactory(sslcontext.getSocketFactory());
            httpsConn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(httpsConn.getInputStream()));
            int code = httpsConn.getResponseCode();
            if (200 == code) {
                for (String temp = in.readLine(); temp != null; temp = in.readLine()) {
                    if (result != null) {
                        result = result + temp;
                    } else {
                        result = temp;
                    }
                }
            }
        } catch (KeyManagementException var8) {
            var8.printStackTrace();
        } catch (NoSuchAlgorithmException var9) {
            var9.printStackTrace();
        } catch (MalformedURLException var10) {
            var10.printStackTrace();
        } catch (ProtocolException var11) {
            var11.printStackTrace();
        } catch (IOException var12) {
            var12.printStackTrace();
        }

        return result;
    }

}