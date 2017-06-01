package com.apkscanner.gui;

import java.awt.event.KeyEvent;

import javax.swing.JTabbedPane;

import com.apkscanner.data.apkinfo.ApkInfo;
import com.apkscanner.data.apkinfo.ApkInfoHelper;
import com.apkscanner.gui.tabpanels.BasicInfo;
import com.apkscanner.gui.tabpanels.Components;
import com.apkscanner.gui.tabpanels.Libraries;
import com.apkscanner.gui.tabpanels.Resources;
import com.apkscanner.gui.tabpanels.Signatures;
import com.apkscanner.gui.tabpanels.Widgets;
import com.apkscanner.gui.theme.TabbedPaneUIManager;
import com.apkscanner.resource.Resource;

public class TabbedPanel extends JTabbedPane
{
	private static final long serialVersionUID = -5500517956616692675L;

	public static final int CMD_EXTRA_DATA = 1000;

	private String[] labels;

	public abstract interface TabDataObject
	{
		public void initialize();
		public void setData(ApkInfo apkInfo);
		public void setExtraData(ApkInfo apkInfo);
		public void reloadResource();
	}

	public TabbedPanel()
	{
		this(null);
	}

	public TabbedPanel(String themeClazz)
	{
		setOpaque(true);
		TabbedPaneUIManager.setUI(this, themeClazz);

		loadResource();

		addTab(labels[0], null, new BasicInfo(), labels[0] + " (Alt+1)");
		setMnemonicAt(0, KeyEvent.VK_1);

		addTab(labels[1], null, new Widgets(), labels[1] + " (Alt+2)");
		setMnemonicAt(1, KeyEvent.VK_2);

		addTab(labels[2], null, new Libraries(), labels[2] + " (Alt+3)");
		setMnemonicAt(2, KeyEvent.VK_3);

		addTab(labels[3], null, new Resources(), labels[3] + " (Alt+4)");
		setMnemonicAt(3, KeyEvent.VK_4);

		addTab(labels[4], null, new Components(), labels[4] + " (Alt+5)");
		setMnemonicAt(4, KeyEvent.VK_5);

		addTab(labels[5], null, new Signatures(), labels[5] + " (Alt+6)");
		setMnemonicAt(5, KeyEvent.VK_6);

		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		for(int i = 1; i < 6; i++) {
			setEnabledAt(i, false);
		}
	}

	private void loadResource()
	{
		labels = new String[] {
				Resource.STR_TAB_BASIC_INFO.getString(),
				Resource.STR_TAB_WIDGET.getString(),
				Resource.STR_TAB_LIB.getString(),
				Resource.STR_TAB_IMAGE.getString(),
				Resource.STR_TAB_ACTIVITY.getString(),
				Resource.STR_TAB_CERT.getString()
		};
	}

	public void reloadResource()
	{
		loadResource();

		for(int i = 0; i < 6; i++) {
			setTitleAt(i, getTitleAt(i).replaceAll("^([^\\(]*)", labels[i]));
			setToolTipTextAt(i, labels[i] + " (Alt+"+ (i+1) +")");
			((TabDataObject)(getComponent(i))).reloadResource();
		}
	}

	public void setProgress(String message)
	{
		((BasicInfo)(getComponent(0))).setProgress(message);
	}

	public void setLodingLabel()
	{
		((BasicInfo)(getComponent(0))).setProgress(null);
		setSelectedIndex(0);
		for(int i = 1; i < 6; i++) {
			setTitleAt(i, labels[i] + "(...)");
			setEnabledAt(i, false);
		}
	}

	public void setData(ApkInfo apkInfo)
	{
		setData(apkInfo, -1);
	}

	public void setData(ApkInfo apkInfo, int id)
	{
		if(apkInfo != null) {
			if(id == -1 || id == 0) {
				((TabDataObject)(getComponent(0))).setData(apkInfo);
				setSelectedIndex(0);
			}
			if(id == -1 || id == 1) setPanelData(1, apkInfo.widgets.length, apkInfo);
			if(id == -1 || id == 2) setPanelData(2, apkInfo.libraries.length, apkInfo);
			if(id == -1 || id == 4) setPanelData(4, ApkInfoHelper.getComponentCount(apkInfo), apkInfo);
			if(id == -1 || id == 5) setPanelData(5, ApkInfoHelper.isSigned(apkInfo) ? apkInfo.certificates.length : 0, apkInfo);
			if(id == -1 || id == 3) setPanelData(3, apkInfo.resources.length, apkInfo);
			if(id >= CMD_EXTRA_DATA) {
				((TabDataObject)(getComponent(id - CMD_EXTRA_DATA))).setExtraData(apkInfo);
			}
		} else {
			((TabDataObject)(getComponent(0))).setData(null);
			setSelectedIndex(0);
			for(int i = 1; i < 6; i++) {
				if(id == -1 || id == i) setPanelData(i, 0, null);
			}
		}
	}

	private void setPanelData(int panelIdx, int dataSize, ApkInfo apkInfo)
	{
		if(dataSize > 0) {
			((TabDataObject)(getComponent(panelIdx))).setData(apkInfo);
			setTitleAt(panelIdx, labels[panelIdx] + "(" + dataSize + ")");
			setEnabledAt(panelIdx, true);
		} else {
			setTitleAt(panelIdx, labels[panelIdx] + "(0)");
			setEnabledAt(panelIdx, false);
		}
	}
}