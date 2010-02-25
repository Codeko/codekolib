package com.codeko.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Num.java
 * Clase encargarda de gestionar las operaciones más comunes relacionadas con numeros
 * @author Boris Burgos García
 * @version 1.1
 */
public class Num {
    //Logger de la clase. De primeras se loggeará como finest siempre

    private static Logger log = Logger.getLogger(Num.class.toString());
    private static Level level = Level.FINEST;

    /**
     * Convierte objetos en enteros
     * @param obj Objeto a convertir en entero
     * @return La conversión a entero del objeto o 0 si no se ha podido convertir
     */
    public static int getInt(Object obj) {
        return getInt(obj, 0);
    }

    /**
     * Convierte objetos en enteros
     * @param obj Objeto a convertir en entero
     * @param porDefecto Valor que tomará el entero si no se puede convertir el objeto
     * @return La conversión a entero del objeto o el valor por defecto si no se ha podido convertir
     */
    public static int getInt(Object obj, int porDefecto) {
        if (obj == null) {
            return porDefecto;
        } else if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        try {
            return Integer.parseInt(obj.toString().trim());
        } catch (Exception e) {
            log.log(level, "OBJ: " + obj + " DEFAULT:" + porDefecto, e);
        }
        return porDefecto;
    }

    /**
     * Convierte objetos en float
     * @param obj Objeto a convertir en float
     * @return La conversión a float del objeto o 0 si no se ha podido convertir
     */
    public static float getFloat(Object obj) {
        return getFloat(obj, 0);
    }

    /**
     * Convierte objetos en float
     * @param obj Objeto a convertir en float
     * @param porDefecto Valor que tomará el float si no se puede convertir el objeto
     * @return La conversión a float del objeto o el valor por defecto si no se ha podido convertir
     */
    public static float getFloat(Object obj, float porDefecto) {
        if (obj == null) {
            return porDefecto;
        } else if (obj instanceof Number) {
            return ((Number) obj).floatValue();
        }
        try {
            return Float.parseFloat(obj.toString());
        } catch (Exception e) {
            log.log(level, "OBJ: " + obj + " DEFAULT:" + porDefecto, e);
        }
        return porDefecto;
    }

    /**
     * Convierte objetos en double
     * @param obj Objeto a convertir en double
     * @return La conversión a double del objeto o 0 si no se ha podido convertir
     */
    public static double getDouble(Object obj) {
        return getDouble(obj, 0);
    }

    /**
     * Convierte objetos en double
     * @param obj Objeto a convertir en double
     * @param porDefecto Valor que tomará el double si no se puede convertir el objeto
     * @return La conversión a double del objeto o el valor por defecto si no se ha podido convertir
     */
    public static double getDouble(Object obj, double porDefecto) {
        if (obj == null) {
            return porDefecto;
        } else if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            log.log(level, "OBJ: " + obj + " DEFAULT:" + porDefecto, e);
        }
        return porDefecto;
    }

    /**
     * Convierte objetos en long
     * @param obj Objeto a convertir en long
     * @return La conversión a long del objeto o 0 si no se ha podido convertir
     */
    public static long getLong(Object obj) {
        return getLong(obj, 0);
    }

    /**
     * Convierte objetos en long
     * @param obj Objeto a convertir en long
     * @param porDefecto Valor que tomará el long si no se puede convertir el objeto
     * @return La conversión a long del objeto o el valor por defecto si no se ha podido convertir
     */
    public static long getLong(Object obj, long porDefecto) {
        if (obj == null) {
            return porDefecto;
        } else if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        try {
            return Long.parseLong(obj.toString());
        } catch (Exception e) {
            log.log(level, "OBJ: " + obj + " DEFAULT:" + porDefecto, e);
        }
        return porDefecto;
    }

    /**
     * Elimina de una cadena todos los caracteres que no sean numéricos (includos ., etc)
     * @param texto Texto a limpiar
     * @return  La cadena de texto pasada dejando solo los caracteres 0, 1, 2, 3, 4, 5, 6, 7, 8 y 9
     */
    public static String limpiar(String texto) {
        StringBuilder cadena = new StringBuilder("");
        for (int i = 0; i < texto.length(); i++) {
            char car = texto.charAt(i);
            if (esNumero(car)) {
                cadena.append(car);
            }
        }
        return cadena.toString();
    }

    /**
     * Elimina de una cadena todos los caracteres que no sean numéricos o decimales
     * @param texto Texto a limpiar
     * @return  La cadena de texto pasada dejando solo los caracteres 0, 1, 2, 3, 4, 5, 6, 7, 8 y 9
     */
    public static String limpiarDecimal(String texto) {
        StringBuilder cadena = new StringBuilder("");
        for (int i = 0; i < texto.length(); i++) {
            char car = texto.charAt(i);
            if (esNumero(car) || car == '.' || car == ',') {
                cadena.append(car);
            }
        }
        return cadena.toString();
    }

    /**
     * Informa de si un caracter es numérico o no
     * @param caracter Caracter a verificar
     * @return True si el caracer es numérico o False si no lo es
     */
    public static boolean esNumero(char caracter) {
        return (caracter > 47 && caracter < 58);
    }

    /**
     * Informa de si un objeto es numérica o no. Si es un objeto hijo de Number directamente devuelve true.
     * Si no lo es lo convierte a cadena y verifica que todos sus caracteres sean números
     * @param objeto Cadena a verificar
     * @return True si el objeto es numérico o False si no lo es
     */
    public static boolean esNumero(Object objeto) {
        boolean esNum = true;
        if (!(objeto instanceof Number)) {
            String cadena = Str.noNulo(objeto);
            try {
                Double d = new Double(cadena);
            } catch (NumberFormatException ex) {
                esNum = false;
            }
        }
        return esNum;
    }

    /**
     * Convierte un objeto a una cadena de texto con formato de dinero según el locale por defecto
     * @param dinero Objeto a formatear
     * @return String con el objeto pasado formateado
     * El objeto es convertido a double antes de ser formateado
     */
    public static String formatearDinero(Object dinero) {
        return formatearDinero(dinero, Locale.getDefault());
    }

    /**
     * Convierte un objeto a una cadena de texto con formato de dinero según un locale
     * @param dinero Objeto a formatear
     * @param locale Locale que definirá el formato del dinero
     * @return String con el objeto pasado formateado
     * * El objeto es convertido a double antes de ser formateado
     */
    public static String formatearDinero(Object dinero, Locale locale) {
        double d = 0;
        if (dinero instanceof Number) {
            d = ((Number) dinero).doubleValue();
        } else {
            try {
                d = NumberFormat.getCurrencyInstance().parse(dinero + "").doubleValue();
            } catch (ParseException ex) {
                d = getDouble(dinero);
            }
        }
        return formatearDinero(d, locale);
    }

    /**
     * Convierte un double a una cadena de texto con formato de dinero según el locale por defecto
     * @param dinero Double a formatear
     * @return String con el double pasado formateado
     */
    public static String formatearDinero(double dinero) {
        return formatearDinero(dinero, Locale.getDefault());
    }

    /**
     * Convierte un double a una cadena de texto con formato de dinero según un Locale
     * @param dinero Double a formatear
     * @param locale Locale que definirá el formato del dinero
     * @return String con el double pasado formateado
     */
    public static String formatearDinero(double dinero, Locale locale) {
        return NumberFormat.getCurrencyInstance(locale).format(dinero);
    }

    /**
     * Funcion igual a Math.max pero que admite un numero variable de parametros y que estos sean de cualquier tipo de Number
     * @param numeros Numeros a comparar
     * @return El numero mayor
     */
    public static Number max(Number... numeros) {
        Number mayor = null;
        double my = 0;
        for (Number n : numeros) {
            if (n != null) {
                if (mayor == null) {
                    mayor = n;
                    my = n.doubleValue();
                } else {
                    double d = n.doubleValue();
                    if (d > my) {
                        mayor = n;
                        my = n.doubleValue();
                    }
                }
            }
        }
        return mayor;
    }

    /**
     * Funcion igual a Math.min pero que admite un numero variable de parametros y que estos sean de cualquier tipo de Number
     * @param numeros Numeros a comparar
     * @return El numero menor
     */
    public static Number min(Number... numeros) {
        Number mayor = null;
        double my = 0;
        if (numeros != null) {
            for (Number n : numeros) {
                if (n != null) {
                    if (mayor == null) {
                        mayor = n;
                        my = n.doubleValue();
                    } else {
                        double d = n.doubleValue();
                        if (d < my) {
                            mayor = n;
                            my = n.doubleValue();
                        }
                    }
                }
            }
        }
        return mayor;
    }

    /**
     * Convierte un objeto a decimal, tratando con problemas como que tenga . o , como separador, que tenga separador de miles etc...
     * El analisis lo hace considerando el último ./, como separador decimal y eliminado el resto
     * @param obj Objeto a analizar
     * @return Double con el valor decimal (0 si no se ha podido analizar)
     */
    public static double getDecimal(Object obj) {
        double d = 0;
        if (obj != null) {
            //Primero verificamos que no sea un número ya
            if (obj instanceof Number) {
                d = ((Number) obj).doubleValue();
            } else {
                //Si falla probamos una version casera
                String num = limpiarDecimal(obj.toString());
                //Vemos la posicion de la coma, si tiene
                int posComa = num.lastIndexOf(",");
                int posPunto = num.lastIndexOf(".");

                if (posComa == posPunto) {//Si son iguales es que no hay ninguno
                    num = limpiar(num);//Limpiamos el numero totalmente
                } else {//Si no eliminamos todo lo que haya hasta el último simbolo 
                    int mayor = getInt(max(posComa, posPunto));
                    StringBuilder sb = new StringBuilder(limpiar(num.substring(0, mayor)));
                    sb.append(".");
                    sb.append(limpiar(num.substring(mayor)));
                    num = sb.toString();
                }
                d = getDouble(num);
            }
        }
        return d;
    }

    /**
     * Redondea un número a la cantidad de decimales especificada
     * El redondeo se realiza de forma estandar, es decir valores de 5 o superiores se redondean al alza y de 4 e inferiores 
     * a la baja
     * @param numero Double a redondear
     * @param decimales Número de decimales 
     * @return El numero rendonde a decimales
     */
    public static double round(double numero, int decimales) {
        double retorno = 0;
        if (decimales >= 0 && numero != 0) {
            //Lo pasamos a long multiplicando por el numero de decimales
            double potencia = Math.pow(10d, Integer.valueOf(decimales).doubleValue());
            numero = numero * potencia;
            //Si es positivo se le añade 0.5 si es negativo se le quita
            numero = numero + (numero > 0 ? 0.5d : -0.5d);
            long entero = Double.valueOf(numero).longValue();
            retorno = entero / potencia;
        }
        return retorno;
    }

    /**
     * Contraccion de new Random().nextInt(max); pero cambiando que max sea exclusivo.
     * En la funcion original el ejemplo Random().nextInt(10) devolvería valores entre 0 y 9. 
     * Sin embargo con Num.rand(10); se devuelven valores entre 0 y 10
     * Los valores negativos generarán una excepción
     * @param max Valor máximo del número aleatorio 
     * @return Un entero pseudoaleatorio entre 0 y max
     */
    public static int rand(int max) {
        return new Random().nextInt(max + 1);
    }

    /**
     * Generador de números aleatorios entre un límite máximo y mínimo
     * Si min es menor que max o ambos son negativos lanzará una excepción
     * @param min Número mínimo
     * @param max Número máximo
     * @return número aleatorio entre min y max ambos incluidos
     */
    public static int rand(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    /**
     * Formatea una cantidad expresada en bytes en la unidad mas adecuada (desde B hasta GB)
     * @param tamanoEnBytes Cantidad expresada en bytes
     * @return Cadena compuesta por ##.##[B|KB|MB|GB]
     */
    public static String formatearTamano(long tamanoEnBytes) {
        String st = "";
        if (tamanoEnBytes > (1024 * 3)) {
            tamanoEnBytes = tamanoEnBytes / 1024;
            if (tamanoEnBytes > 1024) {
                double megas = tamanoEnBytes / 1024.0;
                if (megas > 1024) {
                    double gigas=megas/1024.0;
                    st = Num.round(gigas, 2) + "GB";
                } else {
                    st = Num.round(megas, 2) + "MB";
                }
            } else {
                st = tamanoEnBytes + "KB";
            }
        } else {
            st = tamanoEnBytes + "B";
        }
        return st;
    }
}
