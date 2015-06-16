package com.jzlg.excellentwifi.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jzlg.excellentwifi.R;
import com.jzlg.excellentwifi.entity.Mobile;
import com.jzlg.excellentwifi.utils.HttpUtil;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * WIFI��������ͼ
 * 
 * @author �δ���
 *
 */
public class GplotActivity extends Activity {
	private ActionBar actionBar;
	private HttpUtil mHttpUtil;
	private SharedPreferences sharedPreferences;
	private String protNumber;// �˿ں�
	private ListView mListView;
	private List<Map<String, Object>> maps;
	private SimpleAdapter adapter;
	private WifiManager wifi;
	private List<Mobile> mobiles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_wifi_gplot);
		initView();
	}

	// ��ʼ���ؼ�
	private void initView() {
		actionBar = getActionBar();
		actionBar.setTitle("WIFI����");
		actionBar.setLogo(R.drawable.left_menu_gplot_white_1);
		actionBar.setDisplayHomeAsUpEnabled(true);// ��������ͼ��
		wifi = (WifiManager) getSystemService(WIFI_SERVICE);
		mHttpUtil = new HttpUtil();
		mobiles = new ArrayList<Mobile>();
		sharedPreferences = getSharedPreferences("prot", MODE_PRIVATE);
		protNumber = sharedPreferences.getString("protNumber", "10.0.2.2:8080");
		mListView = (ListView) findViewById(R.id.wifi_gplot_lv);
		initListView();
	}

	// ����Դ
	private List<Map<String, Object>> getData() {
		for (int i = 0; i < mobiles.size(); i++) {
			Mobile mobile = mobiles.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("number", i + "");
			map.put("name", mobile.getMoblie_ip());
			maps.add(map);
		}
		return maps;
	}

	// �ӷ�������ȡ����
	Thread thread = new Thread(new Runnable() {

		@Override
		public void run() {
			String url = "http://" + protNumber
					+ "/ExcellentWiFi/wifiAction!wifiByProperty.action";
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("wifi.wifiMac", wifi.getConnectionInfo().getBSSID());
			String data = mHttpUtil.getHttpData(url, "POST", hashMap);
			try {
				JSONArray jsonArray = new JSONArray(data);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					JSONArray jsonArray2 = jsonObject.getJSONArray("Mobiles");
					for (int j = 0; j < jsonArray2.length(); j++) {
						Mobile mobile = new Mobile();
						JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
						mobile.setMoblie_ip(jsonObject2.getString("mobileIp"));
						mobile.setMoblie_mac(jsonObject2.getString("mobileMac"));
						mobile.setMoblie_date(jsonObject2
								.getString("mobileTime"));
						mobiles.add(mobile);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	});

	// ��ʼ��ListView
	private void initListView() {
		// ����Դ
		maps = new ArrayList<Map<String, Object>>();
		// ������
		adapter = new SimpleAdapter(this, getData(), R.layout.gplot_item,
				new String[] { "number", "name" }, new int[] {
						R.id.wifi_gplot_number_tv, R.id.wifi_gplot_name_tv });
		// ����������
		mListView.setAdapter(adapter);
	}

	// �˵�ѡ���¼�
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return true;
	}
}
