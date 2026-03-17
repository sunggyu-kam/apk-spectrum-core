package com.apkspectrum.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.net.URI;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class JarURIBuilderTest {
    @BeforeAll
    static void setWindows() {
        System.setProperty("test", "true");
    }

    @Test
    void testSetJarFileWithString() {
        String jarFilePath = "test.jar";
        JarURIBuilder builder = new JarURIBuilder().setJarFile(jarFilePath);

        assertNotNull(builder);

        JarURI jarURI = builder.build();
        String expected = "jar:" + new File(jarFilePath).toURI().toString() + "!/";
        assertEquals(expected, jarURI.toString());
        assertEquals(new File(jarFilePath), jarURI.getJarFile());
    }

    @Test
    void testSetJarFileWithFile() {
        File jarFile = new File("test.jar");
        JarURIBuilder builder = new JarURIBuilder().setJarFile(jarFile);

        assertNotNull(builder);

        JarURI jarURI = builder.build();
        String expected = "jar:" + jarFile.toURI().toString() + "!/";
        assertEquals(expected, jarURI.toString());
        assertEquals(jarFile, jarURI.getJarFile());
    }

    @Test
    void testSetEntryWithString() {
        String jarFilePath = "test.jar";
        String entryPath = "META-INF/MANIFEST.MF";

        JarURIBuilder builder = new JarURIBuilder()
                .setJarFile(jarFilePath)
                .setEntry(entryPath);

        assertNotNull(builder);

        JarURI jarURI = builder.build();
        String expected = "jar:" + new File(jarFilePath).toURI().toString() + "!/" + entryPath;
        assertEquals(expected, jarURI.toString());
        assertEquals(new File(jarFilePath), jarURI.getJarFile());
        assertEquals(entryPath, jarURI.getEntryPath());
    }

    @Test
    void testSetEntryWithUri() {
        String jarFilePath = "test.jar";
        String entryPath = "META-INF/MANIFEST.MF";
        URI entryUri = URI.create(entryPath);

        JarURIBuilder builder = new JarURIBuilder()
                .setJarFile(jarFilePath)
                .setEntry(entryUri);

        assertNotNull(builder);

        JarURI jarURI = builder.build();
        String expected = "jar:" + new File(jarFilePath).toURI().toString() + "!/" + entryPath;
        assertEquals(expected, jarURI.toString());
        assertEquals(new File(jarFilePath), jarURI.getJarFile());
        assertEquals(entryPath, jarURI.getEntryPath());
    }

    @Test
    void testSetSymbolicPath() {
        String jarFilePath = "test.jar";
        String entryPath = "META-INF/MANIFEST.MF";
        String symbolicPath = "symbolic";

        JarURIBuilder builder = new JarURIBuilder()
                .setJarFile(jarFilePath)
                .setEntry(entryPath)
                .setSymbolicPath(symbolicPath);

        assertNotNull(builder);

        JarURI jarURI = builder.build();
        String expected = "jar:" + new File(jarFilePath).toURI().toString() + "!/" + entryPath;
        assertEquals(expected, jarURI.toString());
        assertEquals(new File(jarFilePath), jarURI.getJarFile());
        assertEquals(entryPath, jarURI.getEntryPath());
        assertEquals("META-INF/" + symbolicPath, jarURI.getSymbolicPath());
    }

    @Test
    void testSetSymbolicAbsolutePath() {
        String jarFilePath = "test.jar";
        String entryPath = "META-INF/MANIFEST.MF";
        String symbolicPath = "symbolic";
        String absoluteSymbolicPath = "/" + symbolicPath;

        JarURIBuilder builder1 = new JarURIBuilder()
                .setJarFile(jarFilePath)
                .setEntry(entryPath)
                .setSymbolicAbsolutePath(symbolicPath);

        JarURIBuilder builder2 = new JarURIBuilder()
                .setJarFile(jarFilePath)
                .setEntry(entryPath)
                .setSymbolicAbsolutePath(absoluteSymbolicPath);

        JarURI jarURI1 = builder1.build();
        JarURI jarURI2 = builder2.build();

        String expected = "jar:" + new File(jarFilePath).toURI().toString() + "!/" + entryPath;
        assertEquals(expected, jarURI1.toString());
        assertEquals(expected, jarURI2.toString());
        assertEquals(new File(jarFilePath), jarURI1.getJarFile());
        assertEquals(entryPath, jarURI1.getEntryPath());
        assertEquals(symbolicPath, jarURI1.getSymbolicPath());
    }

    @Test
    void testBuildWithoutJarFile() {
        JarURIBuilder builder = new JarURIBuilder();

        assertThrows(IllegalArgumentException.class, () -> {
            builder.build();
        });
    }

    @Test
    void testBuildWithoutEntryWithSymbolicPath() {
        String jarFilePath = "test.jar";
        String symbolicPath = "symbolic";

        JarURIBuilder builder = new JarURIBuilder()
                .setJarFile(jarFilePath)
                .setSymbolicPath(symbolicPath);

        assertThrows(IllegalArgumentException.class, () -> {
            builder.build();
        });
    }

    @Test
    void testBuildWithInvalidSymbolicPath() {
        String jarFilePath = "test.jar";
        String entryPath = "META-INF/MANIFEST.MF";
        String invalidSymbolicPath = "symbolic#invalid";

        JarURIBuilder builder = new JarURIBuilder()
                .setJarFile(jarFilePath)
                .setEntry(entryPath)
                .setSymbolicPath(invalidSymbolicPath);

        assertThrows(IllegalArgumentException.class, () -> {
            builder.build();
        });
    }

    @Test
    void testBuildWithValidSymbolicPath() {
        String jarFilePath = "test.jar";
        String entryPath = "META-INF/MANIFEST.MF";
        String symbolicPath = "symbolic";

        JarURIBuilder builder = new JarURIBuilder()
                .setJarFile(jarFilePath)
                .setEntry(entryPath)
                .setSymbolicPath(symbolicPath);

        assertDoesNotThrow(() -> {
            JarURI jarURI = builder.build();
            assertNotNull(jarURI);
        });
    }

    @Test
    void testFluentPattern() {
        String jarFilePath = "test.jar";
        String entryPath = "META-INF/MANIFEST.MF";
        String symbolicPath = "symbolic";

        JarURI jarURI = new JarURIBuilder()
                .setJarFile(jarFilePath)
                .setEntry(entryPath)
                .setSymbolicPath(symbolicPath)
                .build();

        String expected = "jar:" + new File(jarFilePath).toURI().toString() + "!/" + entryPath;
        assertEquals(expected, jarURI.toString());
        assertEquals(new File(jarFilePath), jarURI.getJarFile());
        assertEquals(entryPath, jarURI.getEntryPath());
        assertEquals("META-INF/" + symbolicPath, jarURI.getSymbolicPath());
    }
}