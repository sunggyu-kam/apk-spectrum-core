package com.apkspectrum.tool.aapt;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;

public class AaptNativeWrapperTest {
    @Test
    public void testNativeLoading() {
        String version = AaptNativeWrapper.getVersion();

        assertNotNull(version);
        assertFalse(version.isEmpty());
    }
}
