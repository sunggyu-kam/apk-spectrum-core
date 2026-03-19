package com.apkspectrum.tool.aapt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

import com.apkspectrum.logback.Log;
import com.apkspectrum.resource._RFile;

public class AaptNativeWrapper {
    private static final int SEM_COUNT = 10;
    private static Semaphore semaphore = new Semaphore(SEM_COUNT, true);
    private static boolean nativeLocked = false;

    public static class List {
        public static String[] getList(String apkFilePath, boolean androidData, boolean verbose) {
            ArrayList<String> cmd = new ArrayList<>();
            cmd.add("list");
            if (verbose) cmd.add("-v");
            if (androidData) cmd.add("-a");
            cmd.add(apkFilePath);
            return run(cmd.toArray(new String[0]));
        }
    }

    public static class Dump {
        public static String[] getStrings(String apkFilePath) {
            return run_l(new String[] {"dump", "strings", apkFilePath});
        }

        public static String[] getBadging(String apkFilePath, boolean includeMetaData) {
            if (includeMetaData) {
                return run_l(new String[] {"dump", "--include-meta-data", "badging", apkFilePath});
            } else {
                return run_l(new String[] {"dump", "badging", apkFilePath});
            }
        }

        public static String[] getPermissions(String apkFilePath) {
            return run_l(new String[] {"dump", "permissions", apkFilePath});
        }

        public static String[] getResources(String apkFilePath, boolean includeResourceValues) {
            Log.i("getResources() " + apkFilePath);
            if (includeResourceValues) {
                return run_l(new String[] {"dump", "--values", "resources", apkFilePath});
            } else {
                return run_l(new String[] {"dump", "resources", apkFilePath});
            }
        }

        public static String[] getConfigurations(String apkFilePath) {
            return run_l(new String[] {"dump", "configurations", apkFilePath});
        }

        public static String[] getXmltree(String apkFilePath, String[] assets) {
            // Log.i("getXmltree() " + apkFilePath);
            ArrayList<String> cmd = new ArrayList<>();
            cmd.add("dump");
            cmd.add("xmltree");
            cmd.add(apkFilePath);
            for (String a : assets) {
                cmd.add(a);
            }
            return run_l(cmd.toArray(new String[0]));
        }

        public static String[] getXmlstrings(String apkFilePath, String[] assets) {
            ArrayList<String> cmd = new ArrayList<>();
            cmd.add("dump");
            cmd.add("xmlstrings");
            cmd.add(apkFilePath);
            for (String a : assets) {
                cmd.add(a);
            }
            return run_l(cmd.toArray(new String[0]));
        }
    }

    public static String getVersion() {
        String[] version = run_l(new String[] {"version"});
        if (version == null || version.length == 0) {
            Log.e("Failed to get version.");
        } else {
            for (String s : version) {
                if (s.startsWith("Android Asset Packaging Tool, v")) {
                    return s.substring(30);
                }
            }
            Log.e("Failed to get version. {}", Arrays.asList(version));
        }
        return null;
    }

    private static String[] run_l(String[] params) {
        for (String s : params) {
            if (s == null) {
                throw new NullPointerException("params has null");
            }
        }

        semaphore.acquireUninterruptibly();
        String[] ret = run(params);
        semaphore.release();
        return ret;
    }

    public static void lock() {
        synchronized (semaphore) {
            if (nativeLocked) return;
            semaphore.acquireUninterruptibly(SEM_COUNT);
            nativeLocked = true;
        }
    }

    public static void unlock() {
        synchronized (semaphore) {
            if (nativeLocked) semaphore.release(SEM_COUNT);
            nativeLocked = false;
        }
    }

    private native static String[] run(String[] params);

    public static void main(String[] args) {
        Log.i("AAPT native library version, {}", getVersion());
    }

    static {
        if ("64".equals(System.getProperty("sun.arch.data.model"))) {
            System.load(_RFile.BIN_AAPT_LIB64.getPath());
        } else {
            System.load(_RFile.BIN_AAPT_LIB32.getPath());
        }
    }
}
