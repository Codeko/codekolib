/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codeko.util;

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
public class StrTest {

    public StrTest() {
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
     * Test of lPad method, of class Str.
     */
    @Test(timeout = 1)
    public void lPad() {
        System.out.println("lPad");
        assertEquals("    cadena", Str.lPad("cadena", 10));
        assertEquals("cadena", Str.lPad("cadena", 3));
        assertEquals("cadena", Str.lPad("cadena", -3));
        assertEquals("   ", Str.lPad(null, 3));
    }

    /**
     * Test of rPad method, of class Str.
     */
    @Test(timeout = 1)
    public void rPad() {
        System.out.println("rPad");
        assertEquals("cadena    ",Str.rPad("cadena", 10));
        assertEquals("cadena",Str.rPad("cadena", 3));
        assertEquals("cadena",Str.rPad("cadena", -3));
        assertEquals("   ",Str.rPad(null, 3));
    }

    /**
     * Test of noNulo method, of class Str.
     */
    @Test(timeout = 1)
    public void noNulo() {
        System.out.println("noNulo");
        assertEquals(Str.noNulo(null), "");
        assertEquals(Str.noNulo("cadena"), "cadena");
        assertEquals(Str.noNulo(1), "1");
    }

    /**
     * Test of repetir method, of class Str.
     */
    @Test
    public void repetir() {
        System.out.println("repetir");
        assertEquals("     ", Str.repetir(" ", 5));
        assertEquals("", Str.repetir(" ", -3));
        assertEquals("", Str.repetir(null, 5));
    }

    /**
     * Test of lPad method, of class Str.
     */
    @Test
    public void testLPad_Object_int() {
        System.out.println("lPad");
        Object cadena = null;
        int tamano = 0;
        String expResult = "";
        String result = Str.lPad(cadena, tamano);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of lPad method, of class Str.
     */
    @Test
    public void testLPad_3args() {
        System.out.println("lPad");
        Object oCadena = null;
        int tamano = 0;
        char relleno = ' ';
        String expResult = "";
        String result = Str.lPad(oCadena, tamano, relleno);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rPad method, of class Str.
     */
    @Test
    public void testRPad_Object_int() {
        System.out.println("rPad");
        Object cadena = null;
        int tamano = 0;
        String expResult = "";
        String result = Str.rPad(cadena, tamano);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rPad method, of class Str.
     */
    @Test
    public void testRPad_3args() {
        System.out.println("rPad");
        Object oCadena = null;
        int tamano = 0;
        char relleno = ' ';
        String expResult = "";
        String result = Str.rPad(oCadena, tamano, relleno);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of noNulo method, of class Str.
     */
    @Test
    public void testNoNulo() {
        System.out.println("noNulo");
        Object obj = null;
        String expResult = "";
        String result = Str.noNulo(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of repetir method, of class Str.
     */
    @Test
    public void testRepetir() {
        System.out.println("repetir");
        String cadena = "";
        int repeticiones = 0;
        String expResult = "";
        String result = Str.repetir(cadena, repeticiones);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of lPad method, of class Str.
     */
    @Test
    public void testLPad_Object_int_2args() {
        System.out.println("lPad");
        Object cadena = null;
        int tamano = 0;
        String expResult = "";
        String result = Str.lPad(cadena, tamano);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of lPad method, of class Str.
     */
    @Test
    public void testLPad_3args_1() {
        System.out.println("lPad");
        Object oCadena = null;
        int tamano = 0;
        char relleno = ' ';
        String expResult = "";
        String result = Str.lPad(oCadena, tamano, relleno);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rPad method, of class Str.
     */
    @Test
    public void testRPad_Object_int_2args() {
        System.out.println("rPad");
        Object cadena = null;
        int tamano = 0;
        String expResult = "";
        String result = Str.rPad(cadena, tamano);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rPad method, of class Str.
     */
    @Test
    public void testRPad_3args_1() {
        System.out.println("rPad");
        Object oCadena = null;
        int tamano = 0;
        char relleno = ' ';
        String expResult = "";
        String result = Str.rPad(oCadena, tamano, relleno);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of noNulo method, of class Str.
     */
    @Test
    public void testNoNulo_Object() {
        System.out.println("noNulo");
        Object obj = null;
        String expResult = "";
        String result = Str.noNulo(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of repetir method, of class Str.
     */
    @Test
    public void testRepetir_String_int() {
        System.out.println("repetir");
        String cadena = "";
        int repeticiones = 0;
        String expResult = "";
        String result = Str.repetir(cadena, repeticiones);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
