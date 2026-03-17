package com.apkspectrum.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import lombok.NonNull;

public class JarURI {
    private static final URI ROOT = URI.create("");
    private static final boolean IS_WINDOWS = SystemUtil.isWindows() || System.getProperty("test") != null;

    private final String jarPath;
    private final String jarUri;
    private final URI entry;
    private final String rawEntry;

    public JarURI(@NonNull String jarFilePath) {
        this(new File(jarFilePath));
    }

    public JarURI(@NonNull File jarFile) {
        this(jarFile, (URI) null);
    }

    public JarURI(@NonNull String jarFilePath, String entryPath) {
        this(new File(jarFilePath), entryPath);
    }

    public JarURI(@NonNull String jarFilePath, URI entryUri) {
        this(new File(jarFilePath), entryUri);
    }

    public JarURI(@NonNull File jarFile, String entryPath) {
        this(jarFile, entryPath != null ? URI.create(entryPath) : (URI) null);
    }

    public JarURI(@NonNull File jarFile, URI entryUri) {
        this(jarFile.getPath(), "jar:" + jarFile.toURI() + "!/", entryUri);
    }

    private JarURI(String jarPath, String jarUri, URI entryUri) {
        this.jarPath = jarPath;
        this.jarUri = jarUri;
        this.entry = getEntry(entryUri);
        this.rawEntry = entryUri != null && !this.entry.equals(entryUri)
                      ? entryUri.toString() : null;
    }

    public static JarURI create(@NonNull String src) {
        if (IS_WINDOWS && src.contains("\\")) {
            String[] split = src.split("!/", 2);
            src = split[0].replaceAll("\\\\", "/");
            if (src.contains(":")) {
                src = src.replaceAll("^((jar:)?(file:)?)?([a-zA-Z]+:)", "$1/$4");
            }
            if (split.length == 2 && split[1] != null) {
                src += "!/" + split[1];
            }
        }
        return create(URI.create(src));
    }

    public static JarURI create(@NonNull URI uri) {
        String fragment = uri.getFragment();
        String scheme = null;
        while ((scheme = uri.getScheme()) != null) {
            switch (scheme) {
                case "jar":
                case "file":
                    String fileUri = uri.getSchemeSpecificPart();
                    if (fragment != null && !fragment.isEmpty()) {
                        fileUri += "#" + fragment;
                    }
                    uri = URI.create(fileUri);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid uri : " + uri);
            }
        }

        String path = uri.toString();
        if (path.contains("!/")) {
            String[] split = path.split("!/", 2);
            return new JarURI(split[0], split[1]);
        } else {
            return new JarURI(path);
        }
    }

    public JarURI resolve(String entryPath) {
        return entryPath != null ? resolve(URI.create(entryPath)) : this;
    }

    public JarURI resolve(URI entryUri) {
        return entryUri != null ? new JarURI(jarPath, jarUri, getEntry().resolve(entryUri)) : this;
    }

    public File getJarFile() {
        return new File(getJarPath());
    }

    public String getJarPath() {
        return jarPath;
    }

    public String getJarAbsolutePath() {
        return getJarFile().getAbsolutePath();
    }

    public URI getRawEntry() {
        return rawEntry != null ? URI.create(rawEntry) : null;
    }

    public String getEntryName() {
        String name = getEntryPath();
        if (name.contains("/")) {
            if (name.endsWith("/")) {
                name = name.substring(name.lastIndexOf("/", name.length() - 2) + 1,
                        name.length() - 1);
            } else {
                name = name.substring(name.lastIndexOf("/") + 1);
            }
        }
        return name;
    }

    public boolean isDirectory() {
        String name = getEntryPath();
        return name.isEmpty() || name.endsWith("/");
    }

    public boolean isFile() {
        return !isDirectory();
    }

    public boolean isSymbolic() {
        return getSymbolicPath() != null;
    }

    public String getEntryPath() {
        return getEntry().getPath();
    }

    public String getEntryPath(String entryPath) {
        return getEntry(entryPath).getPath();
    }

    public String getEntryPath(URI entryUri) {
        return getEntry(entryUri).getPath();
    }

    public URI getEntry() {
        return entry != null ? entry : ROOT;
    }

    public URI getEntry(String entryPath) {
        return getEntry(entryPath != null ? URI.create(entryPath) : (URI) null);
    }

    public URI getEntry(URI entryUri) {
        return entryUri != null ? relativizeWithRoot(getEntry().resolve(entryUri)) : getEntry();
    }

    private URI relativizeWithRoot(URI uri) {
        if (uri == null) return ROOT;
        uri = ROOT.relativize(uri);
        if (uri.getPath().contains("../") || uri.getPath().equals("..")) {
            throw new IllegalArgumentException("Invalid entry path : " + uri.toString());
        }
        String symbolic = uri.getFragment();
        if (symbolic != null) {
            if (symbolic.contains("..")) {
                symbolic = uri.resolve(symbolic).getPath();
                if (symbolic.contains("../") || symbolic.equals("..")) {
                    throw new IllegalArgumentException("Invalid symbolic path : " + uri.toString());
                }
            }
            if (symbolic.isEmpty() || symbolic.endsWith("/")) {
                throw new IllegalArgumentException("Invalid symbolic path : " + uri.toString());
            }
        }
        return uri;
    }

    public String getSymbolicPath() {
        String symbolicPath = getEntry().getFragment();
        if (symbolicPath != null) {
            symbolicPath = getEntryPath(symbolicPath);
            if (symbolicPath.isEmpty() || symbolicPath.equals("/")) {
                return null;
            }
        }
        return symbolicPath;
    }

    @Override
    public String toString() {
        return jarUri + getEntryPath();
    }

    public String toString(String entryPath) {
        return jarUri + getEntryPath(entryPath);
    }

    public String toString(URI entryUri) {
        return jarUri + getEntryPath(entryUri);
    }

    public static String toString(String jarPath, String entryPath) {
        return toString(new File(jarPath), entryPath);
    }

    public static String toString(String jarPath, URI entryUri) {
        return toString(new File(jarPath), entryUri);
    }

    public static String toString(File jarFile, String entryPath) {
        return toString(jarFile, URI.create(entryPath));
    }

    public static String toString(File jarFile, URI entryUri) {
        return new JarURI(jarFile, entryUri).toString();
    }

    public URI toURI() {
        return URI.create(toString());
    }

    public URI toURI(String entryPath) {
        return URI.create(toString(entryPath));
    }

    public URI toURI(URI entryUri) {
        return URI.create(toString(entryUri));
    }

    public static URI toURI(String jarPath, String entryPath) {
        return URI.create(toString(jarPath, entryPath));
    }

    public static URI toURI(File jarFile, String entryPath) {
        return URI.create(toString(jarFile, entryPath));
    }

    public static URI toURI(String jarPath, URI entryUri) {
        return URI.create(toString(jarPath, entryUri));
    }

    public static URI toURI(File jarFile, URI entryUri) {
        return URI.create(toString(jarFile, entryUri));
    }

    public URL toURL() throws MalformedURLException {
        return new URL(toString());
    }

    public URL toURL(String entryPath) throws MalformedURLException {
        return new URL(toString(entryPath));
    }

    public URL toURL(URI entryUri) throws MalformedURLException {
        return new URL(toString(entryUri));
    }

    public static URL toURL(String jarPath, String entryPath) throws MalformedURLException {
        return new URL(toString(jarPath, entryPath));
    }

    public static URL toURL(File jarFile, String entryPath) throws MalformedURLException {
        return new URL(toString(jarFile, entryPath));
    }

    public static URL toURL(String jarPath, URI entryUri) throws MalformedURLException {
        return new URL(toString(jarPath, entryUri));
    }

    public static URL toURL(File jarFile, URI entryUri) throws MalformedURLException {
        return new URL(toString(jarFile, entryUri));
    }

    public static JarURIBuilder builder() {
        return new JarURIBuilder();
    }
}
