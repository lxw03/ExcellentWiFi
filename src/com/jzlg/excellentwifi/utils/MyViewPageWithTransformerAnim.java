package com.jzlg.excellentwifi.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * �Զ���PageView
 * 
 * @author �δ���
 *
 */
public class MyViewPageWithTransformerAnim extends ViewPager {

	private View mLeft;
	private View mRight;

	private float mTrans;
	private float mScale;

	private static final float MIN_SCALE = 0.5f;

	public MyViewPageWithTransformerAnim(Context context, AttributeSet attrs) {

		super(context, attrs);
	}

	@Override
	protected void onPageScrolled(int position, float offset, int offsetPixels) {
		super.onPageScrolled(position, offset, offsetPixels);
	}

}
