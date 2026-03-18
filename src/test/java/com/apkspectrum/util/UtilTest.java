package com.apkspectrum.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class UtilTest {

    @Test
    void testSleepWithoutExcpetion() {
        long sleepTime = 100;
        long startTime = System.currentTimeMillis();
        Util.sleepWithoutExcpetion(sleepTime);
        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;

        assertTrue(elapsed >= sleepTime - 10);
    }

    @Test
    void testGetMessageDigestWithByteArray() throws NoSuchAlgorithmException {
        String testData = "Hello, World!";
        byte[] data = testData.getBytes();
        String algorithm = "SHA-256";

        byte[] result = Util.getMessageDigest(data, algorithm);

        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] expected = md.digest(data);

        assertArrayEquals(expected, result);
    }

    @Test
    void testGetMessageDigestWithSupplier() throws NoSuchAlgorithmException {
        String testData = "Hello, World!";
        byte[] data = testData.getBytes();
        String algorithm = "SHA-256";

        byte[] result = Util.getMessageDigest(() -> data, algorithm);

        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] expected = md.digest(data);

        assertArrayEquals(expected, result);
    }

    @Test
    void testGetMessageDigestWithInvalidAlgorithm() {
        String testData = "Hello, World!";
        byte[] data = testData.getBytes();
        String algorithm = "INVALID-ALGORITHM";

        assertThrows(IllegalArgumentException.class, () -> {
            Util.getMessageDigest(data, algorithm);
        });
    }

    @Test
    void testToHexStringLowerCaseNoSeparator() {
        byte[] data = {(byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
        String result = Util.toHexString(data, '\0', false);
        String expected = "abcdef";

        assertEquals(expected, result);
    }

    @Test
    void testToHexStringUpperCaseWithSeparator() {
        byte[] data = {(byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
        String result = Util.toHexString(data, ':', true);
        String expected = "AB:CD:EF";

        assertEquals(expected, result);
    }

    @Test
    void testToHexStringEmptyArray() {
        byte[] data = {};
        String result = Util.toHexString(data, ':', true);
        String expected = "";

        assertEquals(expected, result);
    }
}