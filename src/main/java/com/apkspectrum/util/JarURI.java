package com.apkspectrum.util;

import java.io.File;
import java.net.URI;

import lombok.NonNull;

public class JarURI {
    private final String jarUri;
    private final URI entry;

    public JarURI(@NonNull String jarFilePath) {
        this(new File(jarFilePath), URI.create(""));
    }

    public JarURI(@NonNull File jarFile) {
        this(jarFile, URI.create(""));
    }

    public JarURI(@NonNull File jarFile, @NonNull String entryPath) {
        this(jarFile, URI.create(entryPath));
    }

    public JarURI(@NonNull File file, URI entryUri) {
        this(file.toURI().toString(), URI.create("/").resolve(entryUri));
    }

    private JarURI(String jarUri, URI entryUri) {
        this.jarUri = jarUri;
        this.entry = checkEntryURI(entryUri);
    }

    public URI toURI() {
        return URI.create("jar:" + jarUri + "!" + entry);
    }

    public URI toURI(String entryPath) {
        return toURI(URI.create(entryPath));
    }

    public URI toURI(URI entryUri) {
        return URI.create("jar:" + jarUri + "!" + checkEntryURI(entry.resolve(entryUri)));
    }

    private URI checkEntryURI(URI uri) {
        String path = uri.toString();
        if (!path.startsWith("/") || path.contains("/..")) {
            throw new IllegalArgumentException("Invalid entry path : " + path);
        }
        return uri;
    }

    public static JarURI create(String src) {
        return create(URI.create(src));
    }

    public static JarURI create(@NonNull URI uri) {
        if ("jar".equals(uri.getScheme())) {
            uri = URI.create(uri.getSchemeSpecificPart());
        }
        String path = uri.getPath();
        if (path.contains("!/")) {
            String[] split = path.split("!/", 2);
            return new JarURI(new File(split[0]), split[1]);
        } else {
            return new JarURI(path);
        }
    }

    public JarURI resolve(String entryPath) {
        return resolve(URI.create(entryPath));
    }

    public JarURI resolve(URI entryUri) {
        return new JarURI(jarUri, entry.resolve(entryUri));
    }

    public File getJarFile() {
        return new File(URI.create(jarUri).getPath());
    }

    public String getJarFilePath() {
        return new File(URI.create(jarUri).getPath()).toString();
    }

    public String getEntryPath() {
        return getEntry().toString().substring(1);
    }

    public URI getEntry() {
        return entry;
    }

    public static String toURI(String jarPath, String entryPath) {
        return toURI(new File(jarPath), entryPath);
    }

    public static String toURI(File jarFile, String entryPath) {
        return "jar:" + jarFile.toURI() + "!/" + entryPath;
    }

    @Override
    public String toString() {
        return toURI().toString();
    }

    public String toString(String entryPath) {
        return toURI(entryPath).toString();
    }

    public String toString(URI entryUri) {
        return toURI(entryUri).toString();
    }
}
