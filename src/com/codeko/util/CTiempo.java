/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeko.util;

/**
 *
 * @author codeko
 */
public class CTiempo {

    long inicial = System.currentTimeMillis();
    long anterior = inicial;
    String nombre = "";
    static boolean activo = true;

    public static boolean isActivo() {
        return activo;
    }

    public static void setActivo(boolean activo) {
        CTiempo.activo = activo;
    }



    public CTiempo(String nombre) {
        this.nombre = nombre;
    }

    public void showTimer(String tag) {
        long t = System.currentTimeMillis();
        if (isActivo()) {
            System.out.println("[" + nombre + "] " + tag + ":" + (t - anterior) + "(" + (t - inicial) + ")");
        }
        anterior = System.currentTimeMillis();
    }
}
