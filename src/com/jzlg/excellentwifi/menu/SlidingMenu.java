package com.jzlg.excellentwifi.menu;

import com.jzlg.excellentwifi.R;
import com.jzlg.excellentwifi.R.styleable;
import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView {
	private LinearLayout mWapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;
	private int mScreenWidth;
	private int mMenuRightpadding = 50;// ��λ��dp
	private boolean once;
	private int mMenuWidth;
	private boolean isOpen;

	/**
	 * ��ʹ�����Զ�������ʱ����ô˷���
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// ��ȡ���Ƕ��������
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(
				attrs, R.styleable.SlidingMenu, defStyleAttr, 0);
		int n = typedArray.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = typedArray.getIndex(i);
			switch (attr) {
			// Ĭ��ֵ
			case R.styleable.SlidingMenu_rightPadding:
				mMenuRightpadding = typedArray.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50, context
										.getResources().getDisplayMetrics()));
				break;

			default:
				break;
			}
		}

		// �ͷ�
		typedArray.recycle();

		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWidth = outMetrics.widthPixels;

		// ��dpת��Ϊpx
		// mMenuRightpadding = (int) TypedValue.applyDimension(
		// TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources()
		// .getDisplayMetrics());
	}

	/**
	 * @param context
	 */
	public SlidingMenu(Context context) {
		this(context, null);
	}

	/**
	 * δʹ���Զ�������ʱ����
	 * 
	 * @param context
	 *            ������
	 * @param attrs
	 *            �Զ���View
	 */
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * �û���ָ����
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		// �û���ָ̧��ʱ
		case MotionEvent.ACTION_UP:
			// ��������ߵĿ��
			int scrollX = getScrollX();
			if (scrollX >= mMenuWidth / 2) {
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			} else {
				this.smoothScrollTo(0, 0);
				isOpen = true;
			}
			return true;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * ������View���Լ��Ŀ�͸�
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (!once) {
			mWapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) mWapper.getChildAt(0);
			mContent = (ViewGroup) mWapper.getChildAt(1);
			mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
					- mMenuRightpadding;
			mContent.getLayoutParams().width = mScreenWidth;
			once = true;
		}
	}

	/**
	 * �Լ�����View�Ĳ��� ͨ������ƫ������Menu����
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(mMenuWidth, 0);
		}
	}

	/**
	 * �򿪲˵�
	 */
	public void openMenu() {
		if (isOpen) {
			return;
		}

		this.smoothScrollTo(0, 0);
		isOpen = true;
	}

	/**
	 * �رղ˵�
	 */
	public void closeMenu() {
		if (!isOpen) {
			return;
		}

		this.smoothScrollTo(mMenuWidth, 0);
		isOpen = false;
	}

	/**
	 * �л��˵�
	 */
	public void toggle() {
		if (isOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}

	/**
	 * �������ص�����
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {

		super.onScrollChanged(l, t, oldl, oldt);

		float scale = l * 1.0f / mMenuWidth;// 1~0

		float rightScale = 0.7f + 0.3f * scale;

		float leftScale = 1.0f - 0.3f * scale;
		float leftAlpha = 0.6f + 0.4f * (1 - scale);
		// �������Զ���������Translation����
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.8f);

		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		ViewHelper.setAlpha(mMenu, leftAlpha);

		// ����Content���ŵ����ĵ�
		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);

	}

}
