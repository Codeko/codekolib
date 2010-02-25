package com.codeko.swing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright Codeko Informática 2008
 * www.codeko.com
 * @author Codeko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CdkAutoTabla {
    static final short EDITABLE_AUTO = -1;
    static final short EDITABLE_SI = 1;
    static final short EDITABLE_NO = 0;
    /**
     * Define si se deben mostrar todos los campos del objeto o sólo aquellos que tengan la anotación (@see CdkAutoTablaCol)
     * @return true/false.
     */
    boolean mostrarTodos() default true;
    /**
     * Define si se debe intentar generar el título automáticamente según el nombre de campo
     * @return true/false
     */
    boolean autoTitulo() default true;
    /**
     * Define el archivo de recursos para la traducción del artículo
     * @return
     */
    String archivoRecursos() default "";
    /**
     * Si los campos deben ser editables o no. El modo EDITABLE_AUTO hace un campo editable si exite un método set
     * @return CdkAutoTabla.EDITABLE_AUTO, CdkAutoTabla.EDITABLE_SI, CdkAutoTabla.EDITABLE_NO. Por defecto CdkAutoTabla.EDITABLE_AUTO
     */
    short editable() default EDITABLE_AUTO;

    /**
     * Indica el número de padres que se debe ascender en busca de propiedades
     * @return
     */
    boolean procesarPadre() default false;
}