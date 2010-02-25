package com.codeko.util;

/**
 *
 * @author codeko
 */
public class OS {
    public static boolean isWindows(){
        return System.getProperty("os.name").toLowerCase().indexOf("windows")!=-1;
    }
}
