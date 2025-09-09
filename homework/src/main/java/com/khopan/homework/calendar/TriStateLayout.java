package com.khopan.homework.calendar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class TriStateLayout extends ViewGroup {
	private final Context context;

	private int divider;

	public TriStateLayout(final Context context) {
		super(context);
		this.context = context;
		this.divider = 0;
	}

	@Override
	public void addView(final View child, final int index, final LayoutParams parameters) {
		if(this.getChildCount() > 2) {
			throw new IllegalStateException("TriStateLayout cannot have more than 2 views!");
		}

		super.addView(child, index, parameters);
	}

	@Override
	protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
		final int childCount = this.getChildCount();

		if(childCount < 1) {
			return;
		}

		final int width = right - left;
		final int height = bottom - top;

		if(childCount < 2) {
			this.getChildAt(0).layout(0, 0, width, height);
			return;
		}

		this.getChildAt(0).layout(0, 0, width, this.divider);
		this.getChildAt(1).layout(0, this.divider, width, height);
	}

	@Override
	protected void onMeasure(int widthMeasure, int heightMeasure) {
		final int width = MeasureSpec.getSize(widthMeasure);
		final int height = MeasureSpec.getSize(heightMeasure);
		this.setMeasuredDimension(width, height);
		this.getChildAt(0).layout(0, 0, width, this.divider);
		this.getChildAt(1).layout(0, this.divider, width, height);
	}
}
