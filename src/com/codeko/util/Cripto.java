package com.codeko.util;

import com.codeko.util.StringEncrypter.EncryptionException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

/**

 * @author codeko
 * @version 0.2
 * 12/08/2008
 */
//TODO Esta clase hay que reorganizarla, crear los paquetes y lo que haga falta
public class Cripto {

    public static String md5(String cadena) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(cadena.getBytes());
            byte messageDigest[] = algorithm.digest();
            return Str.toHexString(messageDigest);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Cripto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String encriptar(String cadena) {
        try {
            return new StringEncrypter().encrypt(cadena);
        } catch (EncryptionException ex) {
            Logger.getLogger(Cripto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String encriptar(String cadena, String key) {
        try {
            return new StringEncrypter(StringEncrypter.DESEDE_ENCRYPTION_SCHEME, key).encrypt(cadena);
        } catch (EncryptionException ex) {
            Logger.getLogger(Cripto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String desencriptar(String cadena) {
        try {
            return new StringEncrypter().decrypt(cadena);
        } catch (EncryptionException ex) {
            Logger.getLogger(Cripto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String desencriptar(String cadena, String key) {
        try {
            return new StringEncrypter(StringEncrypter.DESEDE_ENCRYPTION_SCHEME, key).decrypt(cadena);
        } catch (EncryptionException ex) {
            Logger.getLogger(Cripto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }
}

class StringEncrypter {

    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    public static final String DES_ENCRYPTION_SCHEME = "DES";
    
    public static final String DEFAULT_ENCRYPTION_KEY = "Esta es la mas o menos larga frase por defecto usada para encriptar los datos";
    private KeySpec keySpec;
    private SecretKeyFactory keyFactory;
    private Cipher cipher;
    private static final String UNICODE_FORMAT = "UTF8";

    public StringEncrypter() throws EncryptionException {
        this(DESEDE_ENCRYPTION_SCHEME, DEFAULT_ENCRYPTION_KEY);
    }

    public StringEncrypter(String encryptionScheme) throws EncryptionException {
        this(encryptionScheme, DEFAULT_ENCRYPTION_KEY);
    }

    public StringEncrypter(String encryptionScheme, String encryptionKey)
            throws EncryptionException {

        if (encryptionKey == null) {
            throw new IllegalArgumentException("La clave de encriptación es nula");
        }
        if (encryptionKey.trim().length() < 24) {
            throw new IllegalArgumentException("La clave de encriptación tiene menos de 24 caracteres");
        }

        try {
            byte[] keyAsBytes = encryptionKey.getBytes(UNICODE_FORMAT);

            if (encryptionScheme.equals(DESEDE_ENCRYPTION_SCHEME)) {
                keySpec = new DESedeKeySpec(keyAsBytes);
            } else if (encryptionScheme.equals(DES_ENCRYPTION_SCHEME)) {
                keySpec = new DESKeySpec(keyAsBytes);
            } else {
                throw new IllegalArgumentException("Encryption scheme not supported: " + encryptionScheme);
            }

            keyFactory = SecretKeyFactory.getInstance(encryptionScheme);
            cipher = Cipher.getInstance(encryptionScheme);

        } catch (InvalidKeyException e) {
            throw new EncryptionException(e);
        } catch (UnsupportedEncodingException e) {
            throw new EncryptionException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException(e);
        } catch (NoSuchPaddingException e) {
            throw new EncryptionException(e);
        }

    }

    public String encrypt(String unencryptedString) throws EncryptionException {
        if (unencryptedString == null || unencryptedString.trim().length() == 0) {
            throw new IllegalArgumentException("unencrypted string was null or empty");
        }

        try {
            SecretKey key = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] ciphertext = cipher.doFinal(cleartext);


            return Cripto.byteArrayToHexString(ciphertext);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    public String decrypt(String encryptedString) throws EncryptionException {
        if (encryptedString == null || encryptedString.trim().length() <= 0) {
            throw new IllegalArgumentException("encrypted string was null or empty");
        }

        try {
            SecretKey key = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] cleartext = Cripto.hexStringToByteArray(encryptedString);
            byte[] ciphertext = cipher.doFinal(cleartext);

            return bytes2String(ciphertext);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    private static String bytes2String(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.append((char) bytes[i]);
        }
        return stringBuffer.toString();
    }

    public static class EncryptionException extends Exception {

        public EncryptionException(Throwable t) {
            super(t);
        }
    }
}

