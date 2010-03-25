package com.codeko.util.https;

import com.codeko.util.Str;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Copyright Codeko Informática 2008
 * www.codeko.com
 * @author Codeko
 * Instala automáticamente un el certificado ssl de la url provista para que se puedan realizar conexiones a esa url.
 */
public class InstaladorCertificados {

    private static CodekoKeyStore keyStore = null;

    public static CodekoKeyStore getKeyStore() {
        if (keyStore == null) {
            setKeyStore(new CodekoKeyStore());
        }
        return keyStore;
    }

    public static void setKeyStore(CodekoKeyStore keyStore) {
        InstaladorCertificados.keyStore = keyStore;
        keyStore.asignarComoSSLKeyStore();
    }

    public static void setKeyStore(File archivoKeyStore, String claveKeyStore) {
        setKeyStore(new CodekoKeyStore(archivoKeyStore, claveKeyStore));
    }

    public static boolean instalar(String host) {
        try {
            instalar(host, 443);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(InstaladorCertificados.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static boolean instalar(URL url) {
        try {
            int port = url.getPort();
            if (port < 0) {
                port = 443;
            }
            instalar(url.getHost(), port);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(InstaladorCertificados.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private static void instalar(String host, int port) throws Exception {
        char[] passphrase = getKeyStore().getClaveKeyStore().toCharArray();
        Logger.getLogger(InstaladorCertificados.class.getName()).info("Cargando KeyStore " + getKeyStore().getArchivoKeyStore() + "...");
        InputStream in = new FileInputStream(getKeyStore().getArchivoKeyStore());
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(in, passphrase);
        in.close();
        SSLContext context = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        TrustManager tm = new TrustManager(defaultTrustManager);
        context.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory factory = context.getSocketFactory();
        Logger.getLogger(InstaladorCertificados.class.getName()).info("Abriendo conexión a " + host + ":" + port + "...");
        SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
        socket.setSoTimeout(10000);
        try {
            Logger.getLogger(InstaladorCertificados.class.getName()).info("Iniciando saludo SSL...");
            socket.startHandshake();
            socket.close();
            Logger.getLogger(InstaladorCertificados.class.getName()).info("No hay errores el certificado ya está instalado.");
        } catch (SSLException e) {
            X509Certificate[] chain = tm.chain;
            if (chain == null) {
                Logger.getLogger(InstaladorCertificados.class.getName()).severe("No se puede obtener el anillo de certificados del servidor");
                return;
            }
            Logger.getLogger(InstaladorCertificados.class.getName()).info("El servidor ha enviado " + chain.length + " certificado(s)");
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            for (int i = 0; i < chain.length; i++) {
                X509Certificate cert = chain[i];
                Logger.getLogger(InstaladorCertificados.class.getName()).fine(" " + (i + 1) + " Subject " + cert.getSubjectDN());
                Logger.getLogger(InstaladorCertificados.class.getName()).fine("   Issuer  " + cert.getIssuerDN());
                sha1.update(cert.getEncoded());
                Logger.getLogger(InstaladorCertificados.class.getName()).fine("   sha1    " + Str.toHexString(sha1.digest()));
                md5.update(cert.getEncoded());
                Logger.getLogger(InstaladorCertificados.class.getName()).fine("   md5     " + Str.toHexString(md5.digest()));
            }
            int k = 0;//El certificado a coger
            X509Certificate cert = chain[k];
            String alias = host + "-" + (k + 1);
            ks.setCertificateEntry(alias, cert);
            OutputStream out = new FileOutputStream(getKeyStore().getArchivoKeyStore());
            ks.store(out, passphrase);
            out.close();
            Logger.getLogger(InstaladorCertificados.class.getName()).fine(cert.toString());
            Logger.getLogger(InstaladorCertificados.class.getName()).info("Certificado añadido al keystore '" + getKeyStore().getArchivoKeyStore().getName() + "' usando el alia '" + alias + "'");
        } catch (Exception e) {
            Logger.getLogger(InstaladorCertificados.class.getName()).log(Level.WARNING, "Error recuperando certificado", e);
        }
    }

    private static class TrustManager implements X509TrustManager {

        private final X509TrustManager tm;
        private X509Certificate[] chain;

        TrustManager(X509TrustManager tm) {
            this.tm = tm;
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            this.chain = chain;
            tm.checkServerTrusted(chain, authType);
        }
    }
}
