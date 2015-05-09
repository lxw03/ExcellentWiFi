package com.jzlg.excellentwifi.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jzlg.excellentwifi.R;
import com.jzlg.excellentwifi.utils.WifiAdmin;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * WIFIFragment
 * 
 * @author 
 *
 */
public class WifiFragment extends Fragment implements OnClickListener,
		OnItemClickListener, OnScrollListener {
	private View view;
	private ImageButton onoffWifi;
	private WifiAdmin mWifiAdmin;
	private List<Map<String, Object>> listdata;
	private SimpleAdapter mSimpleAdapter;
	private List<ScanResult> list;
	private Context mContext;
	private ListView listView;

	public WifiFragment(Context context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.layout_wifi, container, false);
		initView();
		initEvent();
		return view;
	}

	// ��ʼ���¼�
	private void initEvent() {
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
		onoffWifi.setOnClickListener(this);
	}

	// ��ʼ�����
	private void initView() {
		onoffWifi = (ImageButton) view.findViewById(R.id.main_tab_wifi_onoff);
		// ��ʼ��WifiAdmin
		mWifiAdmin = new WifiAdmin(mContext);
		listView = (ListView) view.findViewById(R.id.main_tab_wifi_listview);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	// ɨ������
	private void saomiao() {
		listdata = new ArrayList<Map<String, Object>>();
		// ������
		mSimpleAdapter = new SimpleAdapter(view.getContext(), getWfData(),
				R.layout.item_wifi, new String[] { "ssid", "levelimg" },
				new int[] { R.id.item_wifi_ssid, R.id.item_wifi_level });
		// ����������
		listView.setAdapter(mSimpleAdapter);
	}

	private List<Map<String, Object>> getDatas() {
		for (int i = 0; i < 20; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ssid", "����WIFI" + i);
			map.put("levelimg", R.drawable.ic_launcher);
			listdata.add(map);
		}
		return listdata;
	}

	// ����Դ
	private List<Map<String, Object>> getWfData() {
		if (mWifiAdmin.checkState() == 3) {
			mWifiAdmin.startScan();
			// ɨ�����б�
			list = mWifiAdmin.getWifiList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					ScanResult sc = list.get(i);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("ssid", sc.SSID);
					int level = sc.level;
					if (Math.abs(level) <= 45) {
						map.put("levelimg", R.drawable.wifi01);
					} else if (Math.abs(level) <= 65) {
						map.put("levelimg", R.drawable.wifi02);
					} else if (Math.abs(level) <= 80) {
						map.put("levelimg", R.drawable.wifi03);
					} else if (Math.abs(level) <= 100) {
						map.put("levelimg", R.drawable.wifi04);
					} else {
						map.put("levelimg", R.drawable.wifi05);
					}
					listdata.add(map);
				}
			}
		}
		return listdata;
	}

	// ����¼�
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_tab_wifi_onoff:
			if (mWifiAdmin.checkState() != 3) {
				mWifiAdmin.openWifi();
			}
			if (mWifiAdmin.checkState() == 3) {
				saomiao();
			}
			break;

		default:
			break;
		}
	}

	// ������Ŀ������¼�
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		int loac = position - list.size();

		if (loac < 0) {
			final ScanResult sr = list.get(position);
			Log.i(sr.capabilities, sr.SSID + "");

			boolean c = mWifiAdmin.getHistoryWifiConfig(sr.SSID);
			if (c) {
				// ���û���������� �������б���û�и�WIFI
				/* WIFICIPHER_WPA ���� */
				if (sr.capabilities.contains("WPA2-PSK")) {
					Log.i("SSID", "WPA2-PSK");
					mWifiAdmin.showLoadingPop(sr.SSID);
				} else if (sr.capabilities.contains("WPA-PSK")) {
					Log.i("SSID", "WPA-PSK");
					mWifiAdmin.showLoadingPop(sr.SSID);
				} else if (sr.capabilities.contains("WEP")) {
					/* WIFICIPHER_WEP ���� */
					Log.i("SSID", "WEP");
					mWifiAdmin.addNetwork(mWifiAdmin.createWifiInfo(sr.SSID,
							"", 2));
				} else {
					/* WIFICIPHER_OPEN NOPASSWORD �����޼��� */
					Log.i("SSID", "WU");
					mWifiAdmin.addNetwork(mWifiAdmin.createWifiInfo(sr.SSID,
							"", 1));
				}
			}
		}
	}

	/**
	 * firstVisibleItem:��������ֵ�ITEM visibleItemCount:���ڳ��ָ��û�����Ŀ�ж���
	 * totalItemCount���ܹ��ж�����
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		Log.i("onScroll", "firstVisibleItem��" + firstVisibleItem
				+ "visibleItemCount��" + visibleItemCount + "totalItemCount:"
				+ totalItemCount);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case SCROLL_STATE_FLING:
			break;
		case SCROLL_STATE_IDLE:
			break;
		case SCROLL_STATE_TOUCH_SCROLL:
			break;
		default:
			break;
		}
	}

}
