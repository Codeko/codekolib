/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeko.util;

import java.util.Locale;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author codeko
 */
public class NumTest {

    public NumTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getInt method, of class Num.
     */
    @Test(timeout = 2)
    public void getInt() {
        System.out.println("getInt");
        assertEquals(999, Num.getInt("999"));
        assertEquals(99, Num.getInt(99));
        assertEquals(99, Num.getInt(99f));
        assertEquals(99, Num.getInt(99d));
        assertEquals(99, Num.getInt("99 "));
        assertEquals(99, Num.getInt(new Double(99)));
        assertEquals(99, Num.getInt(new Integer(99)));
        assertEquals(0, Num.getInt("asd"));
        assertEquals(0, Num.getInt(null));
        assertEquals(0, Num.getInt(""));
        assertEquals(-999, Num.getInt("-999"));
    }

    /**
     * Test of getFloat method, of class Num.
     */
    @Test(timeout = 2)
    public void getFloat() {
        System.out.println("getFloat");
        assertEquals(99.23f, Num.getFloat("99.23"),0);
        assertEquals(99.23f, Num.getFloat(99.23),0);
        assertEquals(99.23f, Num.getFloat(99.23f),0);
        assertEquals(99.23f, Num.getFloat(99.23d),0);
        assertEquals(99.23f, Num.getFloat("99.23 "),0);
        assertEquals(99.23f, Num.getFloat(new Double(99.23)),0);
        assertEquals(99.0f, Num.getFloat(new Integer(99)),0);
        assertEquals(0f, Num.getFloat("asd"),0);
        assertEquals(0f, Num.getFloat(null),0);
        assertEquals(0f, Num.getFloat(""),0);
        assertEquals(-999.998f, Num.getFloat("-999.998"),0);
    }

    /**
     * Test of getDouble method, of class Num.
     */
    @Test(timeout = 2)
    public void getDouble() {
        System.out.println("getDouble");
        assertEquals(99.23d, Num.getDouble("99.23"),0);
        assertEquals(99.23d, Num.getDouble(99.23),0);
        assertEquals(99.23d, Num.getDouble(99.23d),0);
        assertEquals(99.23d, Num.getDouble("99.23 "),0);
        assertEquals(99.0d, Num.getDouble(new Integer(99)),0);
        assertEquals(0d, Num.getDouble("asd"),0);
        assertEquals(0d, Num.getDouble(null),0);
        assertEquals(0d, Num.getDouble(""),0);
        assertEquals(-999.998d, Num.getDouble("-999.998"),0);
    }

    /**
     * Test of limpiar method, of class Num.
     */
    @Test(timeout = 5)
    public void limpiar() {
        System.out.println("limpiar");
        assertEquals("123", Num.limpiar(" 1sd% ' *as2f3sdf "));
    }

    /**
     * Test of limpiarDecimal method, of class Num.
     */
    @Test(timeout = 5)
    public void limpiarDecimal() {
        System.out.println("limpiarDecimal");
        assertEquals("12,3", Num.limpiarDecimal(" 1sd% ' *as2,f3sdf "));
        assertEquals("12.3", Num.limpiarDecimal(" 1sd% ' *as2.f3sdf "));
    }

    /**
     * Test of esNumero method, of class Num.
     */
    @Test(timeout = 2)
    public void esNumero() {
        System.out.println("esNumero");
        assertTrue(Num.esNumero('1'));
        assertTrue(Num.esNumero('2'));
        assertTrue(Num.esNumero('3'));
        assertTrue(Num.esNumero('4'));
        assertTrue(Num.esNumero('5'));
        assertTrue(Num.esNumero('6'));
        assertTrue(Num.esNumero('7'));
        assertTrue(Num.esNumero('8'));
        assertTrue(Num.esNumero('9'));
        assertTrue(Num.esNumero('0'));
        assertFalse(Num.esNumero('a'));
        assertFalse(Num.esNumero('J'));
        assertFalse(Num.esNumero('%'));
        assertFalse(Num.esNumero('&'));
        assertFalse(Num.esNumero('_'));
    }

    /**
     * Test of formatearDinero method, of class Num.
     */
    @Test(timeout = 2)
    public void formatearDinero() {
        System.out.println("formatearDinero");
        assertEquals("10.001,56 €", Num.formatearDinero("10001.558", new Locale("es", "Es")));
        assertEquals("10.001,56 €", Num.formatearDinero(10001.558d, new Locale("es", "Es")));
    }

    /**
     * Test of getDecimal method, of class Num.
     */
    @Test(timeout = 5)
    public void getDecimal() {
        System.out.println("getDecimal");
        assertEquals(10.23d, Num.getDecimal("10.23"),0);
        assertEquals(10.23d, Num.getDecimal("1.0.23"),0);
        assertEquals(10.23d, Num.getDecimal("1.0,23"),0);
        assertEquals(10.23d, Num.getDecimal("asd1as0.2g3sdf"),0);
        assertEquals(1000.23d, Num.getDecimal("1,000.23"),0);
        assertEquals(1000.23d, Num.getDecimal("1.000,23"),0);
        assertEquals(1000000.23d, Num.getDecimal("1,000,000.23"),0);
        assertEquals(1000000.23d, Num.getDecimal("1.000.000,23"),0);
        assertEquals(1000000.23d, Num.getDecimal("1 000 000,23"),0);
        assertEquals(1000000.23d, Num.getDecimal("1 000 000.23"),0);
        assertEquals(10001.56d, Num.getDecimal("10.001,56 €"),0);
        assertEquals(10001.56d, Num.getDecimal("El importe total asciende a 10.001,56 €"),0);
    }

    /**
     * Test of max method, of class Num.
     */
    @Test(timeout = 2)
    public void max() {
        System.out.println("max");
        assertEquals(Num.max(1,2,3,4), 4);
        assertEquals(Num.max(2.5d,2f,null,6.234,4), 6.234);
    }

    /**
     * Test of getLong method, of class Num.
     */
    @Test
    public void getLong() {
        System.out.println("getLong");
        Object obj = "12345678901234";
        long expResult = 12345678901234L;
        long result = Num.getLong(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of min method, of class Num.
     */
    @Test
    public void min() {
        System.out.println("min");
        assertEquals(1, Num.min(7,5,10.2,4f,1));
    }

    /**
     * Test of round method, of class Num.
     */
    @Test
    public void round() {
        System.out.println("round");
        assertEquals(40.39, Num.round(40.3922,2),0);
        assertEquals(40.40, Num.round(40.3952,2),0);
    }

    /**
     * Test of getInt method, of class Num.
     */
    @Test
    public void testGetInt_Object() {
        System.out.println("getInt");
        Object obj = null;
        int expResult = 0;
        int result = Num.getInt(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInt method, of class Num.
     */
    @Test
    public void testGetInt_Object_int() {
        System.out.println("getInt");
        Object obj = null;
        int porDefecto = 0;
        int expResult = 0;
        int result = Num.getInt(obj, porDefecto);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFloat method, of class Num.
     */
    @Test
    public void testGetFloat_Object() {
        System.out.println("getFloat");
        Object obj = null;
        float expResult = 0.0F;
        float result = Num.getFloat(obj);
        assertEquals(expResult, result,0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFloat method, of class Num.
     */
    @Test
    public void testGetFloat_Object_float() {
        System.out.println("getFloat");
        Object obj = null;
        float porDefecto = 0.0F;
        float expResult = 0.0F;
        float result = Num.getFloat(obj, porDefecto);
        assertEquals(expResult, result,0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDouble method, of class Num.
     */
    @Test
    public void testGetDouble_Object() {
        System.out.println("getDouble");
        Object obj = null;
        double expResult = 0.0;
        double result = Num.getDouble(obj);
        assertEquals(expResult, result,0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDouble method, of class Num.
     */
    @Test
    public void testGetDouble_Object_double() {
        System.out.println("getDouble");
        Object obj = null;
        double porDefecto = 0.0;
        double expResult = 0.0;
        double result = Num.getDouble(obj, porDefecto);
        assertEquals(expResult, result,0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLong method, of class Num.
     */
    @Test
    public void testGetLong_Object() {
        System.out.println("getLong");
        Object obj = null;
        long expResult = 0L;
        long result = Num.getLong(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLong method, of class Num.
     */
    @Test
    public void testGetLong_Object_long() {
        System.out.println("getLong");
        Object obj = null;
        long porDefecto = 0L;
        long expResult = 0L;
        long result = Num.getLong(obj, porDefecto);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of limpiar method, of class Num.
     */
    @Test
    public void testLimpiar() {
        System.out.println("limpiar");
        String texto = "";
        String expResult = "";
        String result = Num.limpiar(texto);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of limpiarDecimal method, of class Num.
     */
    @Test
    public void testLimpiarDecimal() {
        System.out.println("limpiarDecimal");
        String texto = "";
        String expResult = "";
        String result = Num.limpiarDecimal(texto);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of esNumero method, of class Num.
     */
    @Test
    public void testEsNumero_char() {
        System.out.println("esNumero");
        char caracter = ' ';
        boolean expResult = false;
        boolean result = Num.esNumero(caracter);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of esNumero method, of class Num.
     */
    @Test
    public void testEsNumero_String() {
        System.out.println("esNumero");
        String cadena = "";
        boolean expResult = false;
        boolean result = Num.esNumero(cadena);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of formatearDinero method, of class Num.
     */
    @Test
    public void testFormatearDinero_Object() {
        System.out.println("formatearDinero");
        Object dinero = null;
        String expResult = "";
        String result = Num.formatearDinero(dinero);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of formatearDinero method, of class Num.
     */
    @Test
    public void testFormatearDinero_Object_Locale() {
        System.out.println("formatearDinero");
        Object dinero = null;
        Locale locale = null;
        String expResult = "";
        String result = Num.formatearDinero(dinero, locale);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of formatearDinero method, of class Num.
     */
    @Test
    public void testFormatearDinero_double() {
        System.out.println("formatearDinero");
        double dinero = 0.0;
        String expResult = "";
        String result = Num.formatearDinero(dinero);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of formatearDinero method, of class Num.
     */
    @Test
    public void testFormatearDinero_double_Locale() {
        System.out.println("formatearDinero");
        double dinero = 0.0;
        Locale locale = null;
        String expResult = "";
        String result = Num.formatearDinero(dinero, locale);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of max method, of class Num.
     */
    @Test
    public void testMax() {
        System.out.println("max");
        Number[] numeros = null;
        Number expResult = null;
        Number result = Num.max(numeros);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of min method, of class Num.
     */
    @Test
    public void testMin() {
        System.out.println("min");
        Number[] numeros = null;
        Number expResult = null;
        Number result = Num.min(numeros);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDecimal method, of class Num.
     */
    @Test
    public void testGetDecimal() {
        System.out.println("getDecimal");
        Object obj = null;
        double expResult = 0.0;
        double result = Num.getDecimal(obj);
        assertEquals(expResult, result,0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of round method, of class Num.
     */
    @Test
    public void testRound() {
        System.out.println("round");
        double numero = 0.0;
        int decimales = 0;
        double expResult = 0.0;
        double result = Num.round(numero, decimales);
        assertEquals(expResult, result,0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rand method, of class Num.
     */
    @Test
    public void testRand_int() {
        System.out.println("rand");
        int max = 0;
        int expResult = 0;
        int result = Num.rand(max);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rand method, of class Num.
     */
    @Test
    public void testRand_int_int() {
        System.out.println("rand");
        int min = 0;
        int max = 0;
        int expResult = 0;
        int result = Num.rand(min, max);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInt method, of class Num.
     */
    @Test
    public void testGetInt_Object_1args() {
        System.out.println("getInt");
        Object obj = null;
        int expResult = 0;
        int result = Num.getInt(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInt method, of class Num.
     */
    @Test
    public void testGetInt_Object_int_2args() {
        System.out.println("getInt");
        Object obj = null;
        int porDefecto = 0;
        int expResult = 0;
        int result = Num.getInt(obj, porDefecto);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFloat method, of class Num.
     */
    @Test
    public void testGetFloat_Object_1args() {
        System.out.println("getFloat");
        Object obj = null;
        float expResult = 0.0F;
        float result = Num.getFloat(obj);
        assertEquals(expResult, result,0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFloat method, of class Num.
     */
    @Test
    public void testGetFloat_Object_float_2args() {
        System.out.println("getFloat");
        Object obj = null;
        float porDefecto = 0.0F;
        float expResult = 0.0F;
        float result = Num.getFloat(obj, porDefecto);
        assertEquals(expResult, result,0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDouble method, of class Num.
     */
    @Test
    public void testGetDouble_Object_1args() {
        System.out.println("getDouble");
        Object obj = null;
        double expResult = 0.0;
        double result = Num.getDouble(obj);
        assertEquals(expResult, result,0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDouble method, of class Num.
     */
    @Test
    public void testGetDouble_Object_double_2args() {
        System.out.println("getDouble");
        Object obj = null;
        double porDefecto = 0.0;
        double expResult = 0.0;
        double result = Num.getDouble(obj, porDefecto);
        assertEquals(expResult, result,0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLong method, of class Num.
     */
    @Test
    public void testGetLong_Object_1args() {
        System.out.println("getLong");
        Object obj = null;
        long expResult = 0L;
        long result = Num.getLong(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLong method, of class Num.
     */
    @Test
    public void testGetLong_Object_long_2args() {
        System.out.println("getLong");
        Object obj = null;
        long porDefecto = 0L;
        long expResult = 0L;
        long result = Num.getLong(obj, porDefecto);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of limpiar method, of class Num.
     */
    @Test
    public void testLimpiar_String() {
        System.out.println("limpiar");
        String texto = "";
        String expResult = "";
        String result = Num.limpiar(texto);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of limpiarDecimal method, of class Num.
     */
    @Test
    public void testLimpiarDecimal_String() {
        System.out.println("limpiarDecimal");
        String texto = "";
        String expResult = "";
        String result = Num.limpiarDecimal(texto);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of esNumero method, of class Num.
     */
    @Test
    public void testEsNumero_char_1args() {
        System.out.println("esNumero");
        char caracter = ' ';
        boolean expResult = false;
        boolean result = Num.esNumero(caracter);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of esNumero method, of class Num.
     */
    @Test
    public void testEsNumero_String_1args() {
        System.out.println("esNumero");
        String cadena = "";
        boolean expResult = false;
        boolean result = Num.esNumero(cadena);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of formatearDinero method, of class Num.
     */
    @Test
    public void testFormatearDinero_Object_1args() {
        System.out.println("formatearDinero");
        Object dinero = null;
        String expResult = "";
        String result = Num.formatearDinero(dinero);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of formatearDinero method, of class Num.
     */
    @Test
    public void testFormatearDinero_Object_Locale_2args() {
        System.out.println("formatearDinero");
        Object dinero = null;
        Locale locale = null;
        String expResult = "";
        String result = Num.formatearDinero(dinero, locale);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of formatearDinero method, of class Num.
     */
    @Test
    public void testFormatearDinero_double_1args() {
        System.out.println("formatearDinero");
        double dinero = 0.0;
        String expResult = "";
        String result = Num.formatearDinero(dinero);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of formatearDinero method, of class Num.
     */
    @Test
    public void testFormatearDinero_double_Locale_2args() {
        System.out.println("formatearDinero");
        double dinero = 0.0;
        Locale locale = null;
        String expResult = "";
        String result = Num.formatearDinero(dinero, locale);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of max method, of class Num.
     */
    @Test
    public void testMax_NumberArr() {
        System.out.println("max");
        Number[] numeros = null;
        Number expResult = null;
        Number result = Num.max(numeros);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of min method, of class Num.
     */
    @Test
    public void testMin_NumberArr() {
        System.out.println("min");
        Number[] numeros = null;
        Number expResult = null;
        Number result = Num.min(numeros);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDecimal method, of class Num.
     */
    @Test
    public void testGetDecimal_Object() {
        System.out.println("getDecimal");
        Object obj = null;
        double expResult = 0.0;
        double result = Num.getDecimal(obj);
        assertEquals(expResult, result,0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of round method, of class Num.
     */
    @Test
    public void testRound_double_int() {
        System.out.println("round");
        double numero = 0.0;
        int decimales = 0;
        double expResult = 0.0;
        double result = Num.round(numero, decimales);
        assertEquals(expResult, result,0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rand method, of class Num.
     */
    @Test
    public void testRand_int_1args() {
        System.out.println("rand");
        int max = 0;
        int expResult = 0;
        int result = Num.rand(max);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rand method, of class Num.
     */
    @Test
    public void testRand_int_int_2args() {
        System.out.println("rand");
        int min = 0;
        int max = 0;
        int expResult = 0;
        int result = Num.rand(min, max);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
