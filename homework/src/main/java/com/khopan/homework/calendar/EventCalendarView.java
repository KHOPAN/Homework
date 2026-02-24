package com.khopan.homework.calendar;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class EventCalendarView extends LinearLayout {
	static final LocalDate EPOCH_DAY = LocalDate.of(1970, 1, 1);
	static final YearMonth EPOCH_MONTH = YearMonth.of(1970, 1);
	static final LocalDateTime EPOCH_TIME = LocalDateTime.of(1970, 1, 1, 0, 0);

	final Context context;
	final CalendarPagerHolder calendarView;
	final EventPagerHolder eventView;
	final int arcSize;
	final int dividerColor;
	final int dividerSize;
	final int headerSize;
	final int strokeSize;

	int divider;
	int dividerWeek;
	int dividerSplit;
	int dividerMonth;

	private final LinearLayout.LayoutParams calendarViewParams;
	private final float touchSlop;
	private final ValueAnimator animator;

	private float pressedX;
	private float pressedY;
	private int pressedDivider;
	private boolean dragging;
	private float draggedY;

	public EventCalendarView(final Context context) {
		super(context);
		this.context = context;
		this.setOrientation(LinearLayout.VERTICAL);
		this.calendarView = new CalendarPagerHolder(this);
		this.calendarViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
		this.addView(this.calendarView.viewPager, this.calendarViewParams);
		this.eventView = new EventPagerHolder(this);
		this.addView(this.eventView.viewPager, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		final DisplayMetrics metrics = this.context.getResources().getDisplayMetrics();
		this.arcSize = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, metrics));
		final TypedValue value = new TypedValue();
		this.context.getTheme().resolveAttribute(androidx.appcompat.R.attr.listDividerColor, value, true);
		this.dividerColor = this.context.getColor(value.resourceId) & 0xFFFFFF;
		this.dividerSize = Math.max(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5f, metrics)), 1);
		this.headerSize = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50.0f, metrics));
		this.strokeSize = Math.max(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.0f, metrics)), 1);
		this.touchSlop = ViewConfiguration.get(this.context).getScaledTouchSlop();
		this.animator = new ValueAnimator();
		this.animator.addUpdateListener(animator -> {
			this.divider = (int) animator.getAnimatedValue();
			this.update();
		});

		this.animator.setInterpolator(new DecelerateInterpolator());
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		switch(event.getActionMasked()) {
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP: {
			if(this.divider == this.dividerWeek || this.divider == this.dividerSplit || this.divider == this.dividerMonth) {
				break;
			}

			final boolean direction = event.getY() - this.pressedY > 0;
			final int target = this.divider >= this.dividerSplit && this.divider <= this.dividerMonth ? direction ? this.dividerMonth : this.dividerSplit : this.divider >= this.dividerWeek && this.divider <= this.dividerSplit ? direction ? this.dividerSplit : this.dividerWeek : this.dividerSplit;
			this.animator.setDuration(Math.round((Math.abs(this.divider - target) / (double) (this.dividerMonth - this.dividerWeek)) * 500.0d));
			this.animator.setIntValues(this.divider, target);
			this.animator.start();
			this.dragging = false;
			return true;
		}
		case MotionEvent.ACTION_MOVE: {
			this.divider = Math.round(Math.min(Math.max(this.pressedDivider + event.getY() - this.draggedY, this.dividerWeek), this.dividerMonth));
			this.update();
			return true;
		}
		}

		return super.onTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(final MotionEvent event) {
		switch(event.getActionMasked()) {
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			this.animator.resume();
			break;
		case MotionEvent.ACTION_DOWN:
			this.pressedX = event.getX();
			this.pressedY = event.getY();
			this.pressedDivider = this.divider;
			this.dragging = false;
			this.animator.pause();
			return false;
		case MotionEvent.ACTION_MOVE: {
			final float deltaY = event.getY() - this.pressedY;

			if(!this.dragging && Math.abs(deltaY) > Math.abs(event.getX() - this.pressedX) && Math.abs(deltaY) >= this.touchSlop) {
				this.dragging = true;
				this.draggedY = event.getY();
				this.animator.cancel();
			}

			return this.dragging && (this.divider != this.dividerWeek || (deltaY > 0 && this.eventView.recyclerView != null && !this.eventView.recyclerView.canScrollVertically(-1)));
		}
		}

		return super.onInterceptTouchEvent(event);
	}

	@Override
	protected void onSizeChanged(final int width, final int height, final int oldWidth, final int oldHeight) {
		final float progress = this.dividerMonth <= 0 ? 1.0f : this.divider <= this.dividerSplit ? (this.divider - this.dividerWeek) / (float) (this.dividerSplit - this.dividerWeek) : (this.divider - this.dividerSplit) / (float) (this.dividerMonth - this.dividerSplit) + 1.0f;
		this.dividerWeek = this.headerSize * 2;
		this.dividerSplit = Math.round((this.dividerWeek - this.headerSize - this.strokeSize * 2.0f) * 5.0f + this.strokeSize * 6.0f + this.headerSize);
		this.dividerMonth = height;
		this.divider = Math.round(progress <= 1.0f ? (this.dividerSplit - this.dividerWeek) * progress + this.dividerWeek : (this.dividerMonth - this.dividerSplit) * (progress - 1.0f) + this.dividerSplit);
		this.update();
	}

	private void update() {
		this.calendarViewParams.height = this.divider;
		this.requestLayout();
	}
}
