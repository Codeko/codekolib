package com.codeko.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilidades de fechas
 * @author codeko
 * @version 1.0.1
 * 12/08/2008
 */
public class Fechas {

    /**
     * Formatea una fecha según un formato por defecto. Este formato esta definido en la propiedad del sistema com.codeko.util.fechas.formatoFecha
     * @param cal Fecha a formatear, puede ser un objeto Calendar, un Objeto date o una cadena de texto con el formato por defecto.
     * @return String con la fecha formateada o cadena vacia si la fecha es nula
     */
    public static String format(Object cal) {
        if (cal != null) {
            if (cal instanceof Calendar) {
                return format(((Calendar) cal).getTime());
            } else if (cal instanceof Date) {
                return format(((Date) cal));
            } else {
                return format(parse(cal.toString()));
            }
        }
        return "";
    }

    /**
     * Formatea una fecha según un formato @see SimpleDateFormat.
     * @param cal Fecha a formatear, puede ser un objeto Calendar, un Objeto date o una cadena de texto con el formato por defecto.
     * @param formato Formato de la fecha
     * @return String con la fecha formateada o cadena vacia si la fecha es nula
     */
    public static String format(Object cal, String formato) {
        if (cal != null) {
            if (cal instanceof Calendar) {
                return format(((Calendar) cal).getTime(), formato);
            } else if (cal instanceof Date) {
                return format(((Date) cal), formato);
            } else {
                return format(parse(cal.toString()), formato);
            }
        }
        return "";
    }

    /**
     * Formatea una fecha según un formato por defecto. Este formato esta definido en la propiedad del sistema com.codeko.util.fechas.formatoFecha
     * @param cal Fecha a formatear
     * @return String con la fecha formateada o cadena vacia si la fecha es nula
     */
    public static String format(Calendar cal) {
        if (cal != null) {
            return format(cal.getTime());
        }
        return "";
    }

    /**
     * Formatea una fecha según el formato dado @see SimpleDateFormat
     * @param cal Fecha a formatear
     * @param formato Formato de la fecha
     * @return String con la fecha formateada o cadena vacia si la fecha es nula
     */
    public static String format(Calendar cal, String formato) {
        String ret = "";
        if (cal != null) {
            ret = format(cal.getTime(), formato);
        }
        return ret;
    }

    /**
     * Formatea una fecha según el formato dado @see SimpleDateFormat
     * @param date Fecha a formatear
     * @param formato Formato de la fecha
     * @return String con la fecha formateada
     */
    public static String format(Date date, String formato) {
        SimpleDateFormat sdf = new SimpleDateFormat(formato, Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * Formatea una fecha según un formato por defecto. Este formato esta definido en la propiedad del sistema com.codeko.util.fechas.formatoFecha
     * @param date Fecha a formatear
     * @return String con la fecha formateada
     */
    public static String format(Date date) {
        return format(date, getFormatoFechaPorDefecto());
    }

    /**
     * Analiza una cadena de texto esperando el formato de fecha por defecto.
     * Este formato esta definido en la propiedad del sistema com.codeko.util.fechas.formatoFecha
     * @param fecha Cadena de texto con el formato de fecha por defecto
     * @return Date fecha de la cadena en formato Date o null si se produce algún error analizando la fecha
     */
    public static Date parse(String fecha) {
        return parse(fecha, getFormatoFechaPorDefecto());
    }

    /**
     * Analiza una fecha dada según un formato de fecha dado
     * @param fecha Cadena de texto con la fecha en el formato dado
     * @param formato Formato de la fecha dada
     * @return Date fecha de la cadena como Objeto date o null si se produce algún error analizando la fecha
     */
    public static Date parse(String fecha, String formato) {
        SimpleDateFormat sdf = new SimpleDateFormat(formato, Locale.getDefault());
        try {
            return sdf.parse(fecha);
        } catch (ParseException ex) {
            Logger.getLogger(Fechas.class.getName()).log(Level.SEVERE, "Error procesando fecha: '" + fecha + "' con formato '" + formato + "'", ex);
        }
        return null;
    }

    /**
     * Devuelve el formato de fecha usado por esta clase a la hora de formatear fechas
     * Por defecto el formato es "dd/MM/yyyy".
     * Internamente se almacena en la variable de sistema "com.codeko.util.fechas.formatoFecha"
     * @return Formato de fecha según DateFormat
     */
    public static String getFormatoFechaPorDefecto() {
        return System.getProperty("com.codeko.util.fechas.formatoFecha", "dd/MM/yyyy");
    }

    /**
     * Asigna el formato de fecha que se usará para formatear fechas por esta clase
     * @see Fechas#getFormatoFechaPorDefecto()
     * @param formato Formato de fecha según DateFormat
     */
    public static void setFormatoFechaPorDefecto(String formato) {
        System.setProperty("com.codeko.util.fechas.formatoFecha", formato);
    }

    /**
     * Devuelve el formato de fecha usado por esta clase a la hora de formatear fechas para base de datos
     * Por defecto el formato es "yyyy-MM-dd" usado por MySQL.
     * Internamente se almacena en la variable de sistema "com.codeko.util.fechas.formatoFechaBD"
     * @return Formato de fecha según DateFormat
     */
    public static String getFormatoFechaBD(){
        return System.getProperty("com.codeko.util.fechas.formatoFechaBD", "yyyy-MM-dd");
    }


    /**
     * Asigna el formato de fecha que se usará para formatear fechas para base de datos por esta clase
     * @see Fechas#getFormatoFechaBD() 
     * @param formato Formato de fecha según DateFormat
     */
    public static void setFormatoFechaBD(String formato) {
        System.setProperty("com.codeko.util.fechas.formatoFechaBD", formato);
    }

    /**
     * Formatea una fecha según el formato de base de datos "yyyy-MM-dd"
     * @param milis Fecha en milisegundos a formatear
     * @return String con la fecha formateada
     */
    public static String getFechaFormatoBD(long milis) {
        return getFechaFormatoBD(new Date(milis));
    }

    /**
     * Formatea una fecha según el formato de base de datos "yyyy-MM-dd"
     * @param fecha Calendar a formatear
     * @return String con la fecha formateada
     */
    public static String getFechaFormatoBD(Calendar fecha) {
        return getFechaFormatoBD(fecha.getTime());
    }

    /**
     * Formatea una fecha según el formato de base de datos.
     * El formato por defecto es "yyyy-MM-dd" pudiendo cambiarse con la funcion setFormatoFechaBD(String formato)
     * @param fecha Date a formatear
     * @return String con la fecha formateada
     */
    public static String getFechaFormatoBD(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat(getFormatoFechaBD());
        return sdf.format(fecha);
    }

    /**
     * Devuelve la diferencia entre dos fechas en el tipo de dato especificado.
     * El tipo de dato puede ser:
     * <ul>
     * <li>Calendar.MILLISECOND</li>
     * <li>Calendar.SECOND</li>
     * <li>Calendar.MINUTE</li>
     * <li>Calendar.HOUR</li>
     * <li>Calendar.DATE o Calendar.DAY_OF_MONTH o Calendar.DAY_OF_YEAR: En todos devuelve la diferencia en dias</li>
     * <li>Calendar.MONTH</li>
     * <li>Calendar.WEEK_OF_MONTH o Calendar.WEEK_OF_YEAR: En ambos devuelve la diferencia en semanas</li>
     * <li>Calendar.YEAR</li>
     * </ul>
     * Si alguna de las fechas es nula devuelve 0.
     * Los calculos se hacen de forma absoluta es decir, los años tienes 365 días, los meses 30 días, etc...  
     * La diferencia entre fechas si tiene en cuenta los días exactos pero la suma no.
     * Por ejemplo:
     * Entre el 1 de febrero de 2008 y el 1 de marzo de 2008 la diferencia será en dias de 28, pero en meses no será de 1 sino de 0 ya que no han pasado 30 dias
     * Para diferencias entre datos en concreto usar @see getDiferenciaEn
     * @param mayor
     * @param menor
     * @param tipo
     * @return long diferencia entre las dos fecha en la unidad determinada
     */
    public static long getDiferenciaTiempoEn(Calendar mayor, Calendar menor, int tipo) {
        long ret = 0;
        if (mayor != null && menor != null) {
            long dif = mayor.getTimeInMillis() - menor.getTimeInMillis();
            switch (tipo) {
                case Calendar.MILLISECOND:
                    ret = dif;
                    break;
                case Calendar.SECOND:
                    ret = dif / 1000;
                    break;
                case Calendar.MINUTE:
                    ret = (dif / 1000) / 60;
                    break;
                case Calendar.HOUR:
                    ret = ((dif / 1000) / 60) / 60;
                    break;
                case Calendar.DAY_OF_MONTH:
                case Calendar.DAY_OF_YEAR:
                    ret = (((dif / 1000) / 60) / 60) / 24;
                    break;
                case Calendar.MONTH:
                    ret = ((((dif / 1000) / 60) / 60) / 24) / 30;
                    break;
                case Calendar.WEEK_OF_MONTH:
                case Calendar.WEEK_OF_YEAR:
                    ret = (((((dif / 1000) / 60) / 60) / 24) / 30) / 7;
                    break;
                case Calendar.YEAR:
                    ret = (((((dif / 1000) / 60) / 60) / 24) / 30) / 375;
                    break;
            }
        }
        return ret;
    }
    /**
     * Número de milisegundos en un segundo
     */
    public static final int SEGUNDO = 1000;
    /**
     * Número de milisegundos en un minuto
     */
    public static final int MINUTO = SEGUNDO * 60;
    /**
     * Número de milisegundos en una hora
     */
    public static final int HORA = MINUTO * 60;
    /**
     * Número de milisegundos en un dia
     */
    public static final int DIA = HORA * 24;
    /**
     * Número de milisegundos en un año
     */
    public static final int ANO = DIA * 365;

    /**
     * Atajo para el uso más común de la funcion String formatearMilisegundos(long milis, boolean extendido,boolean ignorarMilis,boolean ignorarSegundos,boolean ignorarMinutos,boolean ignorarHoras,boolean ignorarDias,boolean ignorarAnos)
     * Formatea los milisegundos en formato reducido y sin mostrar los milisegundos. Equivalente a llamar a la funcion: formatearMilisegundos(long milis, false,true,false,false,false,false,false)
     * @param milis Milisegundos a formatear
     * @return Cadena de texto con los milisegundos formateados en formato #a #d #h #m #s
     */
    public static String formatearMilisegundos(long milis) {
        return formatearMilisegundos(milis, false, true, false, false, false, false, false);
    }

    /**
     * Atajo para el uso más común de la funcion String formatearMilisegundos(long milis, boolean extendido,boolean ignorarMilis,boolean ignorarSegundos,boolean ignorarMinutos,boolean ignorarHoras,boolean ignorarDias,boolean ignorarAnos)
     * Formatea los milisegundos en formato reducido y sin mostrar los milisegundos. Equivalente a llamar a la funcion: formatearMilisegundos(long milis, boolean extendido,true,false,false,false,false,false)
     * @param milis Milisegundos a formatear
     * @param extendido True para mostrar los textos de las unidades en formato extendido y false para hacerlo en formato reducido
     * @return Cadena de texto con los milisegundos formateados en formato #a #d #h #m #s en formato no extendido y #año/s #dia/s #hora/s #minuto/s #segundo/s (las 's' finales sólo se muestran cuando la unidad es mayor que 1)
     */
    public static String formatearMilisegundos(long milis, boolean extendido) {
        return formatearMilisegundos(milis, extendido, true, false, false, false, false, false);
    }

    /**
     * <p>Función para formatear una cantidad de milisegundos en una cadena de texto. Sólo se muestran las unidades que tengan valor >0 y 
     * el texto de las unidades es sensible a la cantidad, es decir, muestra segundo o segundos en su formato extendido según el valor de los segundos por ejemplo.
     * </p><br/>
     * Permite ignorar la representación de cada unidad. Cuando una unidad se ignora su valor pasa a la siguiente. 
     * Por ejemplo, para un valor en milisegundo de 2 días y 5 horas, si se marca ignorar días la funcion devolverá 53h (48 horas + 5 horas) o 53 horas en su formato extendido.
     * @param milis Milisegundos a formatear
     * @param extendido True para mostrar los textos de las unidades en formato extendido (año/s, dia/s,hora/s,minuto/s,segundo/s,milisegundo/s) y false para hacerlo en formato reducido(a,d,h,m,s,ms)
     * @param ignorarMilis True para no mostrar el valor en milisegundos
     * @param ignorarSegundos True para no mostrar el valor en segundos
     * @param ignorarMinutos True para no mostrar el valor en minutos
     * @param ignorarHoras True para no mostrar el valor en horas
     * @param ignorarDias True para no mostrar el valor en dias
     * @param ignorarAnos True para no mostrar el valor en años
     * @return String con los milisegundos formateados según los parametros dados.
     */
    public static String formatearMilisegundos(long milis, boolean extendido, boolean ignorarMilis, boolean ignorarSegundos, boolean ignorarMinutos, boolean ignorarHoras, boolean ignorarDias, boolean ignorarAnos) {
        StringBuilder sb = new StringBuilder();
        long restante = milis;
        long anos = restante / ANO;
        if (!ignorarAnos && anos > 0) {
            sb.append(anos);
            if (extendido) {
                if (anos == 1) {
                    sb.append(" año ");
                } else {
                    sb.append(" años ");
                }
            } else {
                sb.append("a ");
            }
            restante = restante % ANO;
        }

        long dia = restante / DIA;
        if (!ignorarDias && dia > 0) {
            sb.append(dia);
            if (extendido) {
                if (dia == 1) {
                    sb.append(" dia ");
                } else {
                    sb.append(" dias ");
                }
            } else {
                sb.append("d ");
            }
            restante = restante % DIA;
        }

        long hora = restante / HORA;
        if (!ignorarHoras && hora > 0) {
            sb.append(hora);
            if (extendido) {
                if (hora == 1) {
                    sb.append(" hora ");
                } else {
                    sb.append(" horas ");
                }
            } else {
                sb.append("h ");
            }
            restante = restante % HORA;
        }

        long minuto = restante / MINUTO;
        if (!ignorarMinutos && minuto > 0) {
            sb.append(minuto);
            if (extendido) {
                if (minuto == 1) {
                    sb.append(" minuto ");
                } else {
                    sb.append(" minutos ");
                }
            } else {
                sb.append("m ");
            }
            restante = restante % MINUTO;
        }

        long segundo = restante / SEGUNDO;
        if (!ignorarSegundos && segundo > 0) {
            sb.append(segundo);
            if (extendido) {
                if (segundo == 1) {
                    sb.append(" segundo ");
                } else {
                    sb.append(" segundos ");
                }
            } else {
                sb.append("s ");
            }
            restante = restante % SEGUNDO;
        }

        if (!ignorarMilis && restante > 0) {
            sb.append(restante);
            if (extendido) {
                if (restante == 1) {
                    sb.append(" milisegundo ");
                } else {
                    sb.append(" milisegundos ");
                }
            } else {
                sb.append("ms ");
            }
        }

        return sb.toString().trim();
    }

    /**
     * Convierte un objeto Date (Date,Timestamp, etc) a GregorianCalendar.
     * La principal utilidad de esta clase es evitar tener que repetir el codigo:
     *  GregorianCalendar cal = null;
     *  if (fecha != null) {
     *      cal = new GregorianCalendar();
     *      cal.setTime(fecha);
     *  }
     *  En la recuperacion de fechas desde consultas sql. Así se utilizaría:
     * setFecha(Fechas.toGregorianCalendar(res.getTimestamp("fecha"))); 
     * @param fecha java.util.Date con la fecha.
     * @return La fecha en formato e GregorianCalendar o nulo si la fecha es nula.
     */
    public static GregorianCalendar toGregorianCalendar(Date fecha) {
        GregorianCalendar cal = null;
        if (fecha != null) {
            cal = new GregorianCalendar();
            cal.setTime(fecha);
        }
        return cal;
    }
}
