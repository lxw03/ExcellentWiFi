package com.jzlg.excellentwifi.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jzlg.excellentwifi.R;
import com.jzlg.excellentwifi.activity.RefreshListView;
import com.jzlg.excellentwifi.activity.RefreshListView.IRefreshListener;
import com.jzlg.excellentwifi.utils.WifiAdmin;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class WifiFragment extends Fragment implements OnClickListener,
		OnItemClickListener, OnScrollListener, IRefreshListener {
	private WifiConfiguration mConfig;
	private View view;
	private ImageButton onoffWifi;
	private WifiAdmin mWifiAdmin;
	private RefreshListView listview;
	private List<Map<String, Object>> listdata;
	private SimpleAdapter mSimpleAdapter;
	private List<ScanResult> list;
	private ScanResult mScanResult;
	private boolean isRefresh = false;// �Ƿ�ˢ��
	private Context mContext;
	private LinearLayout loginLayout;

	public WifiFragment(Context context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.tab_wifi, container, false);
		initView();
		initEvent();
		return view;
	}

	// ��ʼ���¼�
	private void initEvent() {
		listview.setOnItemClickListener(this);
		listview.setOnScrollListener(this);
		onoffWifi.setOnClickListener(this);
	}

	// ��ʼ�����
	private void initView() {
		onoffWifi = (ImageButton) view.findViewById(R.id.main_tab_wifi_onoff);
		// ��ʼ��WifiAdmin
		mWifiAdmin = new WifiAdmin(mContext);
		listview = (RefreshListView) view
				.findViewById(R.id.main_tab_wifi_listview);
		listview.setInterface(this);
	}

	// ɨ������
	private void saomiao() {
		listdata = new ArrayList<Map<String, Object>>();
		// ������
		mSimpleAdapter = new SimpleAdapter(view.getContext(), getData(),
				R.layout.item_wifi, new String[] { "ssid", "levelimg" },
				new int[] { R.id.item_wifi_ssid, R.id.item_wifi_level });
		// ����������
		listview.setAdapter(mSimpleAdapter);
	}

	// ����Դ
	private List<Map<String, Object>> getData() {
		// ��ʼɨ������
		mWifiAdmin.startScan();

		// ɨ�����б�
		list = mWifiAdmin.getWifiList();

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				mScanResult = list.get(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ssid", mScanResult.SSID);
				int level = mScanResult.level;
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
		return listdata;
	}

	// ����¼�
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_tab_wifi_onoff:
			mWifiAdmin.openWifi();
			saomiao();
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
			Log.i("ListView", "�û���������");
			break;
		case SCROLL_STATE_IDLE:
			Log.i("ListView", "��ͼֹͣ����");
			break;
		case SCROLL_STATE_TOUCH_SCROLL:
			Log.i("ListView", "�û����ڻ���");
			break;
		default:
			break;
		}
	}

	/**
	 * ˢ������
	 */
	public void onRefresh() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// ��ȡ���µ�����
				saomiao();
				// ֪ͨlistviewˢ���������
				listview.refreshComplete();
			}
		}, 3000);

	}

}
