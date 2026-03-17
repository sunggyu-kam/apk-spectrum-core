package com.apkspectrum.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class JarURITest {
    @BeforeAll
    static void setWindows() {
        System.setProperty("test", "true");
    }

    @Test
    void testConstructorWithString() {
        String jarFilePath = "test.jar";
        JarURI jarURI = new JarURI(jarFilePath);

        String expected = "jar:" + new File(jarFilePath).toURI().toString() + "!/";
        assertEquals(expected, jarURI.toString());
        assertEquals(new File(jarFilePath), jarURI.getJarFile());
        assertEquals("", jarURI.getEntryPath());
    }

    @Test
    void testConstructorWithFile() {
        File jarFile = new File("test.jar");
        JarURI jarURI = new JarURI(jarFile);

        String expected = "jar:" + jarFile.toURI().toString() + "!/";
        assertEquals(expected, jarURI.toString());
        assertEquals(jarFile, jarURI.getJarFile());
        assertEquals("", jarURI.getEntryPath());
    }

    @Test
    void testConstructorWithStringAndEntryPath() {
        String jarFilePath = "test.jar";
        String entryPath = "META-INF/MANIFEST.MF";
        JarURI jarURI = new JarURI(jarFilePath, entryPath);
        JarURI jarURI2 = new JarURI(jarFilePath, "/" + entryPath);

        String expected = "jar:" + new File(jarFilePath).toURI().toString() + "!/" + entryPath;
        assertEquals(expected, jarURI.toString());
        assertEquals(expected, jarURI2.toString());
        assertEquals(new File(jarFilePath), jarURI.getJarFile());
        assertEquals(entryPath, jarURI.getEntryPath());
    }

    @Test
    void testConstructorWithStringAndEntryUri() {
        String jarFilePath = "test.jar";
        String entryPath = "META-INF/MANIFEST.MF";
        URI entryUri1 = URI.create(entryPath);
        URI entryUri2 = URI.create("/" + entryPath);
        JarURI jarURI = new JarURI(jarFilePath, entryUri1);
        JarURI jarURI2 = new JarURI(jarFilePath, entryUri2);

        String expected = "jar:" + new File(jarFilePath).toURI().toString() + "!/" + entryPath;
        assertEquals(expected, jarURI.toString());
        assertEquals(expected, jarURI2.toString());
        assertEquals(new File(jarFilePath), jarURI.getJarFile());
        assertEquals(entryPath, jarURI.getEntryPath());
    }

    @Test
    void testConstructorWithFileAndEntryPath() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        JarURI jarURI = new JarURI(jarFile, entryPath);
        JarURI jarURI2 = new JarURI(jarFile, "/" + entryPath);

        String expected = "jar:" + jarFile.toURI().toString() + "!/" + entryPath;
        assertEquals(expected, jarURI.toString());
        assertEquals(expected, jarURI2.toString());
        assertEquals(jarFile, jarURI.getJarFile());
        assertEquals(entryPath, jarURI.getEntryPath());
    }

    @Test
    void testConstructorWithFileAndEntryUri() {
        File jarFile = new File("test.jar");
        URI entryUri = URI.create("META-INF/MANIFEST.MF");
        URI entryUri2 = URI.create("/META-INF/MANIFEST.MF");
        URI entryUri3 = URI.create("META-INF/TEMP/.././MANIFEST.MF");
        URI entryUri4 = URI.create("/META-INF/TEMP/.././MANIFEST.MF");
        JarURI jarURI = new JarURI(jarFile, entryUri);
        JarURI jarURI2 = new JarURI(jarFile, entryUri2);
        JarURI jarURI3 = new JarURI(jarFile, entryUri3);
        JarURI jarURI4 = new JarURI(jarFile, entryUri4);

        String expected = "jar:" + jarFile.toURI().toString() + "!/" + entryUri.toString();
        assertEquals(expected, jarURI.toString());
        assertEquals(expected, jarURI2.toString());
        assertEquals(expected, jarURI3.toString());
        assertEquals(expected, jarURI4.toString());
        assertEquals(entryUri, jarURI.getEntry());
    }

    @Test
    void testCreateWithString() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        String jarUriString = "jar:" + jarFile.toURI().toString() + "!/" + entryPath;
        String jarUriString2 = "jar:" + jarFile.getPath() + "!/" + entryPath;
        String jarUriString3 = "jar:file:" + jarFile.getPath() + "!/" + entryPath;
        String jarUriString4 = jarFile.toURI().toString() + "!/" + entryPath;
        String jarUriString5 = "file:" + jarFile.getPath() + "!/" + entryPath;
        String jarUriString6 = jarFile.getPath() + "!/" + entryPath;
        String jarUriString7 = "jar:file:" + jarFile.getAbsolutePath() + "!/" + entryPath;
        String jarUriString8 = "file:" + jarFile.getAbsolutePath() + "!/" + entryPath;
        String jarUriString9 = jarFile.getAbsolutePath() + "!/" + entryPath;
        JarURI jarURI1 = JarURI.create(jarUriString);
        JarURI jarURI2 = JarURI.create(jarUriString2);
        JarURI jarURI3 = JarURI.create(jarUriString3);
        JarURI jarURI4 = JarURI.create(jarUriString4);
        JarURI jarURI5 = JarURI.create(jarUriString5);
        JarURI jarURI6 = JarURI.create(jarUriString6);
        JarURI jarURI7 = JarURI.create(jarUriString7);
        JarURI jarURI8 = JarURI.create(jarUriString8);
        JarURI jarURI9 = JarURI.create(jarUriString9);

        assertEquals(jarUriString, jarURI1.toString());
        assertEquals(jarUriString, jarURI2.toString());
        assertEquals(jarUriString, jarURI3.toString());
        assertEquals(jarUriString, jarURI4.toString());
        assertEquals(jarUriString, jarURI5.toString());
        assertEquals(jarUriString, jarURI6.toString());
        assertEquals(jarUriString, jarURI7.toString());
        assertEquals(jarUriString, jarURI8.toString());
        assertEquals(jarUriString, jarURI9.toString());
        assertEquals(jarFile.getAbsoluteFile(), jarURI1.getJarFile());
        assertEquals(jarFile, jarURI2.getJarFile());
        assertEquals(jarFile, jarURI3.getJarFile());
        assertEquals(jarFile.getAbsoluteFile(), jarURI4.getJarFile());
        assertEquals(jarFile, jarURI5.getJarFile());
        assertEquals(jarFile, jarURI6.getJarFile());
        assertEquals(jarFile.getAbsoluteFile(), jarURI7.getJarFile());
        assertEquals(jarFile.getAbsoluteFile(), jarURI8.getJarFile());
        assertEquals(jarFile.getAbsoluteFile(), jarURI9.getJarFile());
        assertEquals(entryPath, jarURI1.getEntryPath());
        assertEquals(entryPath, jarURI2.getEntryPath());
        assertEquals(entryPath, jarURI3.getEntryPath());
        assertEquals(entryPath, jarURI4.getEntryPath());
        assertEquals(entryPath, jarURI5.getEntryPath());
        assertEquals(entryPath, jarURI6.getEntryPath());
        assertEquals(entryPath, jarURI7.getEntryPath());
        assertEquals(entryPath, jarURI8.getEntryPath());
        assertEquals(entryPath, jarURI9.getEntryPath());
    }

    @Test
    void testCreateWithUri() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        String jarUriString0 = "jar:" + jarFile.toURI().toString() + "!/";
        String jarUriString1 = "jar:" + jarFile.toURI().toString() + "!/" + entryPath;
        String jarUriString2 = "jar:" + jarFile.getPath() + "!/" + entryPath;
        String jarUriString3 = "jar:file:" + jarFile.getPath() + "!/" + entryPath;
        String jarUriString4 = jarFile.toURI().toString() + "!/" + entryPath;
        String jarUriString5 = "file:" + jarFile.getPath();
        String jarUriString6 = jarFile.getPath();
        JarURI jarURI1 = JarURI.create(URI.create(jarUriString1));
        JarURI jarURI2 = JarURI.create(URI.create(jarUriString2));
        JarURI jarURI3 = JarURI.create(URI.create(jarUriString3));
        JarURI jarURI4 = JarURI.create(URI.create(jarUriString4));
        JarURI jarURI5 = JarURI.create(URI.create(jarUriString5));
        JarURI jarURI6 = JarURI.create(URI.create(jarUriString6));

        assertEquals(jarUriString1, jarURI1.toString());
        assertEquals(jarUriString1, jarURI1.toString());
        assertEquals(jarUriString1, jarURI2.toString());
        assertEquals(jarUriString1, jarURI3.toString());
        assertEquals(jarUriString1, jarURI4.toString());
        assertEquals(jarUriString0, jarURI5.toString());
        assertEquals(jarUriString0, jarURI6.toString());
        assertEquals(jarFile.getAbsoluteFile(), jarURI1.getJarFile());
        assertEquals(jarFile, jarURI2.getJarFile());
        assertEquals(jarFile, jarURI3.getJarFile());
        assertEquals(jarFile.getAbsoluteFile(), jarURI4.getJarFile());
        assertEquals(jarFile, jarURI5.getJarFile());
        assertEquals(jarFile, jarURI6.getJarFile());
        assertEquals(entryPath, jarURI1.getEntryPath());
        assertEquals(entryPath, jarURI2.getEntryPath());
        assertEquals(entryPath, jarURI3.getEntryPath());
        assertEquals(entryPath, jarURI4.getEntryPath());
        assertEquals("", jarURI5.getEntryPath());
        assertEquals("", jarURI6.getEntryPath());
    }

    @Test
    void testResolveWithString() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/";
        JarURI jarURI = new JarURI(jarFile, entryPath);

        JarURI resolved0 = jarURI.resolve((String) null);
        JarURI resolved1 = jarURI.resolve("MANIFEST.MF");
        JarURI resolved2 = jarURI.resolve("/com/example/../Main.class");
        JarURI resolved3 = jarURI.resolve("../setting/app.properties");
        JarURI resolved4 = jarURI.resolve("./TEMP/README");

        String expected0 = "jar:" + jarFile.toURI().toString() + "!/META-INF/";
        String expected1 = "jar:" + jarFile.toURI().toString() + "!/META-INF/MANIFEST.MF";
        String expected2 = "jar:" + jarFile.toURI().toString() + "!/com/Main.class";
        String expected3 = "jar:" + jarFile.toURI().toString() + "!/setting/app.properties";
        String expected4 = "jar:" + jarFile.toURI().toString() + "!/META-INF/TEMP/README";

        assertEquals(expected0, resolved0.toString());
        assertEquals(expected1, resolved1.toString());
        assertEquals(expected2, resolved2.toString());
        assertEquals(expected3, resolved3.toString());
        assertEquals(expected4, resolved4.toString());

        assertEquals(jarFile, resolved0.getJarFile());
        assertEquals("META-INF/", resolved0.getEntryPath());
        assertEquals("META-INF/MANIFEST.MF", resolved1.getEntryPath());
    }

    @Test
    void testResolveWithUri() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/";
        JarURI jarURI = new JarURI(jarFile, entryPath);

        JarURI resolved0 = jarURI.resolve((URI) null);
        JarURI resolved1 = jarURI.resolve(URI.create("MANIFEST.MF"));
        JarURI resolved2 = jarURI.resolve(URI.create("/com/example/../Main.class"));
        JarURI resolved3 = jarURI.resolve(URI.create("../setting/app.properties"));
        JarURI resolved4 = jarURI.resolve(URI.create("./TEMP/README"));

        String expected0 = "jar:" + jarFile.toURI().toString() + "!/META-INF/";
        String expected1 = "jar:" + jarFile.toURI().toString() + "!/META-INF/MANIFEST.MF";
        String expected2 = "jar:" + jarFile.toURI().toString() + "!/com/Main.class";
        String expected3 = "jar:" + jarFile.toURI().toString() + "!/setting/app.properties";
        String expected4 = "jar:" + jarFile.toURI().toString() + "!/META-INF/TEMP/README";

        assertEquals(expected0, resolved0.toString());
        assertEquals(expected1, resolved1.toString());
        assertEquals(expected2, resolved2.toString());
        assertEquals(expected3, resolved3.toString());
        assertEquals(expected4, resolved4.toString());

        assertEquals(jarFile, resolved0.getJarFile());
        assertEquals("META-INF/", resolved0.getEntryPath());
        assertEquals("META-INF/MANIFEST.MF", resolved1.getEntryPath());
    }

    @Test
    void testGetJarFile() {
        File jarFile = new File("test.jar");
        JarURI jarURI = new JarURI(jarFile);
        JarURI jarURI2 = new JarURI(jarFile.getAbsolutePath());

        assertEquals(jarFile, jarURI.getJarFile());
        assertEquals(jarFile.getAbsolutePath(), jarURI2.getJarFile().getPath());
    }

    @Test
    void testGetJarPath() {
        File jarFile = new File("test.jar");
        JarURI jarURI = new JarURI(jarFile);
        JarURI jarURI2 = new JarURI(jarFile.getAbsolutePath());

        assertEquals(jarFile.getPath(), jarURI.getJarPath());
        assertEquals(jarFile.getAbsolutePath(), jarURI2.getJarPath());
    }

    @Test
    void testGetJarAbsolutePath() {
        File jarFile = new File("test.jar");
        JarURI jarURI = new JarURI(jarFile);

        assertEquals(jarFile.getAbsolutePath(), jarURI.getJarAbsolutePath());
    }

    @Test
    void testGetRawEntry() {
        File jarFile = new File("test.jar");
        String entryPath0 = "META-INF/./MANIFEST.MF";
        String entryPath1 = "META-INF/../MANIFEST.MF";
        String entryPath2 = "/META-INF/../MANIFEST.MF";
        String entryPath3 = "./META-INF/../MANIFEST.MF";
        JarURI jarURI0 = new JarURI(jarFile, entryPath0);
        JarURI jarURI1 = new JarURI(jarFile, entryPath1);
        JarURI jarURI2 = new JarURI(jarFile, entryPath2);
        JarURI jarURI3 = new JarURI(jarFile, entryPath3);

        URI expected0 = URI.create(entryPath0);
        URI expected1 = URI.create(entryPath1);
        URI expected2 = URI.create(entryPath2);
        URI expected3 = URI.create(entryPath3);

        assertEquals(expected0, jarURI0.getRawEntry());
        assertEquals(expected1, jarURI1.getRawEntry());
        assertEquals(expected2, jarURI2.getRawEntry());
        assertEquals(expected3, jarURI3.getRawEntry());
    }

    @Test
    void testGetEntryName() {
        File jarFile = new File("test.jar");
        String entryPath1 = "META-INF";
        String entryPath2 = "META-INF/";
        String entryPath3 = "META-INF/MANIFEST.MF";
        String entryPath4 = "META-INF/TEMP/";
        String entryPath5 = "/META-INF/TEMP/TEST";
        String entryPath6 = "/META-INF/TEMP/TEST/";
        JarURI jarURI0 = new JarURI(jarFile);
        JarURI jarURI1 = new JarURI(jarFile, entryPath1);
        JarURI jarURI2 = new JarURI(jarFile, entryPath2);
        JarURI jarURI3 = new JarURI(jarFile, entryPath3);
        JarURI jarURI4 = new JarURI(jarFile, entryPath4);
        JarURI jarURI5 = new JarURI(jarFile, entryPath5);
        JarURI jarURI6 = new JarURI(jarFile, entryPath6);

        assertEquals("", jarURI0.getEntryName());
        assertEquals("META-INF", jarURI1.getEntryName());
        assertEquals("META-INF", jarURI2.getEntryName());
        assertEquals("MANIFEST.MF", jarURI3.getEntryName());
        assertEquals("TEMP", jarURI4.getEntryName());
        assertEquals("TEST", jarURI5.getEntryName());
        assertEquals("TEST", jarURI6.getEntryName());
    }

    @Test
    void testIsDirectoryAndIsFile() {
        File jarFile = new File("test.jar");
        String entryPath1 = "META-INF";
        String entryPath2 = "META-INF/";
        JarURI jarURI0 = new JarURI(jarFile);
        JarURI jarURI1 = new JarURI(jarFile, entryPath1);
        JarURI jarURI2 = new JarURI(jarFile, entryPath2);

        assertTrue(jarURI0.isDirectory());
        assertFalse(jarURI0.isFile());
        assertFalse(jarURI1.isDirectory());
        assertTrue(jarURI1.isFile());
        assertTrue(jarURI2.isDirectory());
        assertFalse(jarURI2.isFile());
    }

    @Test
    void testIsSymbolic() {
        File jarFile = new File("test.jar");
        String entryPath1 = "META-INF#/MINF";
        JarURI jarURI0 = new JarURI(jarFile);
        JarURI jarURI1 = new JarURI(jarFile, entryPath1);

        assertFalse(jarURI0.isSymbolic());
        assertTrue(jarURI1.isSymbolic());
    }

    @Test
    void testGetEntryPath() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        JarURI jarURI0 = new JarURI(jarFile);
        JarURI jarURI1 = new JarURI(jarFile, entryPath);
        JarURI jarURI2 = new JarURI(jarFile, "/" + entryPath);
        JarURI jarURI3 = new JarURI(jarFile, "./" + entryPath);

        assertEquals("", jarURI0.getEntryPath());
        assertEquals(entryPath, jarURI1.getEntryPath());
        assertEquals(entryPath, jarURI2.getEntryPath());
        assertEquals(entryPath, jarURI3.getEntryPath());
    }

    @Test
    void testGetEntryPathWithString() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/";
        JarURI jarURI = new JarURI(jarFile, entryPath);
        String expected0 = "META-INF/";
        String expected1 = "com/Main.class";
        String expected2 = "setting/app.properties";

        assertEquals(expected0, jarURI.getEntryPath((String) null));
        assertEquals(expected1, jarURI.getEntryPath("/com/example/../Main.class"));
        assertEquals(expected2, jarURI.getEntryPath("../setting/app.properties"));
    }

    @Test
    void testGetEntryPathWithUri() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/";
        JarURI jarURI = new JarURI(jarFile, entryPath);
        String expected0 = "META-INF/";
        String expected1 = "com/Main.class";
        String expected2 = "setting/app.properties";

        assertEquals(expected0, jarURI.getEntryPath((URI) null));
        assertEquals(expected1, jarURI.getEntryPath(URI.create("/com/example/../Main.class")));
        assertEquals(expected2, jarURI.getEntryPath(URI.create("../setting/app.properties")));
    }

    @Test
    void testGetEntry() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        JarURI jarURI0 = new JarURI(jarFile);
        JarURI jarURI1 = new JarURI(jarFile, entryPath);
        JarURI jarURI2 = new JarURI(jarFile, "/" + entryPath);
        JarURI jarURI3 = new JarURI(jarFile, "./" + entryPath);
        URI expected = URI.create(entryPath);

        assertEquals(URI.create(""), jarURI0.getEntry());
        assertEquals(expected, jarURI1.getEntry());
        assertEquals(expected, jarURI2.getEntry());
        assertEquals(expected, jarURI3.getEntry());
    }

    @Test
    void testGetEntryWithString() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        JarURI jarURI = new JarURI(jarFile, entryPath);
        URI expected0 = URI.create("META-INF/MANIFEST.MF");
        URI expected1 = URI.create("com/Main.class");
        URI expected2 = URI.create("setting/app.properties");

        assertEquals(expected0, jarURI.getEntry((String) null));
        assertEquals(expected1, jarURI.getEntry("/com/example/../Main.class"));
        assertEquals(expected2, jarURI.getEntry("../setting/app.properties"));
    }

    @Test
    void testGetEntryWithUri() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        JarURI jarURI = new JarURI(jarFile, entryPath);
        URI expected0 = URI.create("META-INF/MANIFEST.MF");
        URI expected1 = URI.create("com/Main.class");
        URI expected2 = URI.create("setting/app.properties");

        assertEquals(expected0, jarURI.getEntry((URI) null));
        assertEquals(expected1, jarURI.getEntry(URI.create("/com/example/../Main.class")));
        assertEquals(expected2, jarURI.getEntry(URI.create("../setting/app.properties")));
    }

    @Test
    void testGetSymbolicPath() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        JarURI jarURI0 = new JarURI(jarFile, entryPath);
        JarURI jarURI1 = new JarURI(jarFile, entryPath + "#M.MF");
        JarURI jarURI2 = new JarURI(jarFile, entryPath + "#/M.MF");
        JarURI jarURI3 = new JarURI(jarFile, entryPath + "#../TEST/M.MF");

        String expected1 = "META-INF/M.MF";
        String expected2 = "M.MF";
        String expected3 = "TEST/M.MF";

        assertNull(jarURI0.getSymbolicPath());
        assertEquals(expected1, jarURI1.getSymbolicPath());
        assertEquals(expected2, jarURI2.getSymbolicPath());
        assertEquals(expected3, jarURI3.getSymbolicPath());
    }


    @Test
    void testToString() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        JarURI jarURI = new JarURI(jarFile, entryPath);

        String expected = "jar:" + jarFile.toURI().toString() + "!/" + entryPath;
        assertEquals(expected, jarURI.toString());
    }

    @Test
    void testToStringWithString() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        JarURI jarURI = new JarURI(jarFile, entryPath);

        String result0 = jarURI.toString((String) null);
        String result1 = jarURI.toString("/com/example/Main.class");
        String result2 = jarURI.toString("/com/example/../Main.class");
        String result3 = jarURI.toString("../setting/app.properties");
        String result4 = jarURI.toString("./TEMP/README");
        String expected0 = "jar:" + jarFile.toURI().toString() + "!/META-INF/MANIFEST.MF";
        String expected1 = "jar:" + jarFile.toURI().toString() + "!/com/example/Main.class";
        String expected2 = "jar:" + jarFile.toURI().toString() + "!/com/Main.class";
        String expected3 = "jar:" + jarFile.toURI().toString() + "!/setting/app.properties";
        String expected4 = "jar:" + jarFile.toURI().toString() + "!/META-INF/TEMP/README";

        assertEquals(expected0, result0);
        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
        assertEquals(expected3, result3);
        assertEquals(expected4, result4);
    }

    @Test
    void testToStringWithUri() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        JarURI jarURI = new JarURI(jarFile, entryPath);

        String result0 = jarURI.toString((URI) null);
        String result1 = jarURI.toString(URI.create("/com/example/Main.class"));
        String result2 = jarURI.toString(URI.create("/com/example/../Main.class"));
        String result3 = jarURI.toString(URI.create("../setting/app.properties"));
        String result4 = jarURI.toString(URI.create("./TEMP/README"));
        String expected0 = "jar:" + jarFile.toURI().toString() + "!/META-INF/MANIFEST.MF";
        String expected1 = "jar:" + jarFile.toURI().toString() + "!/com/example/Main.class";
        String expected2 = "jar:" + jarFile.toURI().toString() + "!/com/Main.class";
        String expected3 = "jar:" + jarFile.toURI().toString() + "!/setting/app.properties";
        String expected4 = "jar:" + jarFile.toURI().toString() + "!/META-INF/TEMP/README";

        assertEquals(expected0, result0);
        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
        assertEquals(expected3, result3);
        assertEquals(expected4, result4);
    }

    @Test
    void testToStringStaticMethod() {
        String jarPath = "test.jar";
        File jarFile = new File(jarPath);
        String entryPath = "META-INF/MANIFEST.MF";

        String result1 = JarURI.toString(jarPath, entryPath);
        String result2 = JarURI.toString(jarPath, URI.create(entryPath));
        String result3 = JarURI.toString(new File(jarPath), entryPath);
        String result4 = JarURI.toString(new File(jarPath), URI.create(entryPath));
        String expected = "jar:" + jarFile.toURI().toString() + "!/" + entryPath;

        assertEquals(expected, result1);
        assertEquals(expected, result2);
        assertEquals(expected, result3);
        assertEquals(expected, result4);
    }

    @Test
    void testToURI() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        JarURI jarURI = new JarURI(jarFile, entryPath);

        URI uri = jarURI.toURI();
        String expected = "jar:" + jarFile.toURI().toString() + "!/" + entryPath;
        assertEquals(expected, uri.toString());
    }

    @Test
    void testToURIWithString() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        JarURI jarURI = new JarURI(jarFile, entryPath);

        URI uri1 = jarURI.toURI((String) null);
        URI uri2 = jarURI.toURI("/com/example/../Main.class");
        URI uri3 = jarURI.toURI("../setting/app.properties");

        String expected1 = "jar:" + jarFile.toURI().toString() + "!/META-INF/MANIFEST.MF";
        String expected2 = "jar:" + jarFile.toURI().toString() + "!/com/Main.class";
        String expected3 = "jar:" + jarFile.toURI().toString() + "!/setting/app.properties";
        assertEquals(expected1, uri1.toString());
        assertEquals(expected2, uri2.toString());
        assertEquals(expected3, uri3.toString());
    }

    @Test
    void testToURIWithUri() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        JarURI jarURI = new JarURI(jarFile, entryPath);

        URI uri1 = jarURI.toURI((URI) null);
        URI uri2 = jarURI.toURI(URI.create("/com/example/../Main.class"));
        URI uri3 = jarURI.toURI(URI.create("../setting/app.properties"));

        String expected1 = "jar:" + jarFile.toURI().toString() + "!/META-INF/MANIFEST.MF";
        String expected2 = "jar:" + jarFile.toURI().toString() + "!/com/Main.class";
        String expected3 = "jar:" + jarFile.toURI().toString() + "!/setting/app.properties";
        assertEquals(expected1, uri1.toString());
        assertEquals(expected2, uri2.toString());
        assertEquals(expected3, uri3.toString());
    }

    @Test
    void testToURIStaticMethod() {
        String jarPath = "test.jar";
        File jarFile = new File(jarPath);
        String entryPath = "META-INF/MANIFEST.MF";

        URI result1 = JarURI.toURI(jarPath, entryPath);
        URI result2 = JarURI.toURI(jarPath, URI.create(entryPath));
        URI result3 = JarURI.toURI(new File(jarPath), entryPath);
        URI result4 = JarURI.toURI(new File(jarPath), URI.create(entryPath));
        URI expected = URI.create("jar:" + jarFile.toURI().toString() + "!/" + entryPath);

        assertEquals(expected, result1);
        assertEquals(expected, result2);
        assertEquals(expected, result3);
        assertEquals(expected, result4);
    }

    @Test
    void testToURL() throws MalformedURLException {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        JarURI jarURI = new JarURI(jarFile, entryPath);

        URL url = jarURI.toURL();
        String expected = "jar:" + jarFile.toURI().toString() + "!/" + entryPath;
        assertEquals(expected, url.toString());
    }

    @Test
    void testToURLWithString() throws MalformedURLException {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        JarURI jarURI = new JarURI(jarFile, entryPath);

        URL url1 = jarURI.toURL((String) null);
        URL url2 = jarURI.toURL("/com/example/../Main.class");
        URL url3 = jarURI.toURL("../setting/app.properties");

        String expected1 = "jar:" + jarFile.toURI().toString() + "!/META-INF/MANIFEST.MF";
        String expected2 = "jar:" + jarFile.toURI().toString() + "!/com/Main.class";
        String expected3 = "jar:" + jarFile.toURI().toString() + "!/setting/app.properties";
        assertEquals(expected1, url1.toString());
        assertEquals(expected2, url2.toString());
        assertEquals(expected3, url3.toString());
    }

    @Test
    void testToURLWithUri() throws MalformedURLException {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        JarURI jarURI = new JarURI(jarFile, entryPath);

        URL url1 = jarURI.toURL((URI) null);
        URL url2 = jarURI.toURL(URI.create("/com/example/../Main.class"));
        URL url3 = jarURI.toURL(URI.create("../setting/app.properties"));

        String expected1 = "jar:" + jarFile.toURI().toString() + "!/META-INF/MANIFEST.MF";
        String expected2 = "jar:" + jarFile.toURI().toString() + "!/com/Main.class";
        String expected3 = "jar:" + jarFile.toURI().toString() + "!/setting/app.properties";
        assertEquals(expected1, url1.toString());
        assertEquals(expected2, url2.toString());
        assertEquals(expected3, url3.toString());
    }

    @Test
    void testToURLStaticMethod() throws MalformedURLException {
        String jarPath = "test.jar";
        File jarFile = new File(jarPath);
        String entryPath = "META-INF/MANIFEST.MF";

        java.net.URL result1 = JarURI.toURL(jarPath, entryPath);
        java.net.URL result2 = JarURI.toURL(jarPath, URI.create(entryPath));
        java.net.URL result3 = JarURI.toURL(new File(jarPath), entryPath);
        java.net.URL result4 = JarURI.toURL(new File(jarPath), URI.create(entryPath));
        String expected = "jar:" + jarFile.toURI().toString() + "!/" + entryPath;

        assertEquals(expected, result1.toString());
        assertEquals(expected, result2.toString());
        assertEquals(expected, result3.toString());
        assertEquals(expected, result4.toString());
    }

    @Test
    void testRelativizeWithRootWithInvalidPath() {
        File jarFile = new File("test.jar");

        assertThrows(IllegalArgumentException.class, () -> {
            new JarURI(jarFile, "META-INF/../..");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new JarURI(jarFile, "META-INF/../../MANIFEST.MF");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new JarURI(jarFile, "./META-INF/../../MANIFEST.MF");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new JarURI(jarFile, "./../META-INF/../MANIFEST.MF");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new JarURI(jarFile, "./META-INF/MANIFEST.MF#../../TEST/M.MF");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new JarURI(jarFile, "./META-INF/MANIFEST.MF#");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new JarURI(jarFile, "./META-INF/MANIFEST.MF#/");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new JarURI(jarFile, "./META-INF/MANIFEST.MF#ab/");
        });

        assertDoesNotThrow(() -> {
            new JarURI(jarFile, "META-INF/../");
            new JarURI(jarFile, "META-INF/../MANIFEST.MF");
            new JarURI(jarFile, "/META-INF/../MANIFEST.MF");
            new JarURI(jarFile, "META-INF/MANIFEST.MF#../TEST/M.MF");
        });
    }

    @Test
    void testBuilder() {
        File jarFile = new File("test.jar");
        String entryPath = "META-INF/MANIFEST.MF";
        String symbolicPath = "META-INF2/M.MF";

        JarURI jarURI = JarURI.builder()
                .setJarFile(jarFile)
                .setEntry(entryPath)
                .setSymbolicAbsolutePath(symbolicPath)
                .build();

        String expected = "jar:" + jarFile.toURI().toString() + "!/" + entryPath;
        assertEquals(expected, jarURI.toString());
        assertEquals(jarFile, jarURI.getJarFile());
        assertEquals(entryPath, jarURI.getEntryPath());
        assertEquals(symbolicPath, jarURI.getSymbolicPath());
    }
}