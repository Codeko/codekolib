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
 * @author Codeko
 */
public class OSTest {

    public OSTest() {
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
     * Test of isWindows method, of class OS.
     */
    @Test
    public void testIsWindows() {
        System.out.println("isWindows");
        boolean expResult = false;
        boolean result = OS.isWindows();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isWindows method, of class OS.
     */
    @Test
    public void testIsWindows1() {
        System.out.println("isWindows");
        boolean expResult = false;
        boolean result = OS.isWindows();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}