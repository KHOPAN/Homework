package com.khopan.homework.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingParent3;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class EventCalendarView extends ViewGroup implements NestedScrollingParent3, NestedScrollingChild3 {
	private final View calendarView;
	private final View eventView;

	private int divider = 500;

	public EventCalendarView(final Context context) {
		this(context, null, 0);
	}

	public EventCalendarView(final Context context, final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	public EventCalendarView(final Context context, final AttributeSet attributeSet, final int defaultStyleAttribute) {
		super(context, attributeSet, defaultStyleAttribute);
		this.calendarView = new View(context);
		this.calendarView.setBackgroundColor(Color.RED);
		this.addView(this.calendarView);
		this.eventView = new View(context);
		this.eventView.setBackgroundColor(Color.BLUE);
		this.addView(this.eventView);
	}

	@Override
	protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
		this.calendarView.layout(0, 0, right - left, this.divider);
		this.eventView.layout(0, this.divider, right - left, bottom - top);
	}

	@Override
	protected void onMeasure(final int measureWidth, final int measureHeight) {
		final int width = MeasureSpec.getSize(measureWidth);
		final int height = MeasureSpec.getSize(measureHeight);
		this.setMeasuredDimension(width, height);
		final int measuredWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
		this.calendarView.measure(measuredWidth, MeasureSpec.makeMeasureSpec(this.divider, MeasureSpec.EXACTLY));
		this.eventView.measure(measuredWidth, MeasureSpec.makeMeasureSpec(height - this.divider, MeasureSpec.EXACTLY));
	}

	@Override
	public void onNestedScroll(@NonNull final View target, final int dxConsumed, final int dyConsumed, final int dxUnconsumed, final int dyUnconsumed, final int type, final int @NonNull [] consumed) {
		consumed[1] = 300;
	}

	@Override
	public boolean onStartNestedScroll(@NonNull final View child, @NonNull final View target, final int axes, final int type) {
		return (axes & View.SCROLL_AXIS_VERTICAL) != 0;
	}

	@Override
	public void onNestedScrollAccepted(@NonNull final View child, @NonNull final View target, final int axes, final int type) {

	}

	@Override
	public void onStopNestedScroll(@NonNull final View target, final int type) {

	}

	@Override
	public void onNestedScroll(@NonNull final View target, final int dxConsumed, final int dyConsumed, final int dxUnconsumed, final int dyUnconsumed, final int type) {

	}

	@Override
	public void onNestedPreScroll(@NonNull final View target, final int dx, final int dy, final int @NonNull [] consumed, final int type) {

	}

	@Override
	public void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int @Nullable [] offsetInWindow, int type, int @NonNull [] consumed) {

	}

	@Override
	public boolean startNestedScroll(int axes, int type) {
		return (axes & View.SCROLL_AXIS_VERTICAL) != 0;
	}

	@Override
	public void stopNestedScroll(int type) {

	}

	@Override
	public boolean hasNestedScrollingParent(int type) {
		return false;
	}

	@Override
	public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int @Nullable [] offsetInWindow, int type) {
		this.divider += dyUnconsumed;
		this.requestLayout();
		CollapsingToolbarLayout
		return true;
	}

	@Override
	public boolean dispatchNestedPreScroll(int dx, int dy, int @Nullable [] consumed, int @Nullable [] offsetInWindow, int type) {
		return false;
	}
}
