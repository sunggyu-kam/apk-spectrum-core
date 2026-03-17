package com.apkspectrum.util;

import java.io.File;
import java.net.URI;

public class JarURIBuilder {
    private File jarFile;
    private URI entry;
    private String symbolicPath;

    public JarURIBuilder setJarFile(String jarFilePath) {
        return setJarFile(new File(jarFilePath));
    }

    public JarURIBuilder setJarFile(File jarFile) {
        this.jarFile = jarFile;
        return this;
    }

    public JarURIBuilder setEntry(String entry) {
        return setEntry(URI.create(entry));
    }

    public JarURIBuilder setEntry(URI entry) {
        this.entry = entry;
        return this;
    }

    public JarURIBuilder setSymbolicPath(String symbolicPath) {
        this.symbolicPath = symbolicPath;
        return this;
    }

    public JarURIBuilder setSymbolicAbsolutePath(String symbolicPath) {
        if (!symbolicPath.startsWith("/")) {
            symbolicPath = "/" + symbolicPath;
        }
        return setSymbolicPath(symbolicPath);
    }

    public JarURI build() {
        if (jarFile == null) {
            throw new IllegalArgumentException("jar file is not specified");
        }
        URI entry = this.entry;
        String symbolicPath = this.symbolicPath;
        if (symbolicPath != null) {
            if (entry == null) {
                throw new IllegalArgumentException("entry is not specified");
            }
            if (symbolicPath.lastIndexOf("#") > 0) {
                throw new IllegalArgumentException("invalid symbolic path");
            }
            if (!symbolicPath.startsWith("#")) {
                symbolicPath = "#" + symbolicPath;
            }
            entry = entry.resolve(symbolicPath);
        }
        return new JarURI(jarFile, entry);
    }
}
