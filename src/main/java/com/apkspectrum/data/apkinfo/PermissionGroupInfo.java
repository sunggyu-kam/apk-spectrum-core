package com.apkspectrum.data.apkinfo;

import java.util.Arrays;
import java.util.Objects;

import com.apkspectrum.resource._RStr;

public class PermissionGroupInfo {
    public ResourceInfo[] descriptions = null; // "string resource"
    public ResourceInfo[] icons = null; // "drawable resource"
    public ResourceInfo[] labels = null; // "string resource"
    public String name = null; // "string"
    public ResourceInfo[] requests = null; // "string resource"

    public PermissionGroupInfo() {}

    public PermissionGroupInfo(PermissionGroupInfo info) {
        descriptions = info.descriptions;
        icons = info.icons;
        labels = info.labels;
        name = info.name;
        requests = info.requests;
    }

    @Override
    public boolean equals(Object target) {
        if (!(target instanceof PermissionGroupInfo)) return false;
        PermissionGroupInfo other = (PermissionGroupInfo) target;
        return Objects.equals(name, other.name) && Arrays.deepEquals(labels, other.labels)
                && Arrays.deepEquals(descriptions, other.descriptions)
                && Arrays.deepEquals(requests, other.requests)
                && Arrays.deepEquals(icons, other.icons);
    }

    public String getLabel() {
        return ApkInfoHelper.getResourceValue(labels, _RStr.getLanguage());
    }

    public String getDescription() {
        return ApkInfoHelper.getResourceValue(descriptions, _RStr.getLanguage());
    }

    public String getRequest() {
        return ApkInfoHelper.getResourceValue(requests, _RStr.getLanguage());
    }
}
