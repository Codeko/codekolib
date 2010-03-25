package com.codeko.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Codifica distintos origenes de datos a base64
 * @author Codeko
 */
public class Base64 {

    private final static String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private static boolean saltosDeLineaLinux = false;
    private static int caracteresPorLinea = 76;

    /**
     * Devuelve el número de caracteres por linea que se permitirán en la versión codificada en base64.
     * Por defecto son 76 caracteres por linea.
     * @return int con el número de caracteres por linea
     */
    public static int getCaracteresPorLinea() {
        return caracteresPorLinea;
    }

    /**
     * Asigna el  número de caracteres por linea que se permitirán en la versión codificada en base64.
     * Por defecto son 76 caracteres por linea.
     * @param caracteresPorLinea int con el número de caracteres por linea deseados.
     */
    public static void setCaracteresPorLinea(int caracteresPorLinea) {
        Base64.caracteresPorLinea = caracteresPorLinea;
    }

    /**
     * Devuelve si se usarán saltos de linea linux (\n) en vez de saltos de linea de windows (\r\n).
     * Por defecto se devuelven saltos de linea de windows.
     * @return boolean true si se usarán saltos de linea linux o false si serán saltos de linea windows
     */
    public static boolean isSaltosDeLineaLinux() {
        return saltosDeLineaLinux;
    }

    /**
     * Asigna si se usarán saltos de linea linux (\n) en vez de saltos de linea de windows (\r\n).
     * Por defecto se usan saltos de linea de windows.
     * @param saltosDeLineaLinux boolean true si se usarán saltos de linea linux o false si serán saltos de linea windows
     */
    public static void setSaltosDeLineaLinux(boolean saltosDeLineaLinux) {
        Base64.saltosDeLineaLinux = saltosDeLineaLinux;
    }

    /**
     * Escribe un InputStream en el OutputStream indicado asignado los saltos de linea indicados y el número de caracteres por linea por defecto.
     * @param is InputStream desde el que se leeran los datos
     * @param os OutputStream donde se escribirán los datos codificados en base64
     * @param saltosDeLineaLinux boolean true si se deben usar saltos de linea linux (\n) false si se deben usar los saltos de linea windows (\r\n)
     * @throws IOException Lanzada si se produce algún problema leyendo del InputStream o escribiendo en el OutputStream
     * @see Base64#setCaracteresPorLinea(int) 
     */
    public static void encode(InputStream is, OutputStream os, boolean saltosDeLineaLinux) throws IOException {
        encode(is, os, saltosDeLineaLinux, getCaracteresPorLinea());
    }

    /**
     * Escribe un InputStream en el OutputStream indicado asignando los saltos de linea y número de caracteres por linea por defecto.
     * @param is InputStream desde el que se leeran los datos
     * @param os OutputStream donde se escribirán los datos codificados en base64
     * @throws IOException Lanzada si se produce algún problema leyendo del InputStream o escribiendo en el OutputStream
     * @see Base64#setSaltosDeLineaLinux(boolean)
     * @see Base64#setCaracteresPorLinea(int)
     */
    public static void encode(InputStream is, OutputStream os) throws IOException {
        encode(is, os, isSaltosDeLineaLinux(), getCaracteresPorLinea());
    }

    /**
     * Escribe los datos proporcionados por un InputStream en un OutputStream usando el número de caracteres por linea y el salto de linea indicados.
     * @param is InputStream desde el que se leeran los datos
     * @param os OutputStream donde se escribirán los datos codificados en base64
     * @param saltosDeLineaLinux boolean true si se deben usar saltos de linea linux (\n) false si se deben usar los saltos de linea windows (\r\n)
     * @param caracteresPorLinea Número de caracteres por linea que se escribiran.
     * @throws IOException Lanzada si se produce algún problema leyendo del InputStream o escribiendo en el OutputStream
     */
    public static void encode(InputStream is, OutputStream os, boolean saltosDeLineaLinux, int caracteresPorLinea) throws IOException {
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

    /**
     * Codifica un array de bytes en base64 y lo devuelve como String.<br/>
     * Usa tanto los saltos de linea como el número de caracteres por linea por defecto
     * @param bytes Array de bytes a codificar.
     * @return String con el array de bytes codificado en base64.
     * @see Base64#setSaltosDeLineaLinux(boolean)
     * @see Base64#setCaracteresPorLinea(int)
     */
    public static String encode(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            encode(bais, out, isSaltosDeLineaLinux(), getCaracteresPorLinea());
        } catch (IOException ex) {
            Logger.getLogger(Base64.class.getName()).log(Level.SEVERE, "Error accediendo a I/O", ex);
        }
        return out.toString();
    }

    /**
     * Codifica una cadena de texto en base64. <br/>
     * Usa tanto los saltos de linea como el número de caracteres por linea por defecto.
     * @param src Cadena de texto a ser codificada a base64. 
     * @return String Cadena de texto codificada en base64.
     * @see Base64#setSaltosDeLineaLinux(boolean)
     * @see Base64#setCaracteresPorLinea(int)
     */
    public static String encode(String src) {
        return encode(src.getBytes());
    }

    /**
     * Decodifica una cadena de texto en base64
     * @param src Cadena de texto en base64
     * @return Array de bytes decodificados
     * @throws java.lang.Exception Si se produce alguna excepción en el proceso (datos inválidos...).
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
