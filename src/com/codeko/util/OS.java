package com.codeko.util;

/**
 * Gestiona todo tipo de operaciones comunes relacionadas con el Sistema operativo
 * @author codeko
 */
public class OS {
    public static boolean isWindows(){
        return System.getProperty("os.name").toLowerCase().indexOf("windows")!=-1;
    }
}
