package com.khopan.homework;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class TestLayout extends ViewGroup {
	private View viewTop;
	private View viewBottom;

	private int divider;

	public TestLayout(Context context) {
		super(context);
	}

	public void setViews(final View viewTop, final View viewBottom) {
		this.viewTop = viewTop;
		this.viewBottom = viewBottom;

		if(this.viewTop != null && this.viewBottom != null) {
			this.removeAllViews();
			this.addView(this.viewTop);
			this.addView(this.viewBottom);
		}
	}

	@Override
	protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
		final int width = this.getWidth();
		final int height = this.getHeight();

		if(this.viewTop != null) {
			this.viewTop.layout(0, 0, width, this.divider);
		}

		if(this.viewBottom != null) {
			this.viewBottom.layout(0, this.divider, width, height);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			this.divider = Math.round(event.getY());
			this.requestLayout();
			break;
		}

		return false;
	}
}
