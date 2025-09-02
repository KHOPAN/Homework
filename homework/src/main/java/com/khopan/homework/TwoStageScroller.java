package com.khopan.homework;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;

import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.ViewCompat;

public class TwoStageScroller extends ViewGroup implements NestedScrollingParent2 {
	/*public View topView;
	public View bottomView;

	private int currentOffset = 0; // 0 means "perfectly centered"
	private int maxOffset; // the height available for expansion/collapse

	private float lastY;
	private boolean isDragging = false;

	public TwoStageScroller(Context context) {
		super(context);
	}

	public TwoStageScroller(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (getChildCount() != 2) {
			throw new IllegalStateException("TwoStageScroller must have exactly 2 children");
		}
		topView = getChildAt(0);
		bottomView = getChildAt(1);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		maxOffset = height; // max shift is one full screen height

		measureChild(topView, widthMeasureSpec, heightMeasureSpec);
		measureChild(bottomView, widthMeasureSpec, heightMeasureSpec);

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();

		// Apply offset: positive -> bottom expands, top shrinks
		int topHeight = height - currentOffset;
		int bottomHeight = height - topHeight;

		topView.layout(0, 0, width, topHeight);
		bottomView.layout(0, topHeight, width, topHeight + bottomHeight);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				lastY = ev.getY();
				isDragging = false;
				break;
			case MotionEvent.ACTION_MOVE:
				float dy = ev.getY() - lastY;
				if (Math.abs(dy) > 10) {
					isDragging = true;
					return true;
				}
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				lastY = ev.getY();
				return true;
			case MotionEvent.ACTION_MOVE:
				float dy = ev.getY() - lastY;
				lastY = ev.getY();
				currentOffset -= dy; // drag direction inverted
				currentOffset = Math.max(0, Math.min(maxOffset, currentOffset));
				requestLayout();
				return true;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				snapToMiddle();
				return true;
		}
		return super.onTouchEvent(ev);
	}

	private void snapToMiddle() {
		int target = maxOffset / 2; // middle position
		ValueAnimator animator = ValueAnimator.ofInt(currentOffset, target);
		animator.setDuration(300);
		animator.addUpdateListener(a -> {
			currentOffset = (int) a.getAnimatedValue();
			requestLayout();
		});
		animator.start();
	}*/

	private View topView, bottomView;
	private int dividerY; // current divider position
	private int minDivider, maxDivider, midDivider;
	private OverScroller scroller;
	private GestureDetector gestureDetector;
	private int topPos, midPos, bottomPos;


	public TwoStageScroller(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		scroller = new OverScroller(context);
		gestureDetector = new GestureDetector(context, new GestureListener());
	}

	public void setViews(View top, View bottom) {
		removeAllViews();
		topView = top;
		bottomView = bottom;
		addView(topView);
		addView(bottomView);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		setMeasuredDimension(width, height);

		// both children always measured full screen
		measureChild(topView, widthMeasureSpec, heightMeasureSpec);
		measureChild(bottomView, widthMeasureSpec, heightMeasureSpec);

		/*// divider starts at middle
		midDivider = height / 2;
		if (dividerY == 0) dividerY = midDivider;

		minDivider = (int) (height * 0.2); // fully collapse top
		maxDivider = (int) (height * 0.8); // fully collapse bottom*/
		// three snapping points
		topPos = (int) (height * 0.2f);     // mostly top expanded
		midPos = height / 2;                // equal split
		bottomPos = (int) (height * 0.8f);  // mostly bottom expanded

		if (dividerY == 0) dividerY = midPos;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int width = getWidth();
		int height = getHeight();

		// top view: fills from top to divider
		topView.layout(0, 0, width, dividerY);

		// bottom view: fills from divider to bottom
		bottomView.layout(0, dividerY, width, height);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/*gestureDetector.onTouchEvent(event);

		if (event.getAction() == MotionEvent.ACTION_UP ||
				event.getAction() == MotionEvent.ACTION_CANCEL) {
			// snap to nearest state
			int target;
			if (dividerY < midDivider) target = minDivider; // top expanded
			else if (dividerY > midDivider) target = maxDivider; // bottom expanded
			else target = midDivider; // back to middle

			scroller.startScroll(0, dividerY, 0, target - dividerY, 300);
			invalidate();
		}
		return true;*/
		gestureDetector.onTouchEvent(event);

		if (event.getAction() == MotionEvent.ACTION_UP ||
				event.getAction() == MotionEvent.ACTION_CANCEL) {

			// compute nearest snap
			int[] snapPoints = {topPos, midPos, bottomPos};
			int nearest = snapPoints[0];
			int minDist = Math.abs(dividerY - nearest);

			for (int pos : snapPoints) {
				int dist = Math.abs(dividerY - pos);
				if (dist < minDist) {
					minDist = dist;
					nearest = pos;
				}
			}

			scroller.startScroll(0, dividerY, 0, nearest - dividerY, 300);
			invalidate();
		}
		return true;
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			dividerY = scroller.getCurrY();
			requestLayout();
			invalidate();
		}
	}

	private class GestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
								float distanceX, float distanceY) {
			//dividerY -= (int) distanceY;
			//dividerY = Math.max(minDivider, Math.min(dividerY, maxDivider));
			//requestLayout();
			dividerY -= (int) distanceY;
			dividerY = Math.max(topPos, Math.min(dividerY, bottomPos));
			requestLayout();

			return true;
		}
	}

	@Override
	public boolean onStartNestedScroll(View child, View target, int axes, int type) {
		return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
	}

	@Override
	public void onNestedScrollAccepted(View child, View target, int axes, int type) {}

	@Override
	public void onStopNestedScroll(View target, int type) {
		snapToNearest(); // snap after scroll ends
	}

	@Override
	public void onNestedPreScroll(View target, int dx, int dy, int[] consumed, int type) {
		// dy > 0 = scrolling up, dy < 0 = scrolling down
		boolean handled = false;

		if (dy > 0 && !target.canScrollVertically(1)) {
			// child can’t scroll further down → parent takes it
			dividerY = Math.min(bottomPos, dividerY + dy);
			handled = true;
		} else if (dy < 0 && !target.canScrollVertically(-1)) {
			// child can’t scroll further up → parent takes it
			dividerY = Math.max(topPos, dividerY + dy);
			handled = true;
		}

		if (handled) {
			consumed[1] = dy; // mark as consumed
			requestLayout();
		}
	}

	// not used much here, but required
	@Override
	public void onNestedScroll(View target, int dxConsumed, int dyConsumed,
							   int dxUnconsumed, int dyUnconsumed, int type) {}

	private void snapToNearest() {
		int[] snapPoints = {topPos, midPos, bottomPos};
		int nearest = snapPoints[0];
		int minDist = Math.abs(dividerY - nearest);

		for (int pos : snapPoints) {
			int dist = Math.abs(dividerY - pos);
			if (dist < minDist) {
				minDist = dist;
				nearest = pos;
			}
		}
		scroller.startScroll(0, dividerY, 0, nearest - dividerY, 300);
		invalidate();
	}

	private int lastY;
	private final int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();


	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				// start fresh
				lastY = (int) ev.getY();
				scroller.abortAnimation();
				break;

			case MotionEvent.ACTION_MOVE:
				int dy = (int) (ev.getY() - lastY);
				if (Math.abs(dy) > touchSlop) {
					// Intercept vertical drags
					return true;
				}
				break;
		}
		return false;
	}

}
