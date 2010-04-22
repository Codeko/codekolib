package com.codeko.local.es;

import com.codeko.util.Num;
import java.util.HashMap;

/**
 * Copyright Codeko Informática 2008
 * www.codeko.com
 * @author Codeko
 * Diversas utilidades relacionadas con las provincias españolas
 */
public class Provincias {

    private static HashMap<Integer, String> provincias = null;

    private static HashMap<Integer, String> getProvincias() {
        if (provincias == null) {
            provincias = new HashMap<Integer, String>();
            provincias.put(01, "Alava");
            provincias.put(02, "Albacete");
            provincias.put(03, "Alicante");
            provincias.put(04, "Almería");
            provincias.put(33, "Asturias");
            provincias.put(05, "Avila");
            provincias.put(06, "Badajoz");
            provincias.put(07, "Baleares");
            provincias.put(8, "Barcelona");
            provincias.put(9, "Burgos");
            provincias.put(10, "Cáceres");
            provincias.put(11, "Cádiz");
            provincias.put(39, "Cantabria");
            provincias.put(12, "Castellón");
            provincias.put(51, "Ceuta");
            provincias.put(13, "Ciudad Real");
            provincias.put(14, "Córdoba");
            provincias.put(15, "A Coruña");
            provincias.put(16, "Cuenca");
            provincias.put(17, "Girona");
            provincias.put(18, "Granada");
            provincias.put(19, "Guadalajara");
            provincias.put(20, "Guipuzcoa");
            provincias.put(21, "Huelva");
            provincias.put(22, "Huesca");
            provincias.put(23, "Jaen");
            provincias.put(24, "León");
            provincias.put(25, "Lérida");
            provincias.put(27, "Lugo");
            provincias.put(28, "Madrid");
            provincias.put(29, "Málaga");
            provincias.put(52, "Melilla");
            provincias.put(30, "Murcia");
            provincias.put(31, "Navarra");
            provincias.put(32, "Ourense");
            provincias.put(34, "Palencia");
            provincias.put(35, "Las Palmas");
            provincias.put(36, "Pontevedra");
            provincias.put(26, "La Rioja");
            provincias.put(37, "Salamanca");
            provincias.put(38, "S.C.Tenerife");
            provincias.put(40, "Segovia");
            provincias.put(41, "Sevilla");
            provincias.put(42, "Soria");
            provincias.put(43, "Tarragona");
            provincias.put(44, "Teruel");
            provincias.put(45, "Toledo");
            provincias.put(46, "Valencia");
            provincias.put(47, "Valladolid");
            provincias.put(48, "Vizcaya");
            provincias.put(49, "Zamora");
            provincias.put(50, "Zaragoza");
        }
        return provincias;
    }
    /**
     * Recupera el nombre de una provincia según el código de esta.
     * @param cod int con el código de la provincia
     * @return String con el nombre de la provincia
     */
    public static String getProvincia(int cod) {
        return getProvincias().get(cod);
    }

    /**
     * Recupera el nombre de la provincia a la que pertenece un código postal.
     * @param cp String con cualquier código postal
     * @return String con el nombre de lap provincia a la que pertenece el código postal indicado.
     */
    public static String getProvincia(String cp) {
        int cod = Num.getInt((cp.trim()+"  ").substring(0, 2));
        return getProvincia(cod);
    }
}
