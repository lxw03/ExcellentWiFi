package com.jzlg.excellentwifi.entity;

import java.util.Date;

import org.litepal.crud.DataSupport;

/**
 * �ֻ���Ϣʵ����
 * 
 * @author
 *
 */
public class Mobile extends DataSupport {
	private String moblie_mac;// �ֻ�MAC��ַ
	private String moblie_ip;// �ֻ�IP��ַ
	private Date moblie_date;// ʱ��
	private Wifi wifi;// Wifi��

	public String getMoblie_mac() {
		return moblie_mac;
	}

	public void setMoblie_mac(String moblie_mac) {
		this.moblie_mac = moblie_mac;
	}

	public String getMoblie_ip() {
		return moblie_ip;
	}

	public void setMoblie_ip(String moblie_ip) {
		this.moblie_ip = moblie_ip;
	}

	public Date getMoblie_date() {
		return moblie_date;
	}

	public void setMoblie_date(Date moblie_date) {
		this.moblie_date = moblie_date;
	}

	public Wifi getWifi() {
		return wifi;
	}

	public void setWifi(Wifi wifi) {
		this.wifi = wifi;
	}
}