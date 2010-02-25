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
public class StringEncrypterTest {

    public StringEncrypterTest() {
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
     * Test of encrypt method, of class StringEncrypter.
     */
    @Test
    public void testEncrypt() throws Exception {
        System.out.println("encrypt");
        String unencryptedString = "";
        StringEncrypter instance = new StringEncrypter();
        String expResult = "";
        String result = instance.encrypt(unencryptedString);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of decrypt method, of class StringEncrypter.
     */
    @Test
    public void testDecrypt() throws Exception {
        System.out.println("decrypt");
        String encryptedString = "";
        StringEncrypter instance = new StringEncrypter();
        String expResult = "";
        String result = instance.decrypt(encryptedString);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of encrypt method, of class StringEncrypter.
     */
    @Test
    public void testEncrypt_String() throws Exception {
        System.out.println("encrypt");
        String unencryptedString = "";
        StringEncrypter instance = new StringEncrypter();
        String expResult = "";
        String result = instance.encrypt(unencryptedString);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of decrypt method, of class StringEncrypter.
     */
    @Test
    public void testDecrypt_String() throws Exception {
        System.out.println("decrypt");
        String encryptedString = "";
        StringEncrypter instance = new StringEncrypter();
        String expResult = "";
        String result = instance.decrypt(encryptedString);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}