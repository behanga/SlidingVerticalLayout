package com.example.slidingvertical;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.example.com.example.slidingvertical.R;

/**
 * @Title: SlidingVerticalLayout.java
 * @Package com.example.com.example.slidingvertical
 * @Description: TODO(添加描述)
 * @author lichen8974#gmail.com
 * @date 2014-9-7 上午12:45:52
 * @version V1.0
 */
public class SlidingVerticalLayout extends LinearLayout {

	private final int MIN_SLIDING_DIS = 50;

	public SlidingVerticalLayout(Context context) {
		super(context);
		init(context);
	}

	public SlidingVerticalLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private Scroller mScroller;
	private float mLastMotionY;

	private void init(Context context) {
		mScroller = new Scroller(context);
	}

	public void slidingToUp() {

		final int delta = findViewById(R.id.header).getHeight() - getScrollY();
		System.out.println(getScrollY() + "  " + delta);
		mScroller.startScroll(0, getScrollY(), 0, delta, Math.abs(delta) * 2);
		invalidate();
		hasBeenUp = true;
	}

	public void slidingToDown() {

		mScroller.startScroll(0, getScrollY(), 0, -getScrollY(),
				Math.abs(getScrollY()) * 2);
		invalidate();

		hasBeenUp = false;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();

			if (mScroller.isFinished() && !isUp && hasBeenUp) {
				int height = getHeight()
						- findViewById(R.id.header).getHeight();
				findViewById(R.id.list).setLayoutParams(
						new LinearLayout.LayoutParams(getWidth(), height));
				System.out.println("computeScrollOffset");
			}
		}

	}

	boolean isUp;
	boolean hasBeenUp = false;

	private float mDownY;
	private float mDownX;

	private boolean canSlidingHorizontal;

	private boolean canSlidingVertical = false;

	public void setSlidingVertical(boolean canSlidingVertical) {
		this.canSlidingVertical = canSlidingVertical;
	}

	private int deltaY = 0;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		float absY = 0;
		float absX = 0;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = ev.getX();
			mDownY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			deltaY = (int) (mDownY - ev.getY());
			absX = Math.abs(ev.getX() - mDownX);
			absY = Math.abs(ev.getY() - mDownY);
			canSlidingHorizontal = absX > absY;
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		if (hasBeenUp && !canSlidingVertical || canSlidingHorizontal) {
			return false;
		}
		if (hasBeenUp && deltaY < 0 && canSlidingVertical) {
			return true;
		}
		if (hasBeenUp && deltaY > 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		handleTouchEvent(event);
		return true;
	}

	private void handleTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		final float y = ev.getY();
		final float x = ev.getX();
		int moveDis = 0;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mDownY = y;
			mLastMotionY = y;
			mDownX = x;
			break;

		case MotionEvent.ACTION_MOVE:
			moveDis = (int) (mDownY - ev.getY());
			int deltaX = (int) (mLastMotionY - y);
			mLastMotionY = y;
			if (deltaX > 0) {
				isUp = true;
			} else {
				isUp = false;
				isChanged = false;
			}
			if (hasBeenUp) {
				if (deltaX < 0) {
					scrollBy(0, deltaX);
				}
			} else {
				if (isUp && moveDis < 300) {
					scrollBy(0, deltaX);
				}
			}
			break;
		case MotionEvent.ACTION_UP:

			if (isUp) {
				slidingToUp();

			} else {
				slidingToDown();
			}
			break;
		}
	}

	boolean isChanged = false;

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (t > oldt && !isChanged) {
			findViewById(R.id.list).setLayoutParams(
					new LinearLayout.LayoutParams(getWidth(), getHeight()
							- findViewById(R.id.tab).getHeight()));
			isChanged = true;
		}
	}

}
