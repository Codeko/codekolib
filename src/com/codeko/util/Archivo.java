package com.codeko.util;

import com.codeko.util.https.InstaladorCertificados;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * Colección de funciones con las operaciones más comunes relacionadas con ficheros.
 * <p>
 * Estas funciones son puramente practicas por lo que son bastante flexibles en cuanto a parametros 
 * y generan el número menor de errores y excepciones. En la mayoría de los casos en vez de lanzar una 
 * excepción aplican un valor por defecto.
 * </p>
 * @author Boris Burgos García
 * @version 1.0
 * 12/08/2008
 */
public class Archivo {

    private static boolean ignorarErroresSSL = true;

    public static boolean isIgnorarErroresSSL() {
        return ignorarErroresSSL;
    }

    public static void setIgnorarErroresSSL(boolean ignorarErroresSSL) {
        Archivo.ignorarErroresSSL = ignorarErroresSSL;
    }

    /**
     * Devuelve el contenido de un archivo
     * @param archivo Archivo a leer
     * @param charset Codificación del archivo
     * @return Un String con el contenido del archivo
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     * @throws java.io.IOException
     */
    public static String getContenido(File archivo, String charset) throws IOException {
        return getContenido(archivo.toURI().toURL(), charset);
    }

    /**
     * Devuelve el contenido de un archivo identificado por una cadena. Este archivo puede ser una URL
     * @param archivo  Archivo o URL a leer
     * @param charset Codificación del archivo
     * @return Un String con el contenido del archivo
     * @throws java.io.IOException
     */
    public static String getContenido(String archivo, String charset) throws IOException {
        File f = new File(archivo);
        if (f.isFile() && f.exists()) {
            return getContenido(f, charset);
        } else {
            URL u = new URL(archivo);
            return getContenido(u, charset);
        }
    }

    /**
     * Devuelve el contenido de una url gestionando correctamente las conexiones HTTPS si existiesen
     * @param archivo URL a leer
     * @param charset Codificación del archivo
     * @return Un String con el contenido del archivo
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     * @throws java.io.IOException
     */
    public static String getContenido(URL archivo, String charset) throws IOException {
        InputStream fstream = null;
        if (archivo.getProtocol().equals("https")) {
            InstaladorCertificados.instalar(archivo);
            HttpsURLConnection httpsCon = (HttpsURLConnection) archivo.openConnection();
            if (isIgnorarErroresSSL()) {
                httpsCon.setHostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            }
            httpsCon.connect();
            fstream = httpsCon.getInputStream();
        } else {
            fstream = archivo.openStream();
        }
        String retorno = Str.leer(fstream, charset);
        fstream.close();
        return retorno;
    }

    /**
     * Almacena un String en un archivo
     * @param contenido String a almacenar
     * @param charset Charset del String
     * @param archivo Archivo donde se almacenará
     * @param append Boolean añadir. True añade el contenido al actual. False sustituye el contenido actual. 
     * @return True si todo se realiza con éxito. False si no.
     */
    public static boolean setContenido(String contenido, String charset, File archivo, boolean append) {
        boolean ok = false;
        OutputStreamWriter bos = null;
        try {
            bos = new OutputStreamWriter(new FileOutputStream(archivo, append), charset);
            bos.write(contenido);
            ok = true;
        } catch (Exception ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, "Error almacenando texto con codificación '" + charset + "' en '" + archivo + "'", ex);
        } finally {
            Obj.cerrar(bos);
        }
        return ok;
    }

    public static boolean copiarArchivo(InputStream origen, OutputStream destino) throws IOException {
        boolean ret = true;
        byte[] b = new byte[1024];
        int leido = -1;
        while ((leido = origen.read(b)) != -1) {
            destino.write(b, 0, leido);
        }
        return ret;
    }

    public static boolean copiarContenido(File origen, File destino) {
        boolean ret = true;
        File[] hijos = origen.listFiles();
        for (File f : hijos) {
            File nuevo = new File(destino, f.getName());
            if (f.isDirectory()) {
                //Creamos el directorio en distino
                nuevo.mkdirs();
                ret = copiarContenido(f, nuevo) && ret;
            } else {
                try {
                    copiarArchivo(f, nuevo);
                } catch (IOException ex) {
                    ret = false;
                    Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return ret;
    }

    /**
     * Copia un archivo, con soporte para archivos grandes y muy rápido.
     * @param origen Archivo de origen
     * @param destino Archivo o directorio de destino. Si es un directorio se usará el nombre del archivo original.
     * @return True si se produce la copia correctamente, false si no
     * @throws java.io.IOException
     */
    public static boolean copiarArchivo(File origen, File destino) throws IOException {
        boolean retorno = false;
        //Si es directorio cambiamos para que sea un archivo.
        if (destino.isDirectory()) {
            destino = new File(destino, origen.getName());
        }
        FileChannel inChannel = new FileInputStream(origen).getChannel();
        FileChannel outChannel = new FileOutputStream(destino).getChannel();
        try {
            int maxCount = getMaxCountTransferTo();
            long size = inChannel.size();
            long position = 0;
            while (position < size) {
                position += inChannel.transferTo(position, maxCount, outChannel);
            }
            retorno = true;
        } catch (IOException e) {
            throw e;
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
        return retorno;
    }

    private static int getMaxCountTransferTo() {
        //En windows no se pueden copair mas de 64 megas de golpe por lo que hay que hacerlo así
        //(64 * 1024 * 1024) - (32 * 1024);
        //Al final ese tamaño da problemas a veces
        //Según esta discusión es el tamaño debe ser este (http://x86.sun.com/thread.jspa?messageID=1981958)
        int maxCount = 67076032;
        return maxCount;
    }

    /**
     * Combina una serie de archivos en uno.
     * @param archivos Archivos a concatenar
     * @param destino Archivo resultante
     * @return true si el proceso se produce correctamente false si no.
     * @throws java.io.IOException
     */
    public static boolean concatenarArchivos(Collection<File> archivos, File destino) throws IOException {
        return concatenarArchivos((File[]) archivos.toArray(), destino);
    }

    /**
     * Combina una serie de archivos en uno. Admite que se introduzcan archivos nulos o no exitentes
     * @param archivos Archivos a concatenar
     * @param destino Archivo resultante
     * @return true si el proceso se produce correctamente false si no.
     * @throws java.io.IOException
     */
    public static boolean concatenarArchivos(File[] archivos, File destino) throws IOException {
        boolean retorno = false;
        FileOutputStream fos = new FileOutputStream(destino);
        FileChannel outChannel = fos.getChannel();
        FileChannel inChannel = null;
        FileInputStream fis = null;
        try {
            for (File origen : archivos) {
                if (origen != null && origen.exists() && origen.isFile()) {
                    fis = new FileInputStream(origen);
                    inChannel = fis.getChannel();
                    int maxCount = getMaxCountTransferTo();
                    long size = inChannel.size();
                    long position = 0;
                    while (position < size) {
                        position += inChannel.transferTo(position, maxCount, outChannel);
                    }
                    inChannel.close();
                    fis.close();
                }
            }
            outChannel.close();
            fos.close();
            retorno = true;
        } catch (IOException e) {
            throw e;
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
        return retorno;
    }

    /**
     * Añade un archivo al final de otro
     * @param archivo Archivos a concatenar
     * @param destino Archivo al que se le añadirá el anterior
     * @return true si el proceso se produce correctamente false si no.
     * @throws java.io.IOException
     */
    public static boolean concatenarArchivo(File archivo, File destino) throws IOException {
        boolean retorno = false;
        FileOutputStream fos = new FileOutputStream(destino, true);
        FileChannel outChannel = fos.getChannel();
        FileChannel inChannel = null;
        FileInputStream fis = null;
        try {
            if (archivo != null && archivo.exists()) {
                fis = new FileInputStream(archivo);
                inChannel = fis.getChannel();
                //En windows no se pueden copair mas de 64 megas de golpe por lo que hay que hacerlo así
                int maxCount = (64 * 1024 * 1024) - (32 * 1024);
                long size = inChannel.size();
                long position = 0;
                while (position < size) {
                    position += inChannel.transferTo(position, maxCount, outChannel);
                }
                inChannel.close();
                fis.close();
            }
            outChannel.close();
            fos.close();
            retorno = true;
        } catch (IOException e) {
            throw e;
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
        return retorno;
    }

    /**
     * Borra un directorio y todos su contenido (incluidos subdirectorios y su contenido si los hubiera).
     * Si falla el borrado de un elemento cancela el proceso y no continua.
     * @param dir Directorio a borrar
     * @return True si ha borrado correctamente todo, False si ha fallado el borrado de alguno de los archivos
     */
    public static boolean borrarDirectorio(File dir) {
        if (dir!=null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = borrarDirectorio(new File(dir, children[i]));
                if (!success) {
                    Logger.getLogger(Archivo.class.getName()).warning("No se ha podido borrar el archivo: " + children[i] + " del directorio " + dir.getAbsolutePath());
                    return false;
                }
            }
        }
        // Ahora borramos el directorio o archivo...
        return dir.delete();
    }

    /**
     * Descomprime un archivo zip en un directorio dado 
     * @param zip Archivo zip a descomprimir
     * @param destino Directorio de destino
     * @param sobreescribir True para sobreescribir archivos, false para no hacerlo
     * @return ArrayList<File> con los archivos descomprimidos
     */
    public static ArrayList<File> descomprimirZip(File zip, File destino, boolean sobreescribir) {
        ArrayList<File> archivos = new ArrayList<File>();
        try {
            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream(zip);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            int count;
            int total = 0;
            final int BUFFER_SIZE = 1024;
            byte data[] = new byte[BUFFER_SIZE];
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    File destFN = new File(destino, entry.getName());
                    destFN.getParentFile().mkdirs();
                    if (!destFN.exists() || (sobreescribir)) {
                        try {
                            Logger.getLogger(Archivo.class.getName()).fine("Descomprimiendo " + destFN.getAbsolutePath() + " proveniente de " + zip.getAbsolutePath() + " en " + destino.getAbsolutePath());
                            FileOutputStream fos = new FileOutputStream(destFN);
                            dest = new BufferedOutputStream(fos, BUFFER_SIZE);
                            while ((count = zis.read(data, 0, BUFFER_SIZE)) != -1) {
                                dest.write(data, 0, count);
                                total += count;
                                Logger.getLogger(Archivo.class.getName()).fine("Descomprimiendo " + destFN.getAbsolutePath() + " proveniente de " + zip.getAbsolutePath() + " en " + destino.getAbsolutePath() + " [" + total + "B]");
                            }
                            dest.flush();
                            dest.close();
                            archivos.add(destFN);
                        } catch (Exception ex) {
                            Logger.getLogger(Archivo.class.getName()).log(Level.WARNING, "Error descomprimiendo entrada " + entry + " de " + zip.getAbsolutePath() + " en " + destino.getAbsolutePath(), ex);
                        }
                    } else {
                        Logger.getLogger(Archivo.class.getName()).log(Level.WARNING, "El archivo " + destFN.getAbsolutePath() + " ya existía en " + destino.getAbsolutePath() + ". Ignorado.");
                        archivos.add(destFN);
                    }
                }
            }
            zis.close();
        } catch (Exception e) {
            Logger.getLogger(Archivo.class.getName()).log(Level.WARNING, "Error descomprimiendo archivo: " + zip.getAbsolutePath() + " en " + destino.getAbsolutePath(), e);
        }
        return archivos;
    }

    /**
     * Genera un archivo comprimido con los archivos indicados. Esta función comprime directorios recursivamente.
     * @param archivoZip Archivo a generar.
     * @param archivos Archivos a incluir en el archivo comprimido.
     */
    public static void comprimirZip(File archivoZip, File... archivos) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(archivoZip);
            ZipOutputStream out = new ZipOutputStream(fos);
            for (File f : archivos) {
                try {
                    addArchivoAComprimido(f, out, "");
                } catch (Exception e) {
                    Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            out.finish();
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private static void addArchivoAComprimido(File f, ZipOutputStream out, String rutaBase) throws FileNotFoundException, IOException {
        if (f.isDirectory()) {
            String nombreBase = rutaBase + f.getName() + "/";
            ZipEntry ze = new ZipEntry(nombreBase);
            out.putNextEntry(ze);
            for (File f2 : f.listFiles()) {
                addArchivoAComprimido(f2, out, nombreBase);
            }
        } else {
            ZipEntry ze = new ZipEntry(rutaBase + f.getName());
            out.putNextEntry(ze);
            FileInputStream fis = new FileInputStream(f);
            byte[] b = new byte[1024];
            int bn = fis.read(b);
            while (bn != -1) {
                out.write(b, 0, bn);
                bn = fis.read(b);
            }
            fis.close();
        }
    }

    public static String getChecksumMD5(String archivo) throws FileNotFoundException, IOException {
        InputStream fis = new FileInputStream(archivo);
        String csum = getChecksumMD5(fis);
        fis.close();
        return csum;
    }

    public static String getChecksumMD5(InputStream fis) throws IOException {
        byte[] buffer = new byte[1024*64];
        MessageDigest complete;
        try {
            complete = MessageDigest.getInstance("MD5");
            int numRead = fis.read(buffer);
            while (numRead != -1) {
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
                numRead = fis.read(buffer);
            }
            return Str.toHexString(complete.digest());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     * Extrae un recurso de una clase y lo guarda en un fichero.
     * @param claseRecurso Clase base desde la que se extraera el recurso.
     * @param recurso Ruta al recurso a extraer
     * @param salida Fichero donde se guardará el recurso
     * @param sobreEscribir True si se desea sobreescribir el fichero de salida si existe, false si no.
     * @return True si se ha realizado la extración con éxito, False si no.
     */
    public static boolean extraerRecurso(Class claseRecurso, String recurso, File salida, boolean sobreEscribir) {
        boolean ok = false;
        try {
            InputStream is = claseRecurso.getResourceAsStream(recurso);
            if (is != null) {
                FileOutputStream fos = null;
                try {
                    if (salida.exists()) {
                        if (sobreEscribir) {
                            salida.delete();
                        }
                    }
                    if (!salida.exists()) {
                        fos = new FileOutputStream(salida);
                        Archivo.copiarArchivo(is, fos);
                        ok = true;
                    }
                } catch (Exception ex) {
                    Logger.getLogger(claseRecurso.getName()).log(Level.SEVERE, "Error extrayendo " + recurso + " a " + salida, ex);
                } finally {
                    Obj.cerrar(is, fos);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(claseRecurso.getName()).log(Level.SEVERE, "Error extrayendo " + recurso + " a " + salida, e);
        }
        return ok;
    }

    public static void setUltimoArchivoUsado(Object aplicacion, File archivo) {
        if (archivo != null) {
            Preferences.userNodeForPackage(aplicacion.getClass()).put("cdk_ultimo_archivo", archivo.getAbsolutePath());
        } else {
            Preferences.userNodeForPackage(aplicacion.getClass()).remove("cdk_ultimo_archivo");
        }
    }

    public static File getUltimoArchivoUsado(Object aplicacion) {
        return new File(Preferences.userNodeForPackage(aplicacion.getClass()).get("cdk_ultimo_archivo", System.getProperty("user.dir")));
    }
}
