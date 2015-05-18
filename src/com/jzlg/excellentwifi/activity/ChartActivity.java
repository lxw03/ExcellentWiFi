package com.jzlg.excellentwifi.activity;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

import com.jzlg.excellentwifi.R;

import android.app.ActionBar;
import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;

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
	private boolean hasLines = true;// ��ʾ��
	private boolean hasPoints = true;// ��ʾ��
	private LineChartData data;
	private boolean hasAxes = true;
	private boolean hasAxesNames = true;
	private ActionBar actionBar;
	private WifiManager wifi;// WIFI����
	private boolean isRefresh = true;// �Ƿ�ˢ��
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
				Message message = new Message();
				message.what = 1;
				int level = wifi.getConnectionInfo().getRssi();
				if (listLevel.size() > 60) {
					listLevel.remove(0);
				}
				listLevel.add(Float.valueOf(level + ""));
				try {
					thread.sleep(1000);
					handler.sendMessage(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	});

	// ��ʼ�����
	private void initView() {
		actionBar = getActionBar();
		actionBar.setTitle("�ź�����");
		actionBar.setLogo(R.drawable.left_menu_levels_white);
		actionBar.setDisplayHomeAsUpEnabled(true);// ��������ͼ��
		mChartView = (LineChartView) findViewById(R.id.chart_linechart);
		wifi = (WifiManager) getSystemService(WIFI_SERVICE);
		listLevel = new ArrayList<Float>();
		thread.start();// �����߳�
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

	// �����¼�
	@Override
	public void onValueDeselected() {
	}

	@Override
	public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
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
		super.onStart();
	}

	@Override
	protected void onStop() {
		isRefresh = false;// ֹͣˢ��
		super.onStop();
	}

}
