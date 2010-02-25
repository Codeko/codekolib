package com.codeko.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Codifica distintos origenes a base64
 * @author Codeko
 */
public class Base64 {

    private final static String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private static boolean cortarLineas = true;
    private static boolean saltosDeLineaLinux = false;
    private static int caracteresPorLinea = 76;

    public static int getCaracteresPorLinea() {
        return caracteresPorLinea;
    }

    public static void setCaracteresPorLinea(int caracteresPorLinea) {
        Base64.caracteresPorLinea = caracteresPorLinea;
    }

    public static boolean isCortarLineas() {
        return cortarLineas;
    }

    public static void setCortarLineas(boolean cortarLineas) {
        Base64.cortarLineas = cortarLineas;
    }

    public static boolean isSaltosDeLineaLinux() {
        return saltosDeLineaLinux;
    }

    public static void setSaltosDeLineaLinux(boolean saltosDeLineaLinux) {
        Base64.saltosDeLineaLinux = saltosDeLineaLinux;
    }

    public static void encode(InputStream is, OutputStream os, boolean cortarLineas, boolean saltosDeLineaLinux) throws IOException {
        encode(is, os, cortarLineas, saltosDeLineaLinux, getCaracteresPorLinea());
    }

    public static void encode(InputStream is, OutputStream os, boolean cortarLineas) throws IOException {
        encode(is, os, cortarLineas, isSaltosDeLineaLinux(), getCaracteresPorLinea());
    }

    public static void encode(InputStream is, OutputStream os) throws IOException {
        encode(is, os, isCortarLineas(), isSaltosDeLineaLinux(), getCaracteresPorLinea());
    }

    public static void encode(InputStream is, OutputStream os, boolean cortarLineas, boolean saltosDeLineaLinux, int caracteresPorLinea) throws IOException {
        int leidos = 0;
        int totalLeidos = 0;
        byte[] bytes = new byte[3];
        int moduloLinea = ((caracteresPorLinea * 3) / 4) - 1;
        while ((leidos = is.read(bytes)) != -1) {
            byte pos = 0;
            if (leidos > 0) {
                totalLeidos += leidos;
            }
            if (leidos == 3) {
                pos = (byte) ((bytes[0] >> 2) & 63);
                os.write(caracteres.charAt(pos));

                pos = (byte) (((bytes[0] & 3) << 4) + ((bytes[1] >> 4) & 15));
                os.write(caracteres.charAt(pos));

                pos = (byte) (((bytes[1] & 15) << 2) + ((bytes[2] >> 6) & 3));
                os.write(caracteres.charAt(pos));

                pos = (byte) (((bytes[2]) & 63));
                os.write(caracteres.charAt(pos));
                // Añade una nueva linea cada X caracteres 
                // X*3/4 = 57
                if (((totalLeidos + 2) % moduloLinea) == 0) {
                    if (saltosDeLineaLinux) {
                        os.write('\n');
                    } else {
                        os.write('\r');
                        os.write('\n');
                    }
                }
            } else {
                switch (leidos) {
                    case 1:
                        pos = (byte) ((bytes[0] >> 2) & 63);
                        os.write(caracteres.charAt(pos));
                        pos = (byte) ((bytes[0] & 3) << 4);
                        os.write(caracteres.charAt(pos));
                        os.write('=');
                        os.write('=');
                        break;
                    case 2:
                        pos = (byte) ((bytes[0] >> 2) & 63);
                        os.write(caracteres.charAt(pos));
                        pos = (byte) (((bytes[0] & 3) << 4) + ((bytes[1] >> 4) & 15));
                        os.write(caracteres.charAt(pos));
                        pos = (byte) ((bytes[1] & 15) << 2);
                        os.write(caracteres.charAt(pos));
                        os.write('=');
                        break;
                }
            }
        }
    }

    public static String encode(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            encode(bais, out, isCortarLineas(), isSaltosDeLineaLinux(), getCaracteresPorLinea());
        } catch (IOException ex) {
            Logger.getLogger(Base64.class.getName()).log(Level.SEVERE, "Error accediendo a I/O", ex);
        }
        return out.toString();
    }

    /**
     * Codifica un array de bytes
     * 
     * @param bytes Array de bytes para ser codificado. 
     * @return Cadena codificada. 
     */
//    public static String encode(byte[] bytes) {
//        StringBuffer tmp = new StringBuffer();
//        int i = 0;
//        byte pos;
//        for (i = 0; i < (bytes.length - bytes.length % 3); i += 3) {
//            pos = (byte) ((bytes[i] >> 2) & 63);
//            tmp.append(caracteres.charAt(pos));
//
//            pos = (byte) (((bytes[i] & 3) << 4) + ((bytes[i + 1] >> 4) & 15));
//            tmp.append(caracteres.charAt(pos));
//
//            pos = (byte) (((bytes[i + 1] & 15) << 2) + ((bytes[i + 2] >> 6) & 3));
//            tmp.append(caracteres.charAt(pos));
//
//            pos = (byte) (((bytes[i + 2]) & 63));
//            tmp.append(caracteres.charAt(pos));
//
//            // Añade una nueva linea cada 76 caracteres 
//            // 76*3/4 = 57
//            if (((i + 2) % 56) == 0) {
//                tmp.append("\r\n");
//            }
//        }
//
//        if (bytes.length % 3 != 0) {
//            if (bytes.length % 3 == 2) {
//                pos = (byte) ((bytes[i] >> 2) & 63);
//                tmp.append(caracteres.charAt(pos));
//                pos = (byte) (((bytes[i] & 3) << 4) + ((bytes[i + 1] >> 4) & 15));
//                tmp.append(caracteres.charAt(pos));
//                pos = (byte) ((bytes[i + 1] & 15) << 2);
//                tmp.append(caracteres.charAt(pos));
//                tmp.append("=");
//            } else if (bytes.length % 3 == 1) {
//                pos = (byte) ((bytes[i] >> 2) & 63);
//                tmp.append(caracteres.charAt(pos));
//                pos = (byte) ((bytes[i] & 3) << 4);
//                tmp.append(caracteres.charAt(pos));
//                tmp.append("==");
//            }
//        }
//        return tmp.toString();
//    }
    /**
     * Codifica una cadena de texto. 
     * 
     * @param src Cadena de texto a ser codificada a base64. 
     * @return Cadena de texto codificada. 
     */
    public static String encode(String src) {
        return encode(src.getBytes());
    }

    /**
     * Decodifica una cadena de texto en base64
     * @param src Cadena de texto en base64
     * @return Array de bytes decodificados
     * @throws java.lang.Exception
     */
    public static byte[] decode(String src) throws Exception {
        byte[] bytes = null;
        StringBuffer buf = new StringBuffer(src);
        // primero se eliminan todos los caracteres vacios (\r\n, \t, " ");
        int i = 0;
        char c = ' ';
        char oc = ' ';
        while (i < buf.length()) {
            oc = c;
            c = buf.charAt(i);
            if (oc == '\r' && c == '\n') {
                buf.deleteCharAt(i);
                buf.deleteCharAt(i - 1);
                i -= 2;
            } else if (c == '\t') {
                buf.deleteCharAt(i);
                i--;
            } else if (c == ' ') {
                i--;
            }
            i++;
        }

        //La codificación dede consistir en grupos de cuatro bytes.  
        if (buf.length() % 4 != 0) {
            throw new Exception("Tamaño incorrecto de cadena en base64. El tamaño es :" + buf.length() + " y debería ser divisible entre 4.");
        }

        // Asignamos el tamaño del array decodificado
        bytes = new byte[3 * (buf.length() / 4)];
        int index = 0;
        // Y vamos decodificando cada grupo
        for (i = 0; i < buf.length(); i += 4) {
            byte data = 0;
            int nGroup = 0;
            for (int j = 0; j < 4; j++) {
                char theChar = buf.charAt(i + j);
                if (theChar == '=') {
                    data = 0;
                } else {
                    data = getIndiceTablaCaracteres(theChar);
                }

                if (data == -1) {
                    throw new Exception("Caracter '" + theChar + "' no es un caracter válido para una cadena en base64");
                }

                nGroup = 64 * nGroup + data;
            }

            bytes[index] = (byte) (255 & (nGroup >> 16));
            index++;

            bytes[index] = (byte) (255 & (nGroup >> 8));
            index++;

            bytes[index] = (byte) (255 & (nGroup));
            index++;
        }

        byte[] newBytes = new byte[index];
        for (i = 0; i < index; i++) {
            newBytes[i] = bytes[i];
        }
        return newBytes;
    }

    
    
    /**
     * Recupera la posición de un caracter en la tabla de caracteres
     * @param c Caracter
     * @return byte posición del caracter
     */
    protected static byte getIndiceTablaCaracteres(char c) {
        byte index = -1;
        for (byte i = 0; i < caracteres.length(); i++) {
            if (caracteres.charAt(i) == c) {
                index = i;
                break;
            }
        }
        return index;
    }
}