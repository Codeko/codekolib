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
public class ObjTest {

    public ObjTest() {
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
     * Test of noNull method, of class Obj.
     */
    @Test(timeout = 1)
    public void noNull() {
        System.out.println("noNull");
        assertEquals("No Nulo", Obj.noNull(null, null, "No Nulo", "Tampoco nulo", 4));
        assertEquals(3, Obj.noNull(null, null, 3, "No Nulo", "Tampoco nulo", 4));
    }

    /**
     * Test of noNull method, of class Obj.
     */
    @Test
    public void testNoNull() {
        System.out.println("noNull");
        Object[] valores = null;
        Object expResult = null;
        Object result = Obj.noNull(valores);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of noNull method, of class Obj.
     */
    @Test
    public void testNoNull_ObjectArr() {
        System.out.println("noNull");
        Object[] valores = null;
        Object expResult = null;
        Object result = Obj.noNull(valores);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
