package com.codeko.util;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contiene funciones genéricas para tratar con distintos tipos de objetos.
 * Son funciones que simplemente ahorran código.
 * @author Boris Burgos García
 */
public class Obj {

    /**
     * Sirve para recuperar de una lista de objetos el primero que no sea nulo.<br/>
     * Por ejemplo:<br/>
     * noNull(null,null,4,"Una cadena") devolvería 4<br/>
     * noNull(null,null,null,null,"Primer no nulo",4,"Una cadena") devolvería "Primer no nulo"<br/>
     * @param valores Objetos a verificar si son nulos
     * @return El primer parametro no nulo
     */
    public static Object noNull(Object... valores) {
        Object r = null;
        for (Object v : valores) {
            if (v != null) {
                r = v;
                break;
            }
        }
        return r;
    }

    /**
     * Compara dos objetos mediante equals considerando que si son nulos ambos son iguales.
     * Evita a la hora de tener que comparar dos objetos que pueden ser nulos el tener que verificar primero la nulidad de cada uno
     * antes de comparar.
     * Esta funcion es muy util cuando por ejemplo se quieren compara dos objetos por el valor de sus propiedades que peuden ser nulas.
     * La forma normal para un objeto con propiedades A,B,C sería
     * NuestroObjeto obj1;
     * NuestroObjeto obj2;
     * if((obj1.getA()!=null && obj1.getA().equals(obj2.getA())) || (obj1.getA()==null && obj2.getA()==null)){
     *  ...
     * }
     * Con esta funcion se resume en
     * if(Obj.comparar(obj1.getA(),obj2.getA())){
     *  ...
     * }
     * @param a Objeto a comparar
     * @param b Objeto a comparar
     * @return True si son iguales o nulos los dos, false si no son iguales.
     */
    public static boolean comparar(Object a, Object b) {
        boolean ret = false;
        if (a == null && b == null) {
            ret = true;
        }
        if (a != null) {
            ret = a.equals(b);
        } else {
            ret = b.equals(a);
        }
        return ret;
    }

    /**
     * Cierra un recurso. Actualmente cierra:
     * <ul>
     * <li>Todos los objetos que implementen la interfaz java.io.Closeable</li>
     * <li>Todos los objetos que implementen la interfaz java.sql.Connection</li>
     * <li>Todos los objetos que implementen la interfaz java.sql.Statement</li>
     * <li>Todos los objetos que implementen la interfaz java.sql.ResultSet</li>
     * <li>Todos los objetos que tengán una función denominada close() y que no reciba parámetros.</li>
     * </ul>
     *
     * @param recurso Recurso/s a cerrar.
     * @return True si se han cerrado correctamente todos los recursos false si no, los recursos nulos se consideran cerrados.
     */
    public static boolean cerrar(Object... recurso) {
        boolean ret = true;
        for (Object rec : recurso) {
            if (rec instanceof Closeable) {
                try {
                    ((Closeable) rec).close();
                    ret = true;
                } catch (Exception ex) {
                    Logger.getLogger(Obj.class.getName()).log(Level.FINE, null, ex);
                    ret = false;
                }
            } else if (rec instanceof Connection) {
                try {
                    ((Connection) rec).close();
                    ret = true;
                } catch (Exception ex) {
                    Logger.getLogger(Obj.class.getName()).log(Level.FINE, null, ex);
                    ret = false;
                }
            } else if (rec instanceof Statement) {
                try {
                    ((Statement) rec).close();
                    ret = true;
                } catch (Exception ex) {
                    Logger.getLogger(Obj.class.getName()).log(Level.FINE, null, ex);
                    ret = false;
                }
            } else if (rec instanceof ResultSet) {
                try {
                    ((ResultSet) rec).close();
                    ret = true;
                } catch (Exception ex) {
                    Logger.getLogger(Obj.class.getName()).log(Level.FINE, null, ex);
                    ret = false;
                }
            } else {
                if (rec != null) {
                    try {
                        //Probamos a llamar al método close de la clase por si existe
                        rec.getClass().getMethod("close").invoke(rec);
                        ret = true;
                    } catch (NoSuchMethodException ex) {
                        Logger.getLogger(Obj.class.getName()).log(Level.FINE, null, ex);
                        ret = false;
                    } catch (SecurityException ex) {
                        Logger.getLogger(Obj.class.getName()).log(Level.FINE, null, ex);
                        ret = false;
                    } catch (Exception ex) {
                        Logger.getLogger(Obj.class.getName()).log(Level.FINE, "No se reconoce el la clase " + rec.getClass().getName() + " como cerrable o ha lanzado una excepción al ejecutarse el método close().");
                        ret = false;
                    }
                }
            }
        }
        return ret;
    }
}
