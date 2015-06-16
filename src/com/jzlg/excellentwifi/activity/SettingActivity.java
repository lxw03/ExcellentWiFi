package com.jzlg.excellentwifi.activity;

import com.jzlg.excellentwifi.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ����
 * 
 * @author �δ���
 *
 */
public class SettingActivity extends Activity implements OnClickListener {
	private ActionBar actionBar;
	private TextView setProt;
	private SharedPreferences sharedPreferences;
	private Editor edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setting);
		initView();
		initEvent();
	}

	// ��ʼ���ؼ�
	private void initView() {
		actionBar = getActionBar();
		actionBar.setTitle("����");
		actionBar.setLogo(R.drawable.left_menu_setting_white);
		actionBar.setDisplayHomeAsUpEnabled(true);// ��������ͼ��
		setProt = (TextView) findViewById(R.id.setting_setprot);
		sharedPreferences = getSharedPreferences("prot", MODE_PRIVATE);
	}

	// ��ʼ���¼�
	private void initEvent() {
		setProt.setOnClickListener(this);
	}

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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_setprot:
			doSetProt();
			break;

		default:
			break;
		}
	}

	private void doSetProt() {
		LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.setting_prot, null);
		final EditText editText = (EditText) layout
				.findViewById(R.id.setting_proted);
		Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.setTitle("���ö˿ں�");
		alertDialog.setView(layout);
		alertDialog.setCancelable(false);
		alertDialog.setPositiveButton("ȷ��",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String protStr = editText.getText().toString();
						if(!("").equals(protStr)){
							//��������
							edit = sharedPreferences.edit();
							edit.putString("protNumber", protStr);
							edit.commit();
							edit.clear();
						}
					}
				});
		alertDialog.setNegativeButton("ȡ��",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		alertDialog.show();
	}

}
