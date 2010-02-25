package com.codeko.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contiene una serie de utilidades de red que aunque quizás no se usen demasiado en una aplicación
 * normal suelen requerir investigar la forma de hacer la operación en concreto. 
 *  <p>
 * Estas funciones son puramente practicas por lo que son bastante flexibles en cuanto a parametros 
 * y generan el número menor de errores y excepciones. En la mayoría de los casos en vez de lanzar una 
 * excepción aplican un valor por defecto.
 * </p>
 * @author Boris Burgos García
 * @version 0.1
 * 12/08/2008
 */
public class Red {

    /**
     * Devuelve la IP publica procurando buscar una que no sea IPV6 y que no sea 127.0.0.1
     * Si no encuentra una ip válida devuelve "localhost".
     * Si existe por lo tanto debe devolver una ip accesible desde otro puesto.
     * @return IP local de la máquina
     */
    public static String getIPPublica() {
        String ip = "localhost";
        try {
            Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
            boolean desechar = true;
            //Mientras haya interfaces de red o la que hemos procesado se vaya a desechar
            while (ni.hasMoreElements() && desechar) {
                NetworkInterface n = ni.nextElement();
                Enumeration<InetAddress> ad = n.getInetAddresses();
                desechar = false;//de primeras no la marcamos para desechar
                while (ad.hasMoreElements()) {
                    InetAddress a = ad.nextElement();
                    if (a.getHostAddress().equals("127.0.0.1")) {
                        desechar = true;
                    } else {
                        if (a.getHostAddress().indexOf(":") == -1) {
                            ip = a.getHostAddress();
                        }
                    }
                }
                if (desechar) {
                    ip = "localhost";
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Red.class.getName()).log(Level.SEVERE, "Error determinado ip local", ex);
        }
        return ip;
    }

    /**
     * Devuelve la IP publica procurando buscar una que sea IPV6 y que no sea "::1"
     * Si no encuentra una ip válida devuelve "localhost".
     * Si existe por lo tanto debe devolver una ip accesible desde otro puesto.
     * @return IP local de la máquina
     */
    public static String getIPV6Publica() {
        String ip = "localhost";
        try {
            Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
            boolean desechar = true;
             //Mientras haya interfaces de red o la que hemos procesado se vaya a desechar
            while (ni.hasMoreElements() && desechar) {
                NetworkInterface n = ni.nextElement();
                Enumeration<InetAddress> ad = n.getInetAddresses();
                desechar = false;
                while (ad.hasMoreElements()) {
                    InetAddress a = ad.nextElement();
                    if (a.getHostAddress().equals("127.0.0.1")) {
                        desechar = true;
                    } else {
                        if (a.getHostAddress().indexOf(":") != -1) {
                            ip = a.getHostAddress();
                        }
                    }
                }
                if (desechar) {
                    ip = "localhost";
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Red.class.getName()).log(Level.SEVERE, "Error determinando IPv6 local", ex);
        }
        return ip;
    }
}
