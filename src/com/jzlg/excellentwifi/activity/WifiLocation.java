package com.jzlg.excellentwifi.activity;

import com.jzlg.excellentwifi.R;

import android.app.ActionBar;
import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WifiLocation extends Activity {
	private ActionBar actionBar;
	private TextView wifiStrength;
	private ImageView wifiStrengthImg;
	private Refresh refresh;
	private RefreshC refreshC;
	private boolean isRefresh = true;
	private WifiManager wifi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_wifi_location);
		initView();
	}

	private void initView() {
		actionBar = getActionBar();
		actionBar.setTitle("Wifiλ��");
		actionBar.setLogo(R.drawable.left_menu_wifilocation_white);
		actionBar.setDisplayHomeAsUpEnabled(true);// ��������ͼ��
		wifi = (WifiManager) getSystemService(WIFI_SERVICE);
		wifiStrength = (TextView) findViewById(R.id.wifi_location_strength);
		wifiStrengthImg = (ImageView) findViewById(R.id.wifi_location_strengthimg);
		refresh = new Refresh();
		refreshC = new RefreshC();
	}

	@Override
	protected void onStart() {
		super.onStart();
		int rssi = wifi.getConnectionInfo().getRssi();
		if (rssi != -999) {
			isRefresh = true;
			refreshC.start();
		}
	}

	@Override
	protected void onStop() {
		isRefresh = false;
		super.onStop();
	}

	class Refresh extends Handler {
		@Override
		public void handleMessage(Message msg) {
			String ms = (String) msg.obj;
			wifiStrength.setText(ms);
		}
	}

	class RefreshC extends Thread {
		@Override
		public void run() {
			while (isRefresh) {
				int s = wifi.getConnectionInfo().getRssi();// ��ȡ�ź�ǿ��
				Message msg = refresh.obtainMessage();
				msg.obj = s + "";
				try {
					Thread.sleep(1000);
					refresh.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
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
