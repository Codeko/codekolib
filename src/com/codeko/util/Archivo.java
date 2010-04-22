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
 * Colección de funciones con las operaciones más comunes relacionadas con ficheros así como con archivos zip.
 * <p>
 * Estas funciones son puramente practicas por lo que son bastante flexibles en cuanto a parametros 
 * y generan el número menor de errores y excepciones. En la mayoría de los casos en vez de lanzar una 
 * excepción aplican un valor por defecto.
 * </p>
 * <p>Todas las funciones que necesitan la definición de un charset tiene dos variantes una con el charset como parámetro y otra sin él.
 * Aquellas que no reciben el charset como parámetro usan el charset por defecto asignado por setDefaultCharset(String defaultCharset).
 * El charset por defecto es 'UTF-8'.
 * </p>
 * Esta clase hace uso de java.nio.channels.FileChannel para la mayoría de las
 * operaciones con archivos garantizando una alta velocidad en las operaciones y la capacidad para trabajar con archivos muy grandes.
 * @author Boris Burgos García
 * @version 1.0
 * 12/08/2008
 */
public class Archivo {

    private static boolean ignorarErroresSSL = true;
    private static String defaultCharset = "UTF-8";
    private static final int TAM_BUFFER = 1024;

    /**
     * Devuelve el charset por defecto asignado a todas las operaciones de esta clase en las que no se defina.
     * @return String con el charset por defecto.
     */
    public static String getDefaultCharset() {
        return defaultCharset;
    }

    /**
     * Asigna el charset por defecto para todas las operaciones de esta clase en las que no se defina.
     * @param defaultCharset String con el nuevo charset por defecto.
     */
    public static void setDefaultCharset(String defaultCharset) {
        Archivo.defaultCharset = defaultCharset;
    }

    /**
     * Devuelve si la clase está configurada para ignorar los errores SSL provenientes de recuperación de contenidos desde URLs.
     * Por defecto la clase ignora los errores ssl.
     * @return boolean Si la clase está configurada para ignorar los errores ssl o no.
     * @see Archivo#getContenido(java.net.URL, java.lang.String) 
     */
    public static boolean isIgnorarErroresSSL() {
        return ignorarErroresSSL;
    }

    /**
     * Asigna si la clase debe ignorar los errores SSL provenientes de recuperación de contenidos desde URLs.
     * Por defecto la clase ignora los errores ssl.
     * @param ignorarErroresSSL Boolean true para ignorar los errores SSL false para no hacerlo.
     * @see Archivo#getContenido(java.net.URL, java.lang.String) 
     */
    public static void setIgnorarErroresSSL(boolean ignorarErroresSSL) {
        Archivo.ignorarErroresSSL = ignorarErroresSSL;
    }

    private Archivo() {
    }

    /**
     * Recupera el contenido de un archivo como String.
     * @param archivo Archivo a leer
     * @param charset Codificación del archivo
     * @return String con el contenido del archivo indicado
     * @throws java.io.IOException Si se encuentra algún error accediendo al archivo.
     */
    public static String getContenido(File archivo, String charset) throws IOException {
        return getContenido(archivo.toURI().toURL(), charset);
    }

    /**
     * Recupera el contenido de un archivo como String usando el charset por defecto.
     * @param archivo Archivo a leer
     * @return String con el contenido del archivo indicado
     * @throws java.io.IOException Si se encuentra algún error accediendo al archivo.
     * @see Archivo#setDefaultCharset(java.lang.String) 
     */
    public static String getContenido(File archivo) throws IOException {
        return getContenido(archivo.toURI().toURL(), getDefaultCharset());
    }

    /**
     * Devuelve el contenido de un archivo identificado por una cadena. Esta cadena puede ser una URL.
     * @param archivo String con el Archivo o URL a leer
     * @param charset Codificación del archivo
     * @return String con el contenido del archivo
     * @throws java.io.IOException Si se encuentra algún error accediendo al archivo.
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
     * Devuelve el contenido de un archivo identificado por una cadena usando el charset por defecto. Esta cadena puede ser una URL.
     * @param archivo String con el Archivo o URL a leer
     * @return String con el contenido del archivo
     * @throws java.io.IOException Si se encuentra algún error accediendo al archivo.
     * @see Archivo#setDefaultCharset(java.lang.String) 
     */
    public static String getContenido(String archivo) throws IOException {
        return getContenido(archivo, getDefaultCharset());
    }

    /**
     * Devuelve el contenido de una url usando el charset por defecto.
     * Si la urls es HTTPS la función se encarga de isntalar y autorizar el ceritificado automáticamente mediante la clase InstaladorCertificados.<br/>
     * Esta función hace uso de la configuración de isIgnorarErroresSSL() para ignorar o no los errores de validación SSL.
     * @param archivo URL a leer
     * @return String con el contenido del archivo
     * @throws java.io.IOException Si se encuentra algún error accediendo a la URL
     * @see InstaladorCertificados
     * @see Archivo#isIgnorarErroresSSL()
     * @see Archivo#setDefaultCharset(java.lang.String) 
     */
    public static String getContenido(URL archivo) throws IOException {
        return getContenido(archivo, getDefaultCharset());
    }

    /**
     * Devuelve el contenido de una url. 
     * Si la urls es HTTPS la función se encarga de isntalar y autorizar el ceritificado automáticamente mediante la clase InstaladorCertificados.<br/>
     * Esta función hace uso de la configuración de isIgnorarErroresSSL() para ignorar o no los errores de validación SSL.
     * @param archivo URL a leer
     * @param charset Codificación del archivo
     * @return String con el contenido del archivo
     * @throws java.io.IOException Si se encuentra algún error accediendo a la URL
     * @see InstaladorCertificados
     * @see Archivo#isIgnorarErroresSSL() 
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
     * Almacena un String en un archivo usando el charset por defecto.
     * @param contenido String a almacenar en el archivo indicado
     * @param archivo Archivo donde se almacenará la cadena.
     * @param append Boolean True añade el contenido al actual. False sustituye el contenido actual.
     * @return True si todo se realiza con éxito. False si no.
     * @see Archivo#setDefaultCharset(java.lang.String) 
     */
    public static boolean setContenido(String contenido, File archivo, boolean append) {
        return setContenido(contenido, getDefaultCharset(), archivo, append);
    }

    /**
     * Almacena un String en un archivo
     * @param contenido String a almacenar en el archivo indicado
     * @param charset Charset del String
     * @param archivo Archivo donde se almacenará la cadena.
     * @param append Boolean True añade el contenido al actual. False sustituye el contenido actual. 
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

    /**
     * Copia el contenido de un InputStream en un OutputStream. Usa por defecto un buffer de 1024 bytes.
     * @param origen InputStream de origen de los datos.
     * @param destino OutputStream de destino de los datos.
     * @return boolean True si la operación se realiza con éxito, false si no.
     * @throws IOException Si ocurre algún error copiando el contenido.
     */
    public static boolean copiarArchivo(InputStream origen, OutputStream destino) throws IOException {
        return copiarArchivo(origen, destino, TAM_BUFFER);
    }

    /**
     * Copia el contenido de un InputStream en un OutputStream. Usa por defecto un buffer de 1024 bytes.
     * @param origen InputStream de origen de los datos.
     * @param destino OutputStream de destino de los datos.
     * @param tamBuffer int Tamaño del buffer de bytes usado para copiar el contenido.
     * @return boolean True si la operación se realiza con éxito, false si no.
     * @throws IOException Si ocurre algún error copiando el contenido.
     */
    public static boolean copiarArchivo(InputStream origen, OutputStream destino, int tamBuffer) throws IOException {
        boolean ret = true;
        byte[] b = new byte[1024];
        int leido = -1;
        while ((leido = origen.read(b)) != -1) {
            destino.write(b, 0, leido);
        }
        return ret;
    }

    /**
     * Copia el contenido del directorio origen al directorio destino.<br/>
     * No es necesario que el directorio destino exista ya que se crea automáticamente.<br/>
     * Esta función copia todos los archivos y directorios recursivamente.
     * Esta función hace uso de la función copiarArchivo(File origen, File destino) preparada para copiar archivos grandes a mayor velocidad.
     * @param origen File de origen. Debe existir y ser un directorio.
     * @param destino File de destino. No es necesario que exista.
     * @return boolean True si se realiza la operación existosamente false si no. Si se produce algún error copiando algún archivo o directorio el proceso continuará con el resto de archivos aunque la función devolverá false.
     * @see Archivo#copiarArchivo(java.io.File, java.io.File) 
     */
    public static boolean copiarContenido(File origen, File destino) {
        boolean ret = origen != null && origen.isDirectory() && origen.exists() && destino != null;
        if (ret) {
            File[] hijos = origen.listFiles();
            for (File f : hijos) {
                File nuevo = new File(destino, f.getName());
                if (f.isDirectory()) {
                    //Creamos el directorio en el destino
                    nuevo.mkdirs();
                    ret = copiarContenido(f, nuevo) && ret;
                } else {
                    try {
                        copiarArchivo(f, nuevo);
                    } catch (IOException ex) {
                        ret = false;
                        Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, "Error copiando contenido de " + origen.getAbsolutePath() + " a " + destino.getAbsolutePath(), ex);
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Copia un archivo, con soporte para archivos grandes y muy rápido.<br/>
     * Esta función hace uso de java.nio.channels.FileChannel para la transferencia de contenidos.
     * @param origen Archivo de origen
     * @param destino Archivo o directorio de destino. Si es un directorio se usará el nombre del archivo original.
     * @return True si se produce la copia correctamente, false si no
     * @throws java.io.IOException si se produce algún error copiando el archivo.
     * @see java.nio.channels.FileChannel
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

    /**
     * Define el tamaño máximo de las transferencias usando FileChannel
     * @return int con el tamaño máximo
     */
    private static int getMaxCountTransferTo() {
        //En windows no se pueden copair mas de 64 megas de golpe por lo que hay que hacerlo así
        //(64 * 1024 * 1024) - (32 * 1024);
        //Al final ese tamaño da problemas a veces
        //Según esta discusión es el tamaño debe ser este (http://x86.sun.com/thread.jspa?messageID=1981958)
        int maxCount = 67076032;
        return maxCount;
    }

    /**
     * Combina una serie de archivos en uno. Admite que se introduzcan archivos nulos o no exitentes ignorándolos.<br/>
     * Esta función hace uso de java.nio.channels.FileChannel para la transferencia de contenidos.
     * @param archivos Collection<File> de archivos a concatenar
     * @param destino File resultante
     * @return boolean true si el proceso se produce correctamente false si no.
     * @throws java.io.IOException si se produce algún error en la concatenación.
     * @see Archivo#concatenarArchivos(java.io.File[], java.io.File)
     * @see java.nio.channels.FileChannel
     */
    public static boolean concatenarArchivos(Collection<File> archivos, File destino) throws IOException {
        return concatenarArchivos((File[]) archivos.toArray(), destino);
    }

    /**
     * Combina una serie de archivos en uno. Admite que se introduzcan archivos nulos o no exitentes.<br/>
     * Esta función hace uso de java.nio.channels.FileChannel para la transferencia de contenidos.
     * @param archivos Archivos a concatenar
     * @param destino Archivo resultante
     * @return true si el proceso se produce correctamente false si no.
     * @throws java.io.IOException si se produce algún error en la concatenación.
     * @see java.nio.channels.FileChannel
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
     * Añade un archivo al final de otro.<br/>
     * Esta función hace uso de java.nio.channels.FileChannel para la transferencia de contenidos.
     * @param archivo Archivos a concatenar al final de <b>destino</b>
     * @param destino Archivo al que se le añadirá el contenido de <b>archivo</b>
     * @return true si el proceso se produce correctamente false si no.
     * @throws java.io.IOException si se produce algún error en la concatenación.
     * @see java.nio.channels.FileChannel
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
                int maxCount = getMaxCountTransferTo();
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
     * Borra un directorio y todos su contenido (incluidos subdirectorios y su contenido si los hubiera).<br/>
     * Si falla el borrado de un elemento cancela el proceso y no continua.
     * @param dir Directorio a borrar
     * @return True si ha borrado correctamente todo, False si ha fallado el borrado de alguno de los archivos
     */
    public static boolean borrarDirectorio(File dir) {
        if (dir != null && dir.isDirectory()) {
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
     * Descomprime un archivo zip en un directorio dado .
     * @param zip Archivo zip a descomprimir
     * @param destino Directorio de destino del contenido del zip.
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
            byte data[] = new byte[TAM_BUFFER];
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    File destFN = new File(destino, entry.getName());
                    destFN.getParentFile().mkdirs();
                    if (!destFN.exists() || (sobreescribir)) {
                        try {
                            Logger.getLogger(Archivo.class.getName()).fine("Descomprimiendo " + destFN.getAbsolutePath() + " proveniente de " + zip.getAbsolutePath() + " en " + destino.getAbsolutePath());
                            FileOutputStream fos = new FileOutputStream(destFN);
                            dest = new BufferedOutputStream(fos, TAM_BUFFER);
                            while ((count = zis.read(data, 0, TAM_BUFFER)) != -1) {
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
                    String path = "null";
                    if (f != null) {
                        path = f.getAbsolutePath();
                    }
                    Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, "Error añadiendo archivo " + path + " a " + archivoZip.getAbsolutePath(), e);
                }
            }
            out.finish();
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, "Error accediendo al archivo " + archivoZip.getAbsolutePath(), ex);
        }
        Obj.cerrar(fos);
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

    /**
     * Genera el checksum en MD5 para el archivo indicado.
     * @param archivo String con la ruta al archivo
     * @return String con el checksum en MD5 del archivo indicado.
     * @throws IOException si se produce algun error accediendo al archivo.
     */
    public static String getChecksumMD5(String archivo) throws IOException {
        InputStream fis = new FileInputStream(archivo);
        String csum = getChecksumMD5(fis);
        fis.close();
        return csum;
    }

    /**
     * Genera el checksum en MD5 para el InputStream indicado.
     * @param fis InputStream del que se generará el checksum en MD5
     * @return String con el checksum en MD5 del InputStream indicado.
     * @throws IOException si se produce algun error accediendo al InputStream.
     */
    public static String getChecksumMD5(InputStream fis) throws IOException {
        byte[] buffer = new byte[1024 * 64];
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
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, "Error ejecutando algoritmo MD5", ex);
        }
        return "";
    }

    /**
     * Extrae un recurso de una clase y lo guarda en un fichero.
     * @param claseRecurso Clase base desde la que se extraerá el recurso.
     * @param recurso Ruta al recurso a extraer
     * @param salida Fichero donde se guardará el recurso
     * @param sobreEscribir True si se desea sobreescribir el fichero de salida si existe, false si no.
     * @return True si se ha realizado la extración con éxito, False si no.
     * @see Class#getResourceAsStream(java.lang.String) 
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

    /**
     * Función para asignar de forma persistente el último archivo usado en una operación.<br/>
     * Esta función permite guardar mediante java.util.prefs.Preferences el último archivo usado, muy recomendable para que siempre
     * que se muestre un diálogo de abrir/guardar se muestre en el último archivo que se abrió/guardó.<br/>
     * Esta función guarda la referencia al archivo bajo la etiqueta "cdk_ultimo_archivo" si se desea especificar la etiqueta manualmente use la función
     * setUltimoArchivoUsado(Object aplicacion, File archivo, String etiqueta).<br/>
     * El archivo se puede recuperar posteriormente con getUltimoArchivoUsado(java.lang.Object) 
     * @param aplicacion Objeto de aplicación sobre el que se guardará la información persistente del archivo.
     * @param archivo Archivo del que se guardará la ruta.
     * @see java.util.prefs.Preferences
     * @see Archivo#setUltimoArchivoUsado(Object aplicacion, File archivo, String etiqueta)
     * @see Archivo#getUltimoArchivoUsado(java.lang.Object) 
     */
    public static void setUltimoArchivoUsado(Object aplicacion, File archivo) {
        setUltimoArchivoUsado(aplicacion, archivo, "cdk_ultimo_archivo");
    }

    /**
     * Función para asignar de forma persistente el último archivo usado en una operación.<br/>
     * Esta función permite guardar mediante java.util.prefs.Preferences el último archivo usado, muy recomendable para que siempre
     * que se muestre un diálogo de abrir/guardar se muestre en el último archivo que se abrió/guardó.<br/>
     * El archivo se puede recuperar posteriormente con getUltimoArchivoUsado(Object aplicacion,String etiqueta)
     * @param aplicacion Objeto de aplicación sobre el que se guardará la información persistente del archivo.
     * @param archivo Archivo del que se guardará la ruta.
     * @param etiqueta Etiqueta bajo la que se guardará la información del archivo. Util para poder definir distintos archivos.
     * @see java.util.prefs.Preferences
     * @see Archivo#getUltimoArchivoUsado(Object aplicacion,String etiqueta)
     */
    public static void setUltimoArchivoUsado(Object aplicacion, File archivo, String etiqueta) {
        if (archivo != null) {
            Preferences.userNodeForPackage(aplicacion.getClass()).put(etiqueta, archivo.getAbsolutePath());
        } else {
            Preferences.userNodeForPackage(aplicacion.getClass()).remove(etiqueta);
        }
    }

    /**
     * Esta función permite recuperar el archivo guardado mediante setUltimoArchivoUsado(Object aplicacion, File archivo)
     * @param aplicacion Objeto de aplicación sobre el que se guardará la información persistente del archivo.
     * @return File con el archivo guardado. Si no se ha guardado ninguno devolverá un archivo apuntando a la carpeta de usuario (System.getProperty("user.dir"))
     * @see Archivo#setUltimoArchivoUsado(java.lang.Object, java.io.File)
     */
    public static File getUltimoArchivoUsado(Object aplicacion) {
        return getUltimoArchivoUsado(aplicacion, "cdk_ultimo_archivo");
    }

    /**
     * Esta función permite recuperar el archivo guardado mediante setUltimoArchivoUsado(Object aplicacion, File archivo, String etiqueta)
     * @param aplicacion Objeto de aplicación sobre el que se guardará la información persistente del archivo.
     * @param etiqueta Etiqueta bajo la que se guardará la información del archivo. 
     * @return File con el archivo guardado. Si no se ha guardado ninguno devolverá un archivo apuntando a la carpeta de usuario (System.getProperty("user.dir"))
     * @see Archivo#setUltimoArchivoUsado(java.lang.Object, java.io.File, java.lang.String)
     */
    public static File getUltimoArchivoUsado(Object aplicacion, String etiqueta) {
        return new File(Preferences.userNodeForPackage(aplicacion.getClass()).get(etiqueta, System.getProperty("user.dir")));
    }
}
