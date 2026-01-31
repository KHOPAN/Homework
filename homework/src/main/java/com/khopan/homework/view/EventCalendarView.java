package com.khopan.homework.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class EventCalendarView extends LinearLayout {
	final Context context;

	int divider;
	int dividerWeek;
	int dividerSplit;
	int dividerMonth;

	private final CalendarPagerHolder calendarHolder;
	private final LinearLayout.LayoutParams calendarParams;
	private final ViewPager2 eventView;
	private final LinearLayout.LayoutParams eventViewParams;
	private final ValueAnimator animator;
	private final float touchSlop;

	private RecyclerView eventRecyclerView;
	private float pressedX;
	private float pressedY;
	private int pressedDivider;
	private boolean dragging;

	public EventCalendarView(final Context context) {
		this(context, null, 0);
	}

	public EventCalendarView(final Context context, final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	public EventCalendarView(final Context context, final AttributeSet attributeSet, final int defaultStyleAttribute) {
		super(context, attributeSet, defaultStyleAttribute);
		this.context = context;
		this.setOrientation(LinearLayout.VERTICAL);
		this.addView((this.calendarHolder = new CalendarPagerHolder(this)).viewPager, this.calendarParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
		this.addView(this.eventView = EventView.create(context, view -> this.eventRecyclerView = view), this.eventViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
		this.animator = new ValueAnimator();
		this.animator.addUpdateListener(animation -> {
			this.divider = (int) animation.getAnimatedValue();
			this.update();
		});

		this.animator.setInterpolator(new DecelerateInterpolator());
		this.touchSlop = ViewConfiguration.get(this.context).getScaledTouchSlop();
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		switch(event.getActionMasked()) {
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP: {
			if(!this.dragging && (this.divider == this.dividerWeek || this.divider == this.dividerSplit || this.divider == this.dividerMonth)) {
				break;
			}

			final boolean direction = event.getY() - this.pressedY > 0;
			final int target;

			if(this.divider >= this.dividerSplit && this.divider <= this.dividerMonth) {
				target = direction ? this.dividerMonth : this.dividerSplit;
			} else if(this.divider >= this.dividerWeek && this.divider <= this.dividerSplit) {
				target = direction ? this.dividerSplit : this.dividerWeek;
			} else {
				target = this.dividerSplit;
			}

			this.animator.setIntValues(this.divider, target);
			this.animator.setDuration(Math.round((Math.abs(this.divider - target) / (double) Math.abs(this.dividerWeek - this.dividerMonth)) * 500.0d));
			this.animator.start();
			this.dragging = false;
			return true;
		}
		case MotionEvent.ACTION_MOVE: {
			final float deltaX = event.getX() - this.pressedX;
			final float deltaY = event.getY() - this.pressedY;
			this.dragging = Math.abs(deltaX) < Math.abs(deltaY) && Math.abs(deltaY) >= this.touchSlop;

			if(!this.dragging) {
				return false;
			}

			this.divider = Math.round(Math.min(Math.max(this.pressedDivider + event.getY() - this.pressedY + (deltaY > 0 ? -this.touchSlop : this.touchSlop), this.dividerWeek), this.dividerMonth));
			//Log.d("EventCalendarView", "Divider: " + this.divider + " " + event.getY());

			this.update();
			return true;
		}
		}

		return super.onTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(final MotionEvent event) {
		switch(event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			this.pressedX = event.getX();
			this.pressedY = event.getY();
			this.pressedDivider = this.divider;
			this.dragging = false;
			this.animator.cancel();
			return false;
		case MotionEvent.ACTION_MOVE: {
			final float deltaY = event.getY() - this.pressedY;
			return Math.abs(event.getX() - this.pressedX) < Math.abs(deltaY) && Math.abs(deltaY) >= this.touchSlop && (this.divider != this.dividerWeek || deltaY > 0 || this.eventRecyclerView == null || this.eventRecyclerView.canScrollVertically(-1));
		}
		}

		return super.onInterceptTouchEvent(event);
	}

	@Override
	protected void onSizeChanged(final int width, final int height, final int oldWidth, final int oldHeight) {
		final int previousWeek = this.dividerWeek;
		final int previousMonth = this.dividerMonth;
		this.dividerWeek = height / 10;
		this.dividerSplit = height / 2;
		this.dividerMonth = height;
		this.divider = this.divider == previousWeek ? this.dividerWeek : this.divider == previousMonth ? this.dividerMonth : this.dividerSplit;
		this.update();
	}

	private void update() {
		this.calendarParams.height = this.divider;
		this.eventViewParams.height = this.getHeight() - this.divider;
		this.requestLayout();
		final long time = System.nanoTime();
		//Log.d("EventCalendarView", String.format("%d FPS", Math.round(1000000000.0d / (time - this.lastTime))));
		this.lastTime = time;
	}

	private long lastTime;
}
