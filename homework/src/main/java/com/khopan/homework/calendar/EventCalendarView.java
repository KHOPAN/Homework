package com.khopan.homework.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class EventCalendarView extends ViewGroup {
	private final Context context;
	private final View calendarView;
	private final View eventView;

	private float separatorY;
	private float pressedX;
	private float pressedY;

	private EventCalendarView(final Context context) {
		super(context);

		if(context == null) {
			throw new IllegalArgumentException("Argument 'context' cannot be null!");
		}

		this.context = context;
		this.addView(this.calendarView = CalendarView.create(this.context));
		this.addView(this.eventView = EventView.create(this.context));
		this.separatorY = 650.0f;
		this.pressedX = 0.0f;
		this.pressedY = 0.0f;
	}

	private String action(final int action) {
		switch(action) {
		case MotionEvent.ACTION_DOWN: return "ACTION_DOWN";
		case MotionEvent.ACTION_UP: return "ACTION_UP";
		case MotionEvent.ACTION_MOVE: return "ACTION_MOVE";
		case MotionEvent.ACTION_CANCEL: return "ACTION_CANCEL";
		case MotionEvent.ACTION_OUTSIDE: return "ACTION_OUTSIDE";
		case MotionEvent.ACTION_POINTER_DOWN: return "ACTION_POINTER_DOWN";
		case MotionEvent.ACTION_POINTER_UP: return "ACTION_POINTER_UP";
		case MotionEvent.ACTION_HOVER_MOVE: return "ACTION_HOVER_MOVE";
		case MotionEvent.ACTION_POINTER_INDEX_SHIFT: return "ACTION_POINTER_INDEX_SHIFT";
		case MotionEvent.ACTION_HOVER_ENTER: return "ACTION_HOVER_ENTER";
		case MotionEvent.ACTION_HOVER_EXIT: return "ACTION_HOVER_EXIT";
		case MotionEvent.ACTION_BUTTON_PRESS: return "ACTION_BUTTON_PRESS";
		case MotionEvent.ACTION_BUTTON_RELEASE: return "ACTION_BUTTON_RELEASE";
		default: return null;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(final MotionEvent event) {
		switch(event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			this.pressedX = event.getX();
			this.pressedY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		}

		Log.i("Homework", "onInterceptTouchEvent(" + this.action(event.getActionMasked()) + ", " + event.getX() + ", " + event.getY() + ")");
		return super.onInterceptTouchEvent(event);
		//return true;
	}

	@Override
	protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
		final int width = this.getWidth();
		final int separatorY = Math.round(this.separatorY);
		this.calendarView.layout(0, 0, width, separatorY);
		this.eventView.layout(0, separatorY, width, this.getHeight());
	}

	@Override
	protected void onMeasure(final int measureWidth, final int measureHeight) {
		final int width = MeasureSpec.getSize(measureWidth);
		final int height = MeasureSpec.getSize(measureHeight);
		this.setMeasuredDimension(width, height);
		final int measuredWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
		final int separatorY = Math.round(this.separatorY);
		this.calendarView.measure(measuredWidth, MeasureSpec.makeMeasureSpec(separatorY, MeasureSpec.EXACTLY));
		this.eventView.measure(measuredWidth, MeasureSpec.makeMeasureSpec(height - separatorY, MeasureSpec.EXACTLY));
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		Log.i("Homework", "onTouchEvent(" + event.getX() + ", " + event.getY() + ")");
		//return super.onTouchEvent(event);
		return true;
	}

	/*@Override
	public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
		Log.i("Homework", "onNestedScroll()");
	}

	@Override
	public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
		Log.i("Homework", "onStartNestedScroll()");
		return true;
	}

	@Override
	public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
		Log.i("Homework", "onNestedScrollAccepted()");
	}

	@Override
	public void onStopNestedScroll(@NonNull View target, int type) {
		Log.i("Homework", "onStopNestedScroll()");
	}

	@Override
	public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
		Log.i("Homework", "onNestedScroll()");
	}

	@Override
	public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
		Log.i("Homework", "onNestedPreScroll()");
	}*/
}
