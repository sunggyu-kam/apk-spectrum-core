package com.apkspectrum.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.apkspectrum.util.FileUtil.FSStyle;

public class FileUtilTest {

    @TempDir
    Path tempDir;

    @Test
    public void testReadData() {
        // Given
        String testContent = "Hello, World!";
        File testFile = new File(tempDir.toFile(), "test.txt");

        try {
            FileWriter writer = new FileWriter(testFile);
            writer.write(testContent);
            writer.close();
        } catch (IOException e) {
            fail("Failed to create test file");
        }

        // When
        byte[] result = FileUtil.readData(testFile.getAbsolutePath());

        // Then
        assertNotNull(result);
        assertEquals(testContent, new String(result));
    }

    @Test
    public void testCopy() {
        // Given
        String testContent = "Hello, World!";
        File sourceFile = new File(tempDir.toFile(), "source.txt");
        File destFile = new File(tempDir.toFile(), "dest.txt");

        try {
            FileWriter writer = new FileWriter(sourceFile);
            writer.write(testContent);
            writer.close();
        } catch (IOException e) {
            fail("Failed to create source file");
        }

        // When
        FileUtil.copy(sourceFile.getAbsolutePath(), destFile.getAbsolutePath());

        // Then
        assertTrue(destFile.exists());
        assertEquals(testContent, new String(FileUtil.readData(destFile.getAbsolutePath())));
    }

    @Test
    public void testFindFiles() {
        // Given
        File subDir = new File(tempDir.toFile(), "subdir");
        subDir.mkdir();

        File txtFile1 = new File(tempDir.toFile(), "file1.txt");
        File txtFile2 = new File(subDir, "file2.txt");
        File javaFile = new File(tempDir.toFile(), "file.java");

        try {
            txtFile1.createNewFile();
            txtFile2.createNewFile();
            javaFile.createNewFile();
        } catch (IOException e) {
            fail("Failed to create test files");
        }

        // When
        List<String> result = FileUtil.findFiles(tempDir.toFile(), ".txt", null);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains(txtFile1.getAbsolutePath()));
        assertTrue(result.contains(txtFile2.getAbsolutePath()));
    }

    @Test
    public void testGetTempPath() {
        // When
        String result = FileUtil.getTempPath();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("ApkScanner"));
    }

    @Test
    public void testMakeFolder() {
        // Given
        String newFolderPath = tempDir.toFile().getAbsolutePath() + File.separator + "newFolder";

        // When
        Boolean result = FileUtil.makeFolder(newFolderPath);

        // Then
        assertTrue(result);
        assertTrue(new File(newFolderPath).exists());
    }

    @Test
    public void testDeleteDirectory() {
        // Given
        File testDir = new File(tempDir.toFile(), "testDir");
        testDir.mkdir();
        File testFile = new File(testDir, "test.txt");

        try {
            testFile.createNewFile();
        } catch (IOException e) {
            fail("Failed to create test file");
        }

        // When
        boolean result = FileUtil.deleteDirectory(testDir);

        // Then
        assertTrue(result);
        assertFalse(testDir.exists());
    }

    @Test
    public void testGetFileSize() {
        // Given
        String testContent = "Hello, World!";
        File testFile = new File(tempDir.toFile(), "test.txt");

        try {
            FileWriter writer = new FileWriter(testFile);
            writer.write(testContent);
            writer.close();
        } catch (IOException e) {
            fail("Failed to create test file");
        }

        // When
        String result = FileUtil.getFileSize(testFile, FSStyle.SHORT);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetSuffix() {
        // Given
        String filePath1 = "";
        String filePath2 = "txt";
        String filePath3 = ".txt";
        String filePath4 = "./.txt";
        String filePath5 = "file.txt";
        String filePath6 = "/path/.to/filetxt";
        String filePath7 = "/path/to/file.test.txt";

        // When
        String result1 = FileUtil.getSuffix(filePath1);
        String result2 = FileUtil.getSuffix(filePath2);
        String result3 = FileUtil.getSuffix(filePath3);
        String result4 = FileUtil.getSuffix(filePath4);
        String result5 = FileUtil.getSuffix(filePath5);
        String result6 = FileUtil.getSuffix(filePath6);
        String result7 = FileUtil.getSuffix(filePath7);

        // Then
        assertEquals("", result1);
        assertEquals("", result2);
        assertEquals(".txt", result3);
        assertEquals(".txt", result4);
        assertEquals(".txt", result5);
        assertEquals("", result6);
        assertEquals(".txt", result7);
    }

    @Test
    public void testGetMessageDigest() {
        // Given

        File testFile0 = new File(tempDir.toFile(), "empty.txt");
        try {
            testFile0.createNewFile();
        } catch (IOException e) {
            fail("Failed to create test file");
        }

        String testContent = "Hello, World!";
        File testFile1 = new File(tempDir.toFile(), "test.txt");
        try (FileWriter writer = new FileWriter(testFile1)) {
            writer.write(testContent);
        } catch (IOException e) {
            fail("Failed to create test file");
        }

        File testFile2 = new File(tempDir.toFile(), "not.exist.txt");

        // When
        String result0 = FileUtil.getMessageDigest(testFile0, "MD5");
        String result1 = FileUtil.getMessageDigest(testFile1, "MD5");

        // Then
        assertEquals("D4:1D:8C:D9:8F:00:B2:04:E9:80:09:98:EC:F8:42:7E", result0);
        assertEquals("65:A8:E2:7D:88:79:28:38:31:B6:64:BD:8B:7F:0A:D4", result1);

        // Unsupported algorithm name case
        assertThrows(IllegalArgumentException.class, () -> {
            FileUtil.getMessageDigest(testFile0, "NoSuchAlgorithm");
        });

        // Not existed file
        assertThrows(IllegalArgumentException.class, () -> {
            FileUtil.getMessageDigest(testFile2, "MD5");
        });
    }

    @Test
    public void testGetMessageDigestByLargeFile() {
        // Given

        File testFile0 = new File(tempDir.toFile(), "empty.txt");
        try {
            testFile0.createNewFile();
        } catch (IOException e) {
            fail("Failed to create test file");
        }

        String testContent = "Hello, World!";
        File testFile1 = new File(tempDir.toFile(), "test.txt");
        try (FileWriter writer = new FileWriter(testFile1)) {
            writer.write(testContent);
        } catch (IOException e) {
            fail("Failed to create test file");
        }

        File testFile2 = new File(tempDir.toFile(), "not.exist.txt");

        // When
        String result0 = FileUtil.getMessageDigestByLargeFile(testFile0, "MD5");
        String result1 = FileUtil.getMessageDigestByLargeFile(testFile1, "MD5");

        // Then
        assertEquals("D4:1D:8C:D9:8F:00:B2:04:E9:80:09:98:EC:F8:42:7E", result0);
        assertEquals("65:A8:E2:7D:88:79:28:38:31:B6:64:BD:8B:7F:0A:D4", result1);

        // Unsupported algorithm name case
        assertThrows(IllegalArgumentException.class, () -> {
            FileUtil.getMessageDigestByLargeFile(testFile0, "NoSuchAlgorithm");
        });

        // Not existed file
        assertThrows(IllegalArgumentException.class, () -> {
            FileUtil.getMessageDigestByLargeFile(testFile2, "MD5");
        });
    }
}
