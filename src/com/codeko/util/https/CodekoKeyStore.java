package com.codeko.util.https;

import com.codeko.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Copyright Codeko Informática 2008
 * www.codeko.com
 * @author Codeko
 * Gestiona un archivo keystore para usar de cara a las conexiónes SSL sin interferir con el keystore del usuario.
 * @see java.security.KeyStore
 */
//TODO Sería interesante poder mantener el archivo de certificados entre ejecuciones
public class CodekoKeyStore {

    private static final String NOMBRE_KS = "CodekoKeyStore";
    private File archivoKeyStore = null;
    private String claveKeyStore = "CodekoKeyStore";

    /**
     * Crea un nuevo KeyStore
     */
    public CodekoKeyStore() {
    }
    /**
     * Crea un nuevo KeyStore en la localización indicada y con la clave de acceso indicada.
     * @param archivoKeyStore File donde está situado el KeyStore
     * @param claveKeyStore String con la clave de acceso al KeyStore
     */
    public CodekoKeyStore(File archivoKeyStore, String claveKeyStore) {
        setArchivoKeyStore(archivoKeyStore);
        setClaveKeyStore(claveKeyStore);
    }

    /**
     * Devuelve el keyStore creándolo si no existiese
     * @return File del KeyStore
     */
    public File getArchivoKeyStore() {
        if (archivoKeyStore == null || !archivoKeyStore.exists()) {
            InputStream is = getClass().getResourceAsStream(NOMBRE_KS);
            FileOutputStream fos = null;
            try {
                File fOut = File.createTempFile("cdk_", ".ks");
                fos = new FileOutputStream(fOut);
                if (Archivo.copiarArchivo(is, fos)) {
                    setArchivoKeyStore(fOut);
                } else {
                    Logger.getLogger(CodekoKeyStore.class.getName()).severe("No se ha podido copiar el archivo de certificados.");
                }
            } catch (IOException ex) {
                Logger.getLogger(CodekoKeyStore.class.getName()).log(Level.SEVERE, "Error copiando creando archivo temporal.", ex);
            } finally {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(CodekoKeyStore.class.getName()).log(Level.WARNING, "Error cerrando recurso", ex);
                }
                try {
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(CodekoKeyStore.class.getName()).log(Level.WARNING, "Error cerrando recurso", ex);
                }
            }
        }
        return archivoKeyStore;
    }

    /**
     * Asigna el KeyStore como KeyStore a usar para las conexiones SSL por Java.
     */
    public void asignarComoSSLKeyStore() {
        Logger.getLogger(CodekoKeyStore.class.getName()).info("Guardando KS en "+getArchivoKeyStore().getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStore", getArchivoKeyStore().getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStorePassword", getClaveKeyStore());
    }

    /**
     * Asigna un archivo como KeyStore
     * @param archivoKeyStore File a usar como KeyStore
     */
    public void setArchivoKeyStore(File archivoKeyStore) {
        this.archivoKeyStore = archivoKeyStore;
    }

    /**
     * Devuelve la clave asociada al KeyStore
     * @return String con la clave asociada
     */
    public String getClaveKeyStore() {
        return claveKeyStore;
    }
    /**
     * Asigna la clave asociada al KeyStore
     * @param claveKeyStore String con la clave del KeyStore
     */
    public void setClaveKeyStore(String claveKeyStore) {
        this.claveKeyStore = claveKeyStore;
    }
}
