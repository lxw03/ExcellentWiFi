package com.jzlg.excellentwifi.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.jzlg.excellentwifi.R;
import com.jzlg.excellentwifi.utils.DepthPageTransformer;
import com.jzlg.excellentwifi.utils.ViewPagerCompat;
import com.jzlg.excellentwifi.utils.ViewPagerCompat.OnPageChangeListener;

/**
 * ����ҳ
 * 
 * @author �δ���
 *
 */
public class GuideActivity extends BaseActivity implements OnPageChangeListener {
	private ViewPagerCompat mViewPager;
	private PagerAdapter mPagerAdapter;
	private List<ImageView> imageViews;
	private Button toMain;
	private int[] mImgIds = new int[] { R.drawable.guide01, R.drawable.guide02 };
	private int mPosition;// ��ǰҳ��λ��
	private boolean isFirst;// �Ƿ��ǵ�һ�ν���
	private SharedPreferences sharedPreferences;
	private Editor edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_guide);
		init();
	}

	private void init() {
		toMain = (Button) findViewById(R.id.guide_tomain);
		mViewPager = (ViewPagerCompat) findViewById(R.id.guide_viewpager);
		sharedPreferences = getSharedPreferences("isFirst", MODE_PRIVATE);
		isFirst = sharedPreferences.getBoolean("isFirst", true);

		if (!isFirst) {
			mImgIds = new int[] { R.drawable.guide03 };
			toMain.setVisibility(View.VISIBLE);
		}
		imageViews = new ArrayList<ImageView>();
		for (int i = 0; i < mImgIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(mImgIds[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
		}
		mViewPager.setPageTransformer(true, new DepthPageTransformer());// ���ö���
		mPagerAdapter = new com.jzlg.excellentwifi.adapter.PagerAdapter(this,
				imageViews);// ������
		mViewPager.setAdapter(mPagerAdapter);// ����������

		// ������ҳ����1ʱ�����Ǹ�������ҳ��ı��¼���Ҳ�����û��ǵ�һ�ν���Ӧ��
		if (mImgIds.length > 1) {
			mViewPager.setOnPageChangeListener(this);
		}

		toMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edit = sharedPreferences.edit();
				edit.putBoolean("isFirst", false);
				edit.commit();
				edit.clear();
				// �������һҳ��ת��ҳ
				Intent intent = new Intent(GuideActivity.this,
						MainActivity.class);
				GuideActivity.this.startActivity(intent);
				GuideActivity.this.finish();
			}
		});
	}

	// ��ǰ�µ�ҳ�汻ѡ��ʱ
	@Override
	public void onPageSelected(int position) {
		mPosition = position;
		if (mImgIds.length - 1 == position) {
			toMain.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * ������״̬���иı�ʱ 1:��ʼ���� 2:ֹͣ���� 0:ʲô��û��
	 */
	@Override
	public void onPageScrollStateChanged(int state) {
		switch (state) {
		case 1:// ��ʼ����
			toMain.setVisibility(View.GONE);
			break;
		case 2:// ֹͣ����
			break;
		case 0:// ʲô��û��
			if (mPosition == mImgIds.length - 1) {
				toMain.setVisibility(View.VISIBLE);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * ��ҳ�汻����ʱ position����ǰҳ�棬������������ҳ�� positionOffset����ǰҳ��ƫ�Ƶİٷֱ�
	 * positionOffsetPixels����ǰҳ��ƫ�Ƶ�����λ��
	 */
	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

}
