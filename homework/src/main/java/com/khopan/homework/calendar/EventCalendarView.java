package com.khopan.homework.calendar;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

public class EventCalendarView extends ViewGroup {
	final Context context;
	private final View calendarView;
	private final View eventView;

	float separatorY;
	private float pressedX;
	private float pressedY;

	private final int touchSlop;

	private Position position;
	double positionWeek;
	double positionSplit;
	private double positionMonth;

	public EventCalendarView(final Context context) {
		super(context);

		if(context == null) {
			throw new IllegalArgumentException("Argument 'context' cannot be null!");
		}

		this.context = context;
		this.addView(this.calendarView = CalendarView.create(this));
		this.addView(this.eventView = EventView.create(this.context));
		this.touchSlop = ViewConfiguration.get(this.context).getScaledTouchSlop();
		this.separatorY = 650.0f;
		this.pressedX = 0.0f;
		this.pressedY = 0.0f;
		this.position = Position.SPLIT;
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

	private boolean dragging;
	private float pressedSeparatorY;

	@Override
	public boolean onInterceptTouchEvent(final MotionEvent event) {
		//Log.i("Homework", "onInterceptTouchEvent(" + this.action(event.getActionMasked()) + ", " + event.getX() + ", " + event.getY() + ")");

		switch(event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			this.pressedX = event.getX();
			this.pressedY = event.getY();
			this.pressedSeparatorY = this.separatorY;
			this.dragging = false;
			break;
		case MotionEvent.ACTION_MOVE: {
			final float deltaX = event.getX() - this.pressedX;
			final float deltaY = event.getY() - this.pressedY;
			Log.i("Homework", "deltaX: " + deltaX + " deltaY: " + deltaY);

			if(Math.abs(deltaX) >= Math.abs(deltaY) || Math.abs(deltaY) < this.touchSlop) {
				this.dragging = false;
				break;
			}

			Log.i("Homework", "Dragging");
			this.dragging = true;
			this.separatorY = this.pressedSeparatorY + deltaY + (deltaY > 0 ? -this.touchSlop : this.touchSlop);
			this.requestLayout();
			break;
		}
		case MotionEvent.ACTION_UP: {
			if(!this.dragging) {
				break;
			}

			final float deltaY = this.pressedY - event.getY();

			switch(this.position) {
			case WEEK:
				this.position = deltaY > 0 ? Position.WEEK : Position.SPLIT;
				break;
			case MONTH:
				this.position = deltaY > 0 ? Position.SPLIT : Position.MONTH;
				break;
			default:
				this.position = deltaY > 0 ? Position.WEEK : Position.MONTH;
				break;
			}

			final ValueAnimator animator = ValueAnimator.ofFloat(this.separatorY, (float) (Position.WEEK.equals(this.position) ? this.positionWeek : Position.MONTH.equals(this.position) ? this.positionMonth : this.positionSplit));
			animator.setDuration(250);
			animator.setInterpolator(new DecelerateInterpolator());
			animator.addUpdateListener(animation -> {
				this.separatorY = (float) animation.getAnimatedValue();
				this.requestLayout();
			});

			animator.start();
			this.dragging = false;
			this.postInvalidateOnAnimation();
			return true;
		}
		}

		return this.dragging;
	}

	@Override
	protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
		/*final int width = this.getWidth();
		final int separatorY = Math.round(this.separatorY);
		this.calendarView.layout(0, 0, width, separatorY);
		this.eventView.layout(0, separatorY, width, this.getHeight());*/

		final int width = this.getWidth();
		final int height = this.getHeight();
		this.positionWeek = ((double) height) / 10.0d;
		this.positionSplit = ((double) height) / 2.0d;
		this.positionMonth = height;

		if(this.separatorY < 0.0f) {
			this.separatorY = (float) (Position.WEEK.equals(this.position) ? this.positionWeek : Position.MONTH.equals(this.position) ? this.positionMonth : this.positionSplit);
		}

		final int divider = Math.round(this.separatorY = (float) Math.min(Math.max(this.separatorY, this.positionWeek), this.positionMonth));
		this.calendarView.layout(0, 0, width, divider);
		this.eventView.layout(0, divider, width, height);
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
		//Log.i("Homework", "onTouchEvent(" + event.getX() + ", " + event.getY() + ")");
		this.onInterceptTouchEvent(event);
		//return super.onTouchEvent(event);
		return true;
	}

	private enum Position {
		WEEK,
		SPLIT,
		MONTH
	}
}
