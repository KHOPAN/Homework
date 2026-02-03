package com.khopan.homework.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

public class EventCalendarView extends LinearLayout {
	final Context context;
	final CalendarPagerHolder calendarView;
	final EventPagerHolder eventView;
	final int arcSize;
	final int dividerColor;
	final int dividerSize;
	final int strokeSize;

	int divider;
	int dividerWeek;
	int dividerSplit;
	int dividerMonth;

	private final LinearLayout.LayoutParams calendarViewParams;
	private final LinearLayout.LayoutParams eventViewParams;
	private final float touchSlop;
	private final ValueAnimator animator;

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
		this.calendarView = new CalendarPagerHolder(this);
		this.calendarViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
		this.addView(this.calendarView.viewPager, this.calendarViewParams);
		this.eventView = new EventPagerHolder(this);
		this.eventViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
		this.addView(this.eventView.viewPager, this.eventViewParams);
		final DisplayMetrics metrics = this.context.getResources().getDisplayMetrics();
		this.arcSize = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, metrics));
		final TypedValue value = new TypedValue();
		this.context.getTheme().resolveAttribute(androidx.appcompat.R.attr.listDividerColor, value, true);
		this.dividerColor = this.context.getColor(value.resourceId) & 0xFFFFFF;
		this.dividerSize = Math.max(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5f, metrics)), 1);
		this.strokeSize = Math.max(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.0f, metrics)), 1);
		this.touchSlop = ViewConfiguration.get(this.context).getScaledTouchSlop();
		this.animator = new ValueAnimator();
		this.animator.addUpdateListener(animator -> {
			this.divider = (int) animator.getAnimatedValue();
			this.update(this.getHeight());
		});

		this.animator.setInterpolator(new DecelerateInterpolator());
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

			this.update(this.getHeight());
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
		final float progress = this.divider <= this.dividerSplit ? (this.divider - this.dividerWeek) / (float) (this.dividerSplit - this.dividerWeek) : (this.divider - this.dividerSplit) / (float) (this.dividerMonth - this.dividerSplit) + 1.0f;
		this.dividerSplit = Math.round(height / 2.0f);
		this.dividerWeek = Math.round((this.dividerSplit - this.strokeSize * 6.0f) / 5.0f + this.strokeSize * 2.0f);
		this.dividerMonth = height;
		this.divider = Math.round(progress <= 1.0f ? (this.dividerSplit - this.dividerWeek) * progress + this.dividerWeek : (this.dividerMonth - this.dividerSplit) * (progress - 1.0f) + this.dividerSplit);
		this.update(height);
	}

	private void update(final int height) {
		this.calendarViewParams.height = this.divider;
		this.eventViewParams.height = height - this.divider;
		this.requestLayout();
	}
}
