package com.khopan.homework.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.chip.SeslChipGroup;

public class EventCalendarView extends ViewGroup {
	private final ViewPager2 calendarView;
	private final ValueAnimator animator;
	private final ViewPager2 eventView;

	private float pressedX;
	private float pressedY;
	private float pressedDivider;
	private float touchSlop;
	private boolean dragging;
	private float positionWeek;
	private float positionMonth;
	private float positionSplit;
	private float divider;

	public EventCalendarView(final Context context) {
		this(context, null, 0);
	}

	public EventCalendarView(final Context context, final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	public EventCalendarView(final Context context, final AttributeSet attributeSet, final int defaultStyleAttribute) {
		super(context, attributeSet, defaultStyleAttribute);
		this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		this.calendarView = new ViewPager2(context);
		this.calendarView.setAdapter(new CalendarView.Adapter(this.calendarView));
		this.calendarView.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
		this.calendarView.requestDisallowInterceptTouchEvent(true);
		this.addView(this.calendarView);
		this.animator = new ValueAnimator();
		this.animator.setInterpolator(new DecelerateInterpolator());
		this.animator.addUpdateListener(animation -> {
			this.divider = (float) this.animator.getAnimatedValue();
			this.requestLayout();
		});

		this.eventView = new ViewPager2(context);
		this.eventView.setAdapter(new EventView.Adapter(this.eventView, null));
		this.eventView.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
		this.eventView.requestDisallowInterceptTouchEvent(true);
		this.addView(this.eventView);
		this.positionWeek = -1.0f;
		this.positionSplit = -1.0f;
		this.positionMonth = -1.0f;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		switch(event.getActionMasked()) {
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP: {
			if(!this.dragging && (this.divider == this.positionWeek || this.divider == this.positionSplit || this.divider == this.positionMonth)) {
				break;
			}

			final boolean direction = event.getY() - this.pressedY > 0;
			final float target;

			if(this.divider >= this.positionSplit && this.divider <= this.positionMonth) {
				target = direction ? this.positionMonth : this.positionSplit;
			} else if(this.divider >= this.positionWeek && this.divider <= this.positionSplit) {
				target = direction ? this.positionSplit : this.positionWeek;
			} else {
				target = this.positionSplit;
			}

			this.animator.setFloatValues(this.divider, target);
			this.animator.setDuration((int) (Math.abs(this.divider - target) / Math.abs(this.positionWeek - this.positionMonth) * 500));
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

			this.divider = Math.min(Math.max(this.pressedDivider + event.getY() - this.pressedY + (deltaY > 0 ? -this.touchSlop : this.touchSlop), this.positionWeek), this.positionMonth);
			this.requestLayout();
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
			final float deltaX = event.getX() - this.pressedX;
			final float deltaY = event.getY() - this.pressedY;

			if(Math.abs(deltaX) >= Math.abs(deltaY) || Math.abs(deltaY) < this.touchSlop) {
				return false;
			}

			final boolean direction = event.getY() - this.pressedY > 0;

			if(this.divider == this.positionWeek && !(direction && !((EventView) ((RecyclerView) this.eventView.getChildAt(0)).getLayoutManager().findViewByPosition(this.eventView.getCurrentItem())).getChildAt(2).canScrollVertically(-1))) {
				return false;
			}

			return true;
		}
		}

		return super.onInterceptTouchEvent(event);
	}

	@Override
	protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
		final int width = right - left;
		final int height = bottom - top;

		if(changed) {
			final float previousWeek = this.positionWeek;
			final float previousMonth = this.positionMonth;
			this.positionWeek = height / 10.0f;
			this.positionSplit = height / 2.0f;
			this.positionMonth = height;
			this.divider = this.divider == previousWeek ? this.positionWeek : this.divider == previousMonth ? this.positionMonth : this.positionSplit;
		}

		final int divider = Math.max(Math.round(this.divider), 0);
		this.calendarView.layout(0, 0, width, divider);
		this.eventView.layout(0, divider, width, height);
	}

	@Override
	protected void onMeasure(final int widthMeasure, final int heightMeasure) {
		final int width = MeasureSpec.getSize(widthMeasure);
		final int height = MeasureSpec.getSize(heightMeasure);
		this.setMeasuredDimension(width, height);
		final int measureWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
		final int divider = Math.round(this.divider);
		this.calendarView.measure(measureWidth, MeasureSpec.makeMeasureSpec(Math.max(divider, 0), MeasureSpec.EXACTLY));
		this.eventView.measure(measureWidth, MeasureSpec.makeMeasureSpec(Math.max(height - divider, 0), MeasureSpec.EXACTLY));
	}
}
