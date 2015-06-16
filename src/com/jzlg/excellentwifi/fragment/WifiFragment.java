package com.jzlg.excellentwifi.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jzlg.excellentwifi.R;
import com.jzlg.excellentwifi.utils.HttpUtil;
import com.jzlg.excellentwifi.utils.WifiAdmin;
import com.jzlg.excellentwifi.utils.WifiEntity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * WIFIFragment
 * 
 * @author �����δ���������ΰ
 *
 */
public class WifiFragment extends Fragment implements OnClickListener,
		OnItemClickListener, OnScrollListener {
	private View view;
	private ImageButton onoffWifi;// WIFI���ذ�ť
	private WifiAdmin mWifiAdmin;// WIFI������
	private List<Map<String, Object>> listdata;// WIFI����
	private SimpleAdapter mSimpleAdapter;// ��������
	private List<ScanResult> list;// ɨ����
	private Context mContext;// ������
	private ListView listView;// �б�
	private HttpUtil httpUtil;// �������ӹ�����
	private WifiManager mWifiManager;
	private ProgressDialog dialog;
	private boolean flag=false;
	private String password = "";

	public WifiFragment(Context context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.layout_wifi, container, false);
		httpUtil = new HttpUtil();
		initView();
		initEvent();
		return view;
	}

	// ��ʼ�����
	@SuppressWarnings("static-access")
	private void initView() {
		onoffWifi = (ImageButton) view.findViewById(R.id.main_tab_wifi_onoff);
		// ��ʼ��WifiAdmin
		mWifiAdmin = new WifiAdmin(mContext);
		listView = (ListView) view.findViewById(R.id.main_tab_wifi_listview);
		mWifiManager = (WifiManager) mContext
				.getSystemService(mContext.WIFI_SERVICE);
		SMWFThread.start();
	}

	// ��ʼ���¼�
	private void initEvent() {
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
		registerForContextMenu(listView);
		onoffWifi.setOnClickListener(this);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("�������ǿ��");
		menu.setHeaderIcon(R.drawable.left_menu_pwdstrength_white);
		getActivity().getMenuInflater().inflate(R.menu.menu_wifiitem, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.wifi_menu_pwdstrength:
			dialog = new ProgressDialog(mContext);
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			dialog.setTitle("����ǿ�ȼ��");
			dialog.setMessage("���ڼ����...");
			new Thread(new RunPassword(mContext, list.get(info.position)))
					.start();
			dialog.setIcon(R.drawable.left_menu_pwdstrength_white);
			dialog.show();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	class RunPassword implements Runnable {
		private ScanResult result;
		private Context context;
		private WifiAdmin admin;
		private boolean flag = false;
		public RunPassword(Context context, ScanResult result) {
			this.context = context;
			this.result = result;
			admin = new WifiAdmin(context);
		}
		public void run() {
			String pwdDir = "99999999\n12345678\n66666666\n00000000\nabcd1234\nqwer123\n87654321\nasdfghjkl\n22222222\n88888888";
			String[] passwords = pwdDir.split("\n");
			dialog.setMax(100);
			int size = passwords.length;
			for (int i = 0;i<size; i++) {
				WifiConfiguration config = admin.tryToConnect(result.SSID,
						passwords[i], admin.getWifiMMType(result));
				WifiManager wm = admin.getWifiManager();
				int id = wm.addNetwork(config);
				wm.enableNetwork(id, true);
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				flag = admin.isConnect(context);
				
				dialog.setProgress((i+1)*10);
				if (flag) {
					password = passwords[i];
					break;
				} else {
					wm.removeNetwork(id);
				}
			}
			Message msg = new Message();
			msg.what = 2;
			Bundle bundle = new Bundle();
			if (flag) {
				dialog.dismiss();
				bundle.putString("result", password);
				msg.setData(bundle);
				WifiEntity.wifiPwd=password;
				handler.sendMessage(msg);
			} else {
				dialog.dismiss();
				bundle.putString("result", "����̫ǿ��");
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		if (mWifiAdmin.checkState() == 3) {
			saomiao();
		}
	}

	// ʵʱ����WIFI��Ϣ����ťͼƬ
	Thread SMWFThread = new Thread(new Runnable() {

		@Override
		public void run() {
			while (true) {
				int rssi = mWifiManager.getConnectionInfo().getRssi();// ��ȡ�ź�ǿ��
				Message msg = new Message();
				msg.what = 1;
				msg.obj = rssi;
				handler.sendMessage(msg);
			}
		}
	});

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				int level = (Integer) msg.obj;
				if (Math.abs(level) <= 45) {
					onoffWifi.setBackgroundResource(R.drawable.wifi01);
				} else if (Math.abs(level) <= 65) {
					onoffWifi.setBackgroundResource(R.drawable.wifi02);
				} else if (Math.abs(level) <= 80) {
					onoffWifi.setBackgroundResource(R.drawable.wifi03);
				} else if (Math.abs(level) <= 100) {
					onoffWifi.setBackgroundResource(R.drawable.wifi04);
				} else {
					onoffWifi.setBackgroundResource(R.drawable.wifi05);
				}
				break;
			case 2:
				Bundle bundle = msg.getData();
				String result = bundle.getString("result");
				Builder builder=new Builder(mContext);
				builder.setTitle("֪ͨ");
				if (result.equals("����̫ǿ��")) {
					builder.setMessage("������˼~������ʵ����̫ǿ��");
				} else {
					builder.setMessage("�ð�Ŷ~wifi�����ǣ�"+password);
				}
				builder.show();
				break;
			default:
				break;
			}
		}
	};

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

	// ��WIFI�źŽ�ǿ���ϴ���������
	private void upServer(final ScanResult sc) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				SharedPreferences sharedPreferences = mContext
						.getSharedPreferences("prot", mContext.MODE_PRIVATE);
				String protNumber = sharedPreferences.getString("protNumber",
						"10.0.2.2:8080");
				String url = "http://" + protNumber
						+ "/ExcellentWiFi/wifiAction!wifiInsert.action";
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("wifi.wifiMac", sc.BSSID);
				hashMap.put("wifi.wifiSsid", sc.SSID);
				hashMap.put("wifi.wifiPwd", "123456");
				hashMap.put("wifi.wifiAddress", "������");
				hashMap.put("wifi.wifiX", "11.22.33");
				hashMap.put("wifi.wifiY", "11.22.33");
				hashMap.put("wifi.wifiIshide", "��");
				httpUtil.sendRequestWithHttpURLConnection(url, "POST", hashMap);
			}
		}).start();
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
				int type = mWifiAdmin.getWifiMMType(sr);
				if (type==3){
					Log.i("SSID", "WPA2-PSK");
					mWifiAdmin.showLoadingPop(sr.SSID);
				}
				if (type==3) {
					Log.i("SSID", "WPA-PSK");
					mWifiAdmin.showLoadingPop(sr.SSID);
				} 
				if (type==2) {
					/* WIFICIPHER_WEP ���� */
					Log.i("SSID", "WEP");
					mWifiAdmin.addNetwork(mWifiAdmin.createWifiInfo(sr.SSID,
							"", type));
				} else {
					/* WIFICIPHER_OPEN NOPASSWORD �����޼��� */
					Log.i("SSID", "WU");
					mWifiAdmin.addNetwork(mWifiAdmin.createWifiInfo(sr.SSID,
							"", type));
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
