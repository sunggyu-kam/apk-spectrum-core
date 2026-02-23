package com.apkspectrum.tool.aapt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class AaptNativeWrapperTest {
    @Test
    public void testNativeLoading() {
        AaptNativeWrapper.main(null);
    }
}
