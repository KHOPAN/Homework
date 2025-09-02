package com.khopan.homework;

import static com.khopan.homework.TriStateLayout.State.HALF;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

/* loaded from: classes.dex */
public class TriStateLayout extends LinearLayout {
	public enum State { ONE_WEEK, HALF, FULL }
	public enum ViewType { MONTH, REMINDER }

	private float touchStartX, touchStartY, lastY;
	private boolean intercepting;
	private boolean animating;
	private State currentState = State.ONE_WEEK;
	private State targetState = State.ONE_WEEK;
	private View contentView;
	//private View pagerContainer;

	private int swipeThreshold;
	private final Handler handler = new Handler(Looper.getMainLooper());

	private OnStateChangeListener stateChangeListener;
	private ModeChangeChecker modeChangeChecker;

	public TriStateLayout(Context context) {
		super(context);
		swipeThreshold = 30;//context.getResources()
				//.getDimensionPixelSize(R.dimen.tri_state_layout_swipe_threshold_y);
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		if (getChildCount() == 0) {
			contentView = child;
			//pagerContainer = child.findViewById(R.id.pager_container);
		}
		super.addView(child, index, params);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return handleTouch(ev) || super.dispatchTouchEvent(ev);
	}

	private boolean handleTouch(MotionEvent ev) {
		if (animating) return true;

		switch (ev.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				touchStartX = ev.getRawX();
				touchStartY = ev.getRawY();
				lastY = touchStartY;
				intercepting = true;
				break;

			case MotionEvent.ACTION_MOVE:
				if (!intercepting) break;
				float dy = ev.getRawY() - touchStartY;
				if (Math.abs(dy) > swipeThreshold) {
					targetState = chooseNextState(dy < 0);
					animateToState(targetState);
					return true;
				}
				lastY = ev.getRawY();
				break;

			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				intercepting = false;
				break;
		}
		return false;
	}

	private State chooseNextState(boolean upward) {
		switch (currentState) {
			case ONE_WEEK:
				return upward ? State.ONE_WEEK : HALF;
			case HALF:
				return upward ? State.ONE_WEEK : State.FULL;
			case FULL:
				return upward ? HALF : State.FULL;
		}
		return HALF;
	}

	private void animateToState(State newState) {
		if (contentView == null) return;
		animating = true;
		int startHeight = contentView.getHeight();
		int endHeight = getTargetHeight(newState);

		ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
		animator.setDuration(300);
		animator.setInterpolator(new DecelerateInterpolator());
		animator.addUpdateListener(a -> {
			contentView.getLayoutParams().height = (int) a.getAnimatedValue();
			contentView.requestLayout();
		});
		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				animating = false;
				currentState = newState;
				if (stateChangeListener != null) {
					stateChangeListener.onStateChanged(currentState);
				}
			}
		});
		animator.start();
	}

	private int getTargetHeight(State state) {
		switch (state) {
			case ONE_WEEK: return getHeight() / 5;
			case HALF:     return getHeight() / 2;
			case FULL:     return getHeight();
		}
		return getHeight();
	}

	// --- API ---

	public void setState(State state) {
		currentState = state;
		if (contentView != null) {
			contentView.getLayoutParams().height = getTargetHeight(state);
			contentView.requestLayout();
		}
	}

	public State getState() { return currentState; }

	public void setOnStateChangeListener(OnStateChangeListener l) {
		stateChangeListener = l;
	}

	public void setModeChangeChecker(ModeChangeChecker checker) {
		modeChangeChecker = checker;
	}

	public interface OnStateChangeListener {
		void onStateChanged(State state);
	}

	public interface ModeChangeChecker {
		boolean canChange();
	}
}
