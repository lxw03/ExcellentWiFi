package com.jzlg.excellentwifi.activity;

import com.jzlg.excellentwifi.utils.ActivityCollector;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

/**
 * Activity����
 * 
 * @author 
 *
 */
public class BaseActivity extends FragmentActivity {
	private int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ر�����
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ��ȡ��ǰʵ��������
		Log.i("BaseActivity", getClass().getSimpleName());
		ActivityCollector.addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	@Override
	public void onBackPressed() {
		if (count == 1) {
			count--;
			ActivityCollector.finishAll();
		} else {
			count++;
			Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
		}
	}

}
