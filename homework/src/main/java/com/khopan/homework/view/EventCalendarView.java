package com.khopan.homework.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class EventCalendarView extends ViewGroup {
	private final View calendarView;

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
	}

	@Override
	protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
		this.calendarView.layout(0, 0, right - left, bottom - top);
	}

	@Override
	protected void onMeasure(final int measureWidth, final int measureHeight) {
		final int width = MeasureSpec.getSize(measureWidth);
		final int height = MeasureSpec.getSize(measureHeight);
		this.setMeasuredDimension(width, height);
		final int measuredWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
		//final int separatorY = Math.round(this.separatorY);
		this.calendarView.measure(measuredWidth, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
		//this.eventView.measure(measuredWidth, MeasureSpec.makeMeasureSpec(height - separatorY, MeasureSpec.EXACTLY));
	}
}
