package com.jzlg.excellentwifi.utils;

import com.nineoldandroids.view.ViewHelper;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

/**
 * PageView�л�����
 * 
 * @author
 *
 */
public class RotateDownPageTransformer implements PageTransformer {

	// ��ת
	private static final float MAX_ROTATE = 20f;
	private float mRot;

	// Aҳ�Ƕȱ仯-20~0 Bҳ�Ƕȱ仯 0~20
	@Override
	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();
		if (position < -1) {
			ViewHelper.setRotation(view, 0);

		} else if (position <= 0) {// Aҳ0.0~1

			// 0~-20
			mRot = position * MAX_ROTATE;
			ViewHelper.setPivotX(view, pageWidth / 2);
			ViewHelper.setPivotY(view, view.getMeasuredHeight());
			ViewHelper.setRotation(view, mRot);
		} else if (position <= 1) {// Bҳ1~0.0
			// 20 ~ 0
			mRot = position * MAX_ROTATE;
			ViewHelper.setPivotX(view, pageWidth / 2);
			ViewHelper.setPivotY(view, view.getMeasuredHeight());
			ViewHelper.setRotation(view, mRot);
		} else {
			ViewHelper.setRotation(view, 0);
		}

	}

}
