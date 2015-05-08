package com.jzlg.excellentwifi.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jzlg.excellentwifi.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class RefreshListView extends ListView implements OnScrollListener {

	private View header;// ���������ļ�
	private int headerHeight;// ���������ļ��ĸ߶�
	private int firstVisibleItem;// ��ǰ��һ���ɼ���item��λ��
	private int scrollState;// ��ǰ����״̬
	private boolean isRemark;// ��ʶ����ǰlistview��˰��µ�
	private int starY;// ����ʱYֵ
	private int state;// ��ǰ��״̬
	private final int NONE = 0;// ����״̬
	private final int PULL = 1;// ��ʾ����״̬
	private final int RELESE = 2;// ��ʾ�ͷ�״̬
	private final int REFRESHING = 3;// ����ˢ��״̬
	private IRefreshListener iRefreshListener;//ˢ�����ݵĽӿ�

	public RefreshListView(Context context) {
		super(context);
		initView(context);
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	/**
	 * ��ʼ�����棬��Ӷ��������ļ���listview
	 * 
	 * @param context
	 */
	private void initView(Context context) {

		header = LayoutInflater.from(context).inflate(
				R.layout.layout_listview_header, null);
		measureView(header);
		headerHeight = header.getMeasuredHeight();
		topPadding(-headerHeight);
		this.addHeaderView(header);// ��Ӷ��������ļ�
		this.setOnScrollListener(this);
	}

	/**
	 * ֪ͨ�����ִ˲��ִ�С
	 * 
	 * @param view
	 */
	private void measureView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		// spec:��߾�;padding:�ڱ߾�;childDimension:�Ӳ��ֵĿ��
		int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int height;
		int tempHeight = p.height;
		if (tempHeight > 0) {
			height = MeasureSpec.makeMeasureSpec(tempHeight,
					MeasureSpec.EXACTLY);
		} else {
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}

		view.measure(width, height);
	}

	/**
	 * ����header���ֵ��ϱ߾�
	 * 
	 * @param topPadding
	 */
	private void topPadding(int topPadding) {
		header.setPadding(header.getPaddingLeft(), topPadding,
				header.getPaddingRight(), header.getPaddingBottom());
		header.invalidate();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (firstVisibleItem == 0) {
				isRemark = true;
				starY = (int) ev.getY();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			onMove(ev);
			break;
		case MotionEvent.ACTION_UP:
			if (state == RELESE) {
				state = REFRESHING;

				// ������������
				refreshViewByState();
				iRefreshListener.onRefresh();
			} else if (state == PULL) {
				state = NONE;
				isRemark = false;
				refreshViewByState();
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * �ж��ƶ������еĲ���
	 * 
	 * @param ev
	 */
	private void onMove(MotionEvent ev) {
		if (!isRemark) {
			return;
		}
		int tempY = (int) ev.getY();// ��ǰ�ƶ���ʲôλ��
		int space = tempY - starY;// ��ǰ�ƶ��ľ���
		int topPadding = space - headerHeight;
		switch (state) {
		case NONE:
			if (space > 0) {
				state = PULL;
				refreshViewByState();
			}
			break;
		case PULL:
			topPadding(topPadding);
			if (space > headerHeight + 30
					&& scrollState == SCROLL_STATE_TOUCH_SCROLL) {
				state = RELESE;
				refreshViewByState();
			}
			break;
		case RELESE:
			topPadding(topPadding);
			if (space < headerHeight + 30) {
				state = PULL;
				refreshViewByState();
			} else if (space <= 0) {
				state = NONE;
				isRemark = false;
				refreshViewByState();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * ���ݵ�ǰ״̬�ı������ʾ
	 */
	private void refreshViewByState() {
		TextView tip = (TextView) header
				.findViewById(R.id.listview_header_right_tip_refresh);
		ImageView arrow = (ImageView) header
				.findViewById(R.id.listview_header_left_arrow);
		ProgressBar progressBar = (ProgressBar) header
				.findViewById(R.id.listview_header_left_progress);
		RotateAnimation anim1 = new RotateAnimation(0, 180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim1.setDuration(1000);// ����ʱ����500����
		anim1.setFillAfter(true);
		RotateAnimation anim2 = new RotateAnimation(180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim1.setDuration(1000);// ����ʱ����500����
		anim1.setFillAfter(true);
		switch (state) {
		case NONE:
			arrow.clearAnimation();// �Ƴ�����
			topPadding(-headerHeight);
			break;
		case PULL:
			arrow.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tip.setText("��������ˢ��");
			arrow.clearAnimation();
			arrow.setAnimation(anim2);
			break;
		case RELESE:
			arrow.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tip.setText("�ɿ�����ˢ��");
			arrow.clearAnimation();
			arrow.setAnimation(anim1);
			break;
		case REFRESHING:
			topPadding(50);
			arrow.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			tip.setText("����ˢ��...");
			arrow.clearAnimation();
			break;

		default:
			break;
		}
	}

	/**
	 * ��ȡ������
	 */
	public void refreshComplete() {
		state = NONE;
		isRemark = false;
		refreshViewByState();
		TextView lastUpdateTime = (TextView) header
				.findViewById(R.id.listview_header_right_tip_time);
		SimpleDateFormat smf = new SimpleDateFormat("yyyy��MM��dd�� hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());

		String time = smf.format(date);
		lastUpdateTime.setText(time);
	}
	
	
	public void setInterface(IRefreshListener iRefreshListener){
		this.iRefreshListener = iRefreshListener;
	}
	
	/**
	 * ˢ�����ݽӿ�
	 * @author Administrator
	 *
	 */
	public interface IRefreshListener{
		public void onRefresh();
	}

}
