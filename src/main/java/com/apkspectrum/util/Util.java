package com.apkspectrum.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Supplier;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Util {
    static public void sleepWithoutExcpetion(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            log.trace(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public static byte[] getMessageDigest(@NonNull byte[] data, @NonNull String algorithm) {
        return getMessageDigest(() -> data, algorithm);
    }

    public static byte[] getMessageDigest(@NonNull Supplier<byte[]> dataSupplier,
            @NonNull String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            byte[] data = null, pre = null;
            while ((data = dataSupplier.get()) != null) {
                if (data == pre) break;
                messageDigest.update(pre = data);
            }
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Algorithm not found: " + algorithm, e);
        }
    }

    /**
     * Converts a byte array to hex string
     */
    public static String toHexString(byte[] block, char byteSeperate, boolean upperCase) {
        StringBuilder buf = new StringBuilder();
        int len = block.length;
        for (int i = 0; i < len; i++) {
            byte2hex(block[i], buf, upperCase);
            if (byteSeperate != '\0' && i < len - 1) buf.append(byteSeperate);
        }
        return buf.toString();
    }

    /**
     * Converts a byte to hex digit and writes to the supplied buffer
     */
    private static void byte2hex(byte b, StringBuilder buf, boolean upperCase) {
        char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F'};
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        if (upperCase) {
            if (high >= 10) high += 6;
            if (low >= 10) low += 6;
        }
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }
}
