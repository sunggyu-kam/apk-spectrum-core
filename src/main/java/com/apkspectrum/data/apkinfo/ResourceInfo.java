package com.apkspectrum.data.apkinfo;

import java.util.Objects;

public class ResourceInfo implements Cloneable {
    public final String name;
    public final String configuration;

    public ResourceInfo() {
        this(null, null);
    }

    public ResourceInfo(String name) {
        this(name, null);
    }

    public ResourceInfo(String name, String configuration) {
        this.name = name;
        this.configuration = configuration;
    }

    public boolean hasName() {
        return name != null && !name.isEmpty();
    }

    public boolean hasConfiguration() {
        return configuration != null && !configuration.isEmpty()
                && !configuration.equals("default");
    }

    @Override
    public boolean equals(Object target) {
        if (!(target instanceof ResourceInfo)) return false;
        ResourceInfo other = (ResourceInfo) target;
        return Objects.equals(name, other.name)
                && Objects.equals(configuration, other.configuration);
    }

    @Override
    public ResourceInfo clone() {
        try {
            return (ResourceInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            // Won't happen because we implement Cloneable
            throw new Error(e.toString());
        }
    }

    @Override
    public String toString() {
        return "ResourceInfo[name=\"" + name + "\", configuration=\"" + configuration + "\"]";
    }
}
