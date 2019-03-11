package com.apkscanner.core.permissionmanager;

import java.util.List;

import com.apkscanner.data.apkinfo.ApkInfoHelper;
import com.apkscanner.data.apkinfo.PermissionGroupInfo;
import com.apkscanner.data.apkinfo.PermissionInfo;
import com.apkscanner.data.apkinfo.ResourceInfo;
import com.apkscanner.resource.Resource;

public class PermissionGroupInfoExt extends PermissionGroupInfo {
	public int sdk;
	public String comment;
	public String label;
	public String icon;
	public String description;
    public String request;
    public Integer priority;
	public List<PermissionInfo> permissions;
	public int protectionFlags;

	public PermissionGroupInfoExt() { }

	public PermissionGroupInfoExt(PermissionGroupInfo info) {
		super(info);
		if(info instanceof PermissionGroupInfoExt) {
			PermissionGroupInfoExt infoExt = (PermissionGroupInfoExt) info;
			comment = infoExt.comment;
			sdk = infoExt.sdk;
			label = infoExt.label;
			icon = infoExt.icon;
			description = infoExt.description;
			request = infoExt.request;
			priority = infoExt.priority;
			permissions = infoExt.permissions;
			protectionFlags = infoExt.protectionFlags;
		}
	}

	public PermissionGroupInfoExt(PermissionGroupInfoExt info) {
		this((PermissionGroupInfo)info);
	}

	@Override
	public boolean equals(Object target) {
		if(!(target instanceof PermissionGroupInfoExt)) return false;
		PermissionGroupInfoExt other = (PermissionGroupInfoExt) target;
		return super.equals(target)
				&& objEquals(label, other.label)
				&& objEquals(icon, other.icon)
				&& objEquals(description, other.description)
				&& objEquals(request, other.request)
				&& objEquals(priority, other.priority)
				&& objEquals(comment, other.comment);
	}

	public boolean isDeprecated() {
		return comment != null && comment.contains("@deprecated");
	}

	public boolean isRemoved() {
		return comment != null && comment.contains("@Removed");
	}

	public boolean isSystemApi() {
		return comment != null && comment.contains("@SystemApi");
	}

	public boolean isHide() {
		return comment != null && comment.contains("@hide");
	}

	@Override
	public String getLabel() {
    	return ApkInfoHelper.getResourceValue(getLabels(), (String)Resource.PROP_PREFERRED_LANGUAGE.getData(""));
    }

	@Override
    public String getDescription() {
    	return ApkInfoHelper.getResourceValue(getDescriptions(), (String)Resource.PROP_PREFERRED_LANGUAGE.getData(""));
    }

	public ResourceInfo[] getLabels() {
		if(labels != null) return labels;
		return labels = PermissionManager.getResource(label, sdk);
	}

	public ResourceInfo[] getDescriptions() {
		if(descriptions != null) return descriptions;
		descriptions = PermissionManager.getResource(description, sdk);
		return descriptions;
	}

	public String getIconPath() {
		return getIcons()[0].name;
	}

	public ResourceInfo[] getIcons() {
		if(icons != null) return icons;
		return icons = PermissionManager.getResource(icon, sdk);
	}

	public String getSummary() {
		StringBuilder summary = new StringBuilder();
		summary.append("[");
		summary.append(label != null ? getLabel() : name);
		summary.append("]");
		if(description != null) {
			summary.append(" : ");
			summary.append(getDescription());
		}
		if(permissions != null) {
			for(PermissionInfo info : permissions) {
				String permLabel = info.getLabel();
				if(permLabel == null) permLabel = info.name;
				summary.append("\n - ");
				summary.append(permLabel);
			}
		}
		return summary.toString();
	}

	public boolean hasDangerous() {
		return (protectionFlags & PermissionInfo.PROTECTION_DANGEROUS) != 0;
	}
}