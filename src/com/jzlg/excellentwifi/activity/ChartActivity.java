package com.jzlg.excellentwifi.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.litepal.crud.DataSupport;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

import com.jzlg.excellentwifi.R;
import com.jzlg.excellentwifi.entity.WIFILine;

import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * ͼ��
 * 
 * @author 
 *
 */
public class ChartActivity extends Activity implements
		LineChartOnValueSelectListener {
	private LineChartView mChartView;

	private ValueShape shape = ValueShape.CIRCLE;// CIRCLE:Բ��;DIAMOND:����;SQUARE:������
	private boolean isCubic = true;// �Ƿ�������
	private boolean isFilled = false;// �Ƿ����
	private boolean hasLabels = true;// �Ƿ���ʾ��ֵ
	private boolean hasLabelsOnlyForSelected = false;// ������ѡ�еĵ������ʾ��ֵ
	private boolean hasLines = true;// ��
	private boolean hasPoints = true;// ��
	private LineChartData data;
	private boolean hasAxes = true;
	private boolean hasAxesNames = true;
	private ActionBar actionBar;
	private WifiManager wifi;// WIFI����
	private boolean isRefresh = true;// �Ƿ�ˢ��
	private WIFILine mWifiLine;// WIFI�ź�����ʵ����
	private WifiInfo connectionInfo;// WIFI������Ϣ
	private ArrayList<Float> listLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_chart);
		initView();
		initData();
		initEvent();
	}

	private void initEvent() {
		mChartView.setOnValueTouchListener(this);
	}

	private void initData() {
		generateValues();
		generateData();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				initData();
				break;
			default:
				break;
			}
		}
	};

	Thread thread = new Thread(new Runnable() {

		@SuppressWarnings("static-access")
		@Override
		public void run() {
			while (isRefresh) {
				doWifi();// ���������
				Message message = new Message();
				message.what = 1;
				try {
					thread.sleep(1000);
					handler.handleMessage(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	});

	private void initView() {
		actionBar = getActionBar();
		actionBar.setTitle("�ź�����");
		actionBar.setLogo(R.drawable.left_menu_levels_white);
		actionBar.setDisplayHomeAsUpEnabled(true);// ��������ͼ��
		mChartView = (LineChartView) findViewById(R.id.chart_linechart);
		wifi = (WifiManager) getSystemService(WIFI_SERVICE);
		connectionInfo = wifi.getConnectionInfo();
		listLevel = new ArrayList<Float>();
		mWifiLine = new WIFILine();
		thread.start();// �����߳�
	}

	// ��WIFI���źŽ��д���
	private void doWifi() {
//		int seconds = new Date().getSeconds();// ��
		int level = connectionInfo.getRssi();
//		int count = mWifiLine.count(WIFILine.class);
//		if (count > 60) {
//			WIFILine findFirst = mWifiLine.findFirst(WIFILine.class);
//			findFirst.delete();
//		}
		if (listLevel.size() > 60) {
			listLevel.remove(0);
		}
		listLevel.add(Float.valueOf(level+""));
//		mWifiLine.setMacAddress(connectionInfo.getMacAddress());
//		mWifiLine.setSeconds(seconds);
//		mWifiLine.setLevel(level);
//		mWifiLine.save();// ��������
	}

	// ��������
	private void generateData() {
		List<Line> lines = new ArrayList<Line>();
		for (int i = 0; i < 1; i++) {
			// ����Դ
			List<PointValue> values = new ArrayList<PointValue>();
			for (int j = 0; j < listLevel.size(); j++) {
				values.add(new PointValue(j, listLevel.get(j)));
			}
			// ������
			Line line = new Line(values);
			// �����е���ɫ
			line.setColor(ChartUtils.COLORS[i]);
			// ��״
			line.setShape(shape);
			// �Ƿ�������
			line.setCubic(isCubic);
			// ���
			line.setFilled(isFilled);
			// ��ǩ
			line.setHasLabels(hasLabels);
			// ������ѡ�е����ñ�ǩ
			line.setHasLabelsOnlyForSelected(hasLabelsOnlyForSelected);
			// ����
			line.setHasLines(hasLines);
			// ��
			line.setHasPoints(hasPoints);
			lines.add(line);
		}

		data = new LineChartData();
		data.setLines(lines);

		if (hasAxes) {
			Axis axisX = new Axis();
			Axis axisY = new Axis().setHasLines(true);
			if (hasAxesNames) {
				axisX.setName("ʱ��(s)");
				axisY.setName("�ź�ǿ��");
			}
			data.setAxisXBottom(axisX);
			data.setAxisYLeft(axisY);
		} else {
			data.setAxisXBottom(null);
			data.setAxisYLeft(null);
		}

		data.setBaseValue(Float.NEGATIVE_INFINITY);
		mChartView.setLineChartData(data);
	}

	@SuppressWarnings("static-access")
	private void generateValues() {
		List<WIFILine> findAll = mWifiLine.findAll(WIFILine.class);
//		listLevel = new ArrayList<Float>();
//		for (int j = 0; j < 1; ++j) {
//			listLevel.add(Float.valueOf(mWifiLine.findLast(WIFILine.class)+""));
//			listLevel.add(Float.valueOf(wifi.getConnectionInfo().getRssi()+""));
//		}
	}

	// �����¼�
	@Override
	public void onValueDeselected() {
	}

	@Override
	public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
		// Toast.makeText(
		// ChartActivity.this,
		// "ֵSelected: " + value + "��lineIndex:" + lineIndex
		// + "��pointIndex:" + pointIndex, Toast.LENGTH_SHORT)
		// .show();
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
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		isRefresh = true;// ��ʼˢ��
		mWifiLine.deleteAll(WIFILine.class);
		super.onStart();
	}

	@Override
	protected void onStop() {
		isRefresh = false;// ֹͣˢ��
		mWifiLine.deleteAll(WIFILine.class);
		super.onStop();
	}

}
