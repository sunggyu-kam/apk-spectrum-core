package com.apkspectrum.util;

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
}
