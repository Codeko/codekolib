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
 */
//TODO Sería interesante poder mantener el archivo de certificados entre ejecuciones
public class CodekoKeyStore {

    private static final String NOMBRE_KS = "CodekoKeyStore";
    private File archivoKeyStore = null;
    private String claveKeyStore = "CodekoKeyStore";

    public CodekoKeyStore() {
    }

    public CodekoKeyStore(File archivoKeyStore, String claveKeyStore) {
        setArchivoKeyStore(archivoKeyStore);
        setClaveKeyStore(claveKeyStore);
    }

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
     * Asigna el KeyStore como KeyStore a usar para las conexiones SSL.
     */
    public void asignarComoSSLKeyStore() {
        Logger.getLogger(CodekoKeyStore.class.getName()).info("Guardando KS en "+getArchivoKeyStore().getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStore", getArchivoKeyStore().getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStorePassword", getClaveKeyStore());
    }

    public void setArchivoKeyStore(File archivoKeyStore) {
        this.archivoKeyStore = archivoKeyStore;
    }

    public String getClaveKeyStore() {
        return claveKeyStore;
    }

    public void setClaveKeyStore(String claveKeyStore) {
        this.claveKeyStore = claveKeyStore;
    }
}
