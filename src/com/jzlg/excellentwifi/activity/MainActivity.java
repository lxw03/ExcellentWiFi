package com.jzlg.excellentwifi.activity;

import com.jzlg.excellentwifi.R;
import com.jzlg.excellentwifi.fragment.LeidaFragment;
import com.jzlg.excellentwifi.fragment.MapFragment;
import com.jzlg.excellentwifi.fragment.MoreFragment;
import com.jzlg.excellentwifi.fragment.WifiFragment;
import com.jzlg.excellentwifi.menu.SlidingMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements OnClickListener {
	// �໬�˵�
	private SlidingMenu mLeftMenu;

	// �ĸ��������
	private LinearLayout mTabWifi;
	private LinearLayout mTabMap;
	private LinearLayout mTabLeida;
	private LinearLayout mTabMore;

	// �ĸ�ͼƬ��ť
	private ImageButton mImgWifi;
	private ImageButton mImgMap;
	private ImageButton mImgLeida;
	private ImageButton mImgMore;

	// �ĸ�Fragment
	private Fragment mFMWifi;
	private Fragment mFMMap;
	private Fragment mFMLeida;
	private Fragment mFMMore;

	// ����໬�˵�ѡ��
	private RelativeLayout mLayoutLevels;
	private RelativeLayout mLayoutWifilocation;
	private RelativeLayout mLayoutPwd;
	private RelativeLayout mLayoutGplot;
	private RelativeLayout mLayoutSetting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_main);
		initView();
		initEvent();
		setSelect(0);
	}

	// ��ʼ���¼�
	private void initEvent() {
		mTabWifi.setOnClickListener(this);
		mTabMap.setOnClickListener(this);
		mTabLeida.setOnClickListener(this);
		mTabMore.setOnClickListener(this);
		mLayoutLevels.setOnClickListener(this);
		mLayoutWifilocation.setOnClickListener(this);
		mLayoutPwd.setOnClickListener(this);
		mLayoutGplot.setOnClickListener(this);
		mLayoutSetting.setOnClickListener(this);
	}

	// ��ʼ������
	private void initView() {
		// ����
		mTabWifi = (LinearLayout) findViewById(R.id.main_bottom_wifi);
		mTabMap = (LinearLayout) findViewById(R.id.main_bottom_map);
		mTabLeida = (LinearLayout) findViewById(R.id.main_bottom_leida);
		mTabMore = (LinearLayout) findViewById(R.id.main_bottom_more);
		// ͼƬ��ť
		mImgWifi = (ImageButton) findViewById(R.id.main_bottom_wifi_imgbtn);
		mImgMap = (ImageButton) findViewById(R.id.main_bottom_map_imgbtn);
		mImgLeida = (ImageButton) findViewById(R.id.main_bottom_leida_imgbtn);
		mImgMore = (ImageButton) findViewById(R.id.main_bottom_more_imgbtn);
		// ��߲˵�ѡ��
		mLayoutLevels = (RelativeLayout) findViewById(R.id.left_menu_levels);
		mLayoutWifilocation = (RelativeLayout) findViewById(R.id.left_menu_wifilocation);
		mLayoutPwd = (RelativeLayout) findViewById(R.id.left_menu_pwdStrength);
		mLayoutGplot = (RelativeLayout) findViewById(R.id.left_menu_gplot);
		mLayoutSetting = (RelativeLayout) findViewById(R.id.left_menu_setting);
	}

	// �л��˵�
	public void toggleMenu(View view) {
		mLeftMenu.toggle();
	}

	// ����¼�
	@Override
	public void onClick(View v) {
		resetImg();
		switch (v.getId()) {
		case R.id.main_bottom_wifi:
			setSelect(0);
			break;
		case R.id.main_bottom_map:
			setSelect(1);
			break;
		case R.id.main_bottom_leida:
			setSelect(2);
			break;
		case R.id.main_bottom_more:
			setSelect(3);
			break;
		case R.id.left_menu_levels:
			Intent intent = new Intent(this, ChartActivity.class);
			startActivity(intent);
			break;
		case R.id.left_menu_wifilocation:
			Toast.makeText(this, "2", 0).show();
			break;
		case R.id.left_menu_pwdStrength:
			Toast.makeText(this, "3", 0).show();
			break;
		case R.id.left_menu_gplot:
			Toast.makeText(this, "4", 0).show();
			break;
		case R.id.left_menu_setting:
			Toast.makeText(this, "5", 0).show();
			break;

		default:
			break;
		}
	}

	// ���ñ�ѡ�е�TAB
	private void setSelect(int i) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		hideFragment(transaction);
		// ��ͼƬ��������
		// ������������
		switch (i) {
		case 0:
			if (mFMWifi == null) {
				mFMWifi = new WifiFragment(getApplicationContext());
				transaction.add(R.id.main_fragemntlayout, mFMWifi);
			} else {
				transaction.show(mFMWifi);
			}
			mImgWifi.setImageResource(R.drawable.main_bottom_wifi_on);
			break;
		case 1:
			if (mFMMap == null) {
				mFMMap = new MapFragment(getApplicationContext());
				transaction.add(R.id.main_fragemntlayout, mFMMap);
			} else {
				transaction.show(mFMMap);
			}
			mImgMap.setImageResource(R.drawable.main_bottom_map_on);
			break;
		case 2:
			if (mFMLeida == null) {
				mFMLeida = new LeidaFragment();
				transaction.add(R.id.main_fragemntlayout, mFMLeida);
			} else {
				transaction.show(mFMLeida);
			}
			mImgLeida.setImageResource(R.drawable.main_bottom_leida_on);
			break;
		case 3:
			if (mFMMore == null) {
				mFMMore = new MoreFragment();
				transaction.add(R.id.main_fragemntlayout, mFMMore);
			} else {
				transaction.show(mFMMore);
			}
			mImgMore.setImageResource(R.drawable.main_bottom_more_on);
			break;

		default:
			break;
		}

		transaction.commit();
	}

	// �����Ϊ��������
	private void hideFragment(FragmentTransaction transaction) {
		if (mFMWifi != null) {
			transaction.hide(mFMWifi);
		}
		if (mFMMap != null) {
			transaction.hide(mFMMap);
		}
		if (mFMLeida != null) {
			transaction.hide(mFMLeida);
		}
		if (mFMMore != null) {
			transaction.hide(mFMMore);
		}
	}

	// �л�ͼƬ����ɫ
	private void resetImg() {
		mImgWifi.setImageResource(R.drawable.main_bottom_wifi_off);
		mImgMap.setImageResource(R.drawable.main_bottom_map_off);
		mImgLeida.setImageResource(R.drawable.main_bottom_leida_off);
		mImgMore.setImageResource(R.drawable.main_bottom_more_off);
	}

}