package com.jzlg.excellentwifi.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * WIFI��γ�ȸ�������
 * 
 * @author 
 *
 */
public class WifiInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 420265804065545702L;
	private String name;
	private double latitude;
	private double longitude;

	public static List<WifiInfo> infos = new ArrayList<WifiInfo>();
	static {
		infos.add(new WifiInfo("����WIFI1", 30.333607, 112.248643));
		infos.add(new WifiInfo("����WIFI2", 30.343607, 112.258643));
		infos.add(new WifiInfo("����WIFI3", 30.353607, 112.268643));
		infos.add(new WifiInfo("����WIFI4", 30.363607, 112.278643));
	}

	public WifiInfo(String name, double latitude, double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}