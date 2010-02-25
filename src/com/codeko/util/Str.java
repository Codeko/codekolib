package com.codeko.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

/**
 * Colección de funciones con las operaciones más comunes relacionadas con cadenas.
 * <p>
 * Estas funciones son puramente practicas por lo que son bastante flexibles en cuanto a parametros 
 * y generan el número menor de errores y excepciones. En la mayoría de los casos en vez de lanzar una 
 * excepción aplican un valor por defecto.
 * </p>
 * @author Boris Burgos García
 * @version 1.0
 */
public class Str {

    /**
     * Devuelve una cadena rellena con espacios a la izquierda
     * @param cadena Objecto que se convertira a String y será la cadena
     * @param tamano Tamaño de la cadena una vez rellena de espacios a la izquierda
     * @return Cadena rellena con espacios a la izquierda
     */
    public static String lPad(Object cadena, int tamano) {
        return lPad(cadena, tamano, ' ');
    }

    /**
     * Devuelve una cadena rellena con un caracter a la izquierda
     * @param oCadena Objecto que se convertira a String y será la cadena
     * @param tamano Tamaño de la cadena una vez rellena de espacios a la izquierda
     * @param relleno Caracter de relleno
     * @return Cadena rellena con un caracter a la izquierda
     */
    public static String lPad(Object oCadena, int tamano, char relleno) {
        if (oCadena == null) {
            oCadena = "";
        }
        String cadena = oCadena.toString();
        if (cadena.length() < tamano) {
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < tamano - cadena.length(); i++) {
                sb.append(relleno);
            }
            sb.append(cadena);
            cadena = sb.toString();
        }
        return cadena;
    }

    /**
     * Devuelve una cadena rellena con espacios a la derecha
     * @param cadena Objecto que se convertira a String y será la cadena
     * @param tamano Tamaño de la cadena una vez rellena de espacios a la derecha
     * @return Cadena rellena con espacios a la derecha
     */
    public static String rPad(Object cadena, int tamano) {
        return rPad(cadena, tamano, ' ');
    }

    /**
     * Devuelve una cadena rellena con un caracter a la derecha
     * @param oCadena Objecto que se convertira a String y será la cadena
     * @param tamano Tamaño de la cadena una vez rellena de espacios a la derecha
     * @param relleno Caracter de relleno
     * @return Cadena rellena con un caracter a la derecha
     */
    public static String rPad(Object oCadena, int tamano, char relleno) {
        if (oCadena == null) {
            oCadena = "";
        }
        String cadena = oCadena.toString();
        if (cadena.length() < tamano) {
            StringBuilder sb = new StringBuilder(cadena);
            for (int i = 0; i < tamano - cadena.length(); i++) {
                sb.append(relleno);
            }
            cadena = sb.toString();
        }
        return cadena;
    }

    /**
     * Convierte un objeto a cadena de texto, si el objeto es nulo lo convierte a cadena vacia no a "null"
     * @param obj Objeto a convertir a cadena de texto
     * @return String 
     */
    public static String noNulo(Object obj) {
        String ret = "";
        if (obj != null) {
            ret = obj.toString();
        }
        return ret;
    }

    /**
     * Repite una cadena un número determinado de veces.
     * Si repeticiones es menor que 0 se considerará 0.
     * Si la cadena es nula se la considera cadena vacia 
     * @param cadena Cadena a repetir
     * @param repeticiones Número de veces que se repetirá la cadena
     * @return La cadena repetida tantas veces como 'repeticiones'
     */
    public static String repetir(String cadena, int repeticiones) {
        return repetir(cadena, repeticiones, "");
    }

    /**
     * Repite una cadena un número determinado de veces usando el separador indicado.
     * Si repeticiones es menor que 0 se considerará 0.
     * Si la cadena es nula se la considera cadena vacia.
     * @param cadena Cadena a repetir
     * @param repeticiones Número de repeticiones
     * @param separador Separador usado entre las repeticiones de la cadena
     * @return String cadena repetida usando el separador indicado
     */
    public static String repetir(String cadena, int repeticiones, String separador) {
        cadena = noNulo(cadena);
        StringBuilder sb = new StringBuilder();
        if (repeticiones < 0) {
            repeticiones = 0;
        }
        for (int i = 0; i < repeticiones; i++) {
            if (i != 0) {
                sb.append(separador);
            }
            sb.append(cadena);
        }
        return sb.toString();
    }

    /**
     * Genera una cadena con los elementos de un collection separándolos con un separador
     * @param datos Colección de datos
     * @param separador Separador a usar
     * @return Cadena resultante
     */
    /**
     * Genera una cadena con los elementos de un collection separándolos con un separador
     * @param datos Colección de datos
     * @param separador Separador a usar
     * @return Cadena resultante
     */
    public static String implode(Collection datos, String separador) {
        return implode(datos, separador, "");
    }

    /**
     * Genera una cadena con los elementos de un collection separándolos con un separador
     * @param datos Colección de datos
     * @param separador Separador a usar
     * @param valorVacio Valor que debe devolver cuando el array está vacio
     * @return Cadena resultante
     */
    public static String implode(Collection datos, String separador, String valorVacio) {
        StringBuilder sb = new StringBuilder();
        if (datos.size() > 0) {
            boolean primero = true;
            for (Object obj : datos) {
                if (primero) {
                    primero = false;
                } else {
                    sb.append(separador);
                }
                sb.append(obj);
            }
        } else {
            sb.append(valorVacio);
        }
        return sb.toString();
    }

    /**
     * Lee el contenido de un inputstream y lo devuelve como cadena de texto
     * @param is InputStream a leer. La función cierra el stream tras su uso.
     * @param charset Codificación de caracteres del InputStream
     * @return String con el contenido del InputStream
     * @throws java.io.IOException Si se produce algun error de lectura.
     */
    public static String leer(InputStream is, String charset) throws IOException {
        StringBuilder contenido = new StringBuilder();
        DataInputStream in = new DataInputStream(is);
        BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));
        char[] buff = new char[1024];
        int leido = -1;
        while ((leido = br.read(buff)) != -1) {
            contenido.append(buff, 0, leido);
        }
        in.close();
        is.close();
        return contenido.toString();
    }

    public static String toHexString(byte[] bytes) {
        final char[] HEXDIGITS = "0123456789abcdef".toCharArray();
        StringBuilder sb = new StringBuilder(bytes.length * 3);
        for (int b : bytes) {
            b &= 0xff;
            sb.append(HEXDIGITS[b >> 4]);
            sb.append(HEXDIGITS[b & 15]);
        }
        /**
         * Probar las dos implementaciones y ver cual es la mas rapida
         * StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
        hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
        }
         */
        return sb.toString();
    }

    public static String comprimirDeflate(String cadena) {
        byte[] in = cadena.getBytes();
        Deflater compresser = new Deflater();
        compresser.setInput(in);
        compresser.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(in.length);
        // Compress the data
        byte[] buf = new byte[1024];
        while (!compresser.finished()) {
            int count = compresser.deflate(buf);
            bos.write(buf, 0, count);
        }
        try {
            bos.close();
        } catch (IOException e) {
        }

        // Get the compressed data
        byte[] compressedData = bos.toByteArray();
        return new String(compressedData);
    }

    public static String toStr(InputStream is, String charset) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
        StringBuilder sb = new StringBuilder();
        try {
            char[] buff = new char[1024];
            int leido = reader.read(buff);
            while (leido > -1) {
                sb.append(buff, 0, leido);
                leido = reader.read(buff);
            }
        } catch (Exception e) {
            Logger.getLogger(Str.class.getName()).log(Level.SEVERE, "Error leyendo InputStream", e);
        }
        Obj.cerrar(is);
        return sb.toString();
    }

    public static String comprimirGZ(String cadena) {
        String ret = cadena;
        ByteArrayOutputStream byos = new ByteArrayOutputStream();
        GZIPOutputStream out = null;
        try {
            out = new GZIPOutputStream(byos);
            out.write(cadena.getBytes(), 0, cadena.length());
            out.finish();
            out.close();
            ret = byos.toString();
            byos.close();
        } catch (IOException ex) {
            Logger.getLogger(Str.class.getName()).log(Level.SEVERE, null, ex);
        }
        Obj.cerrar(out, byos);
        return ret;
    }
}
