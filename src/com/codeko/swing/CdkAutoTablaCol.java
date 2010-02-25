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
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface CdkAutoTablaCol {

    /**
     * Define el título a mostrar en la cabecera de la tabla. Por defecto el nombre del campo
     * @return String con el título o "" para asignar el nombre del campo
     */
    String titulo() default "";

    /**
     * Define si se debe ignorar el campo
     * @return false muestra el campo en la tabla, true no lo muestra
     */
    boolean ignorar() default false;

    /**
     * Define el modo de edición específico del campo. El modo por defecto CdkAutoTabla.EDITABLE_AUTO usa el valor de
     * editable de CdkAutoTabla.
     * @return CdkAutoTabla.EDITABLE_AUTO, CdkAutoTabla.EDITABLE_SI, CdkAutoTabla.EDITABLE_NO. Por defecto CdkAutoTabla.EDITABLE_AUTO
     */
    short editable() default CdkAutoTabla.EDITABLE_AUTO;

    /**
     * Define el peso de cada campo a la hora de decidir el orden de las columnas. Por defecto es 0 y admite valores negativos.
     * @return int con el peso del campo.
     */
    int peso() default 0;
}
