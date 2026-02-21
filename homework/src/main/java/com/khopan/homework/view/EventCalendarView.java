package com.khopan.homework.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.time.YearMonth;

public class EventCalendarView extends LinearLayout {
	static final YearMonth EPOCH_MONTH = YearMonth.of(1970, 1);

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
	private float cellWidth;

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
	public void requestLayout() {
		if(this.calendarViewParams == null) {
			super.requestLayout();
			return;
		}

		final int height = this.calendarViewParams.height;
		this.calendarViewParams.height = height + 1;
		super.requestLayout();
		// F*** you, if it works, it works.
		new Thread(() -> {
			try {
				Thread.sleep(10);
			} catch(final Throwable ignored) {}

			this.post(() -> {
				this.calendarViewParams.height = height;
				super.requestLayout();
			});
		}).start();
	}

	@Override
	protected void dispatchDraw(@NonNull final Canvas canvas) {
		super.dispatchDraw(canvas);
		final Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setTextSize(30.0f);

		for(int x = 0; x < 7; x++) {
			final String text = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}[x];
			final float center = this.cellWidth * x + (this.cellWidth + this.strokeSize - paint.measureText(text)) / 2.0f + 35;
			//canvas.drawCircle(center, this.headerSize, 10.0f, paint);
			canvas.drawText(text, center, this.headerSize, paint);
		}
	}

	@Override
	protected void onSizeChanged(final int width, final int height, final int oldWidth, final int oldHeight) {
		this.cellWidth = (width - 70 - this.strokeSize * 8.0f) / 7.0f + this.strokeSize;
		Log.d("EventCalendarView", "EventCalendarView: " + width);

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
