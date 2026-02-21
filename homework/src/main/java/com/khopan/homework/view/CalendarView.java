package com.khopan.homework.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.YearMonth;

@SuppressLint("ViewConstructor")
public class CalendarView extends View {
	private final EventCalendarView view;
	private final int rows;
	private final RectF outerBounds;
	private final RectF innerBounds;
	private final Paint paint;
	private final ValueAnimator animator;

	private int width;
	private float cellWidth;
	private float cellTop;
	private float cellBottom;
	private float cellOffset;
	private float dividerValue;
	private int dividerColorValue;
	//private float offsetIndex;
	private float selectorX;
	private float selectorY;
	private float startSelectorX;
	private float startSelectorY;
	private float targetSelectorX;
	private float targetSelectorY;

	private LocalDate date;

	public CalendarView(final EventCalendarView view, final int rows) {
		super(view.context);
		this.view = view;
		this.rows = rows;
		this.outerBounds = new RectF();
		this.innerBounds = new RectF();
		this.paint = new Paint();
		this.paint.setTextSize(50.0f);
		//this.offsetIndex = 2.0f;
		this.setClickable(true);
		this.setFocusable(true);
		this.setBackground(null);
		this.animator = new ValueAnimator();
		this.animator.addUpdateListener(animator -> {
			final float time = (float) animator.getAnimatedValue();
			this.selectorX = (this.targetSelectorX - this.startSelectorX) * time + this.startSelectorX;
			this.selectorY = (this.targetSelectorY - this.startSelectorY) * time + this.startSelectorY;
			this.invalidate();
		});
	}

	@Override
	protected void onDraw(@NonNull final Canvas canvas) {
		for(int y = 0; y < this.rows; y++) {
			final float rawTop = this.cellTop * y + this.cellOffset;
			final int top = Math.round(rawTop);
			final int bottom = Math.round(rawTop + this.cellBottom);

			if(this.dividerValue > 0.0f) {
				this.paint.setColor(this.dividerColorValue);
				canvas.drawRect(0.0f, top - this.dividerValue, this.width, top, this.paint);
			}

			for(int x = 0; x < 7; x++) {
				final float left = this.cellWidth * x;
				this.drawCell(canvas, this.date == null ? 0 : this.date.plusDays(y * 7L + x).getDayOfMonth(), Math.round(left), top, Math.round(left + this.cellWidth + this.view.strokeSize), bottom);
			}
		}

		this.drawSelector(canvas, 2, this.selectorY);
	}

	@Override
	protected void onSizeChanged(final int width, final int height, final int oldWidth, final int oldHeight) {
		this.width = width;
		Log.d("CalendarView", "CalendarView: " + width);
		final float topProgress = Math.min(Math.max((this.view.divider - this.view.dividerWeek) / (float) (this.view.dividerSplit - this.view.dividerWeek), 0.0f), 1.0f);
		final float bottomProgress = Math.min(Math.max((this.view.divider - this.view.dividerSplit) / (float) (this.view.dividerMonth - this.view.dividerSplit), 0.0f), 1.0f);
		this.cellWidth = (this.width - this.view.strokeSize * 8.0f) / 7.0f + this.view.strokeSize;
		this.dividerValue = this.view.dividerSize * bottomProgress;
		this.dividerColorValue = this.view.dividerColor | (Math.round(0xFF * bottomProgress) << 24);
		float cellHeight = (Math.max(height, this.view.dividerSplit - this.view.headerSize) - this.view.strokeSize * (this.rows + (this.rows - 1) * bottomProgress + 1) - this.dividerValue * this.rows) / (float) this.rows + this.view.strokeSize;
		final float weekHeight = this.view.dividerWeek - this.view.headerSize - this.view.strokeSize;
		cellHeight = (cellHeight - weekHeight) * topProgress + weekHeight;
		this.cellTop = cellHeight + (this.view.strokeSize + this.view.dividerSize) * bottomProgress;
		this.cellBottom = cellHeight + this.view.strokeSize;
		this.cellOffset = (this.dividerValue + weekHeight * this.selectorY) * topProgress - weekHeight * this.selectorY;
	}

	void setMonth(final YearMonth month) {
		final LocalDate start = month.atDay(1);
		this.date = start.minusDays(start.getDayOfWeek().getValue() % 7);
	}

	private void drawCell(final Canvas canvas, final int day, final float left, final float top, final float right, final float bottom) {
		/*this.outerBounds.left = left;
		this.outerBounds.top = top;
		this.outerBounds.right = right;
		this.outerBounds.bottom = bottom;
		this.innerBounds.left = left + this.view.strokeSize;
		this.innerBounds.top = top + this.view.strokeSize;
		this.innerBounds.right = right - this.view.strokeSize;
		this.innerBounds.bottom = bottom - this.view.strokeSize;

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			final float innerArcSize = this.view.arcSize - this.view.strokeSize * 2.0f;
			this.paint.setColor(Color.RED);
			canvas.drawDoubleRoundRect(this.outerBounds, this.view.arcSize, this.view.arcSize, this.innerBounds, innerArcSize, innerArcSize, this.paint);
		}*/

		final Paint.FontMetrics metrics = this.paint.getFontMetrics();
		this.paint.setColor(Color.GREEN);
		final String text = Integer.toString(day);
		canvas.drawText(text, (left + right) / 2.0f - this.paint.measureText(text) / 2.0f, (top + bottom) / 2.0f - (metrics.ascent + metrics.descent) / 2.0f, this.paint);
	}

	private void drawSelector(final Canvas canvas, final float x, final float y) {
		final float rawLeft = this.cellWidth * x;
		final float rawTop = this.cellTop * y + this.cellOffset;
		final int left = Math.round(rawLeft);
		final int top = Math.round(rawTop);
		final int right = Math.round(rawLeft + this.cellWidth + this.view.strokeSize);
		final int bottom = Math.round(rawTop + this.cellBottom);
		this.outerBounds.left = left;
		this.outerBounds.top = top;
		this.outerBounds.right = right;
		this.outerBounds.bottom = bottom;
		this.innerBounds.left = left + this.view.strokeSize;
		this.innerBounds.top = top + this.view.strokeSize;
		this.innerBounds.right = right - this.view.strokeSize;
		this.innerBounds.bottom = bottom - this.view.strokeSize;

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			final float innerArcSize = this.view.arcSize - this.view.strokeSize * 2.0f;
			this.paint.setColor(Color.GREEN);
			canvas.drawDoubleRoundRect(this.outerBounds, this.view.arcSize, this.view.arcSize, this.innerBounds, innerArcSize, innerArcSize, this.paint);
		}
	}

	@Override
	public boolean dispatchTouchEvent(final MotionEvent event) {
		switch(event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			this.startSelectorY = this.selectorY;
			this.targetSelectorY = Math.round(event.getY() / this.cellTop);
			this.animator.setFloatValues(0.0f, 1.0f);
			this.animator.start();
			break;
		}

		return super.dispatchTouchEvent(event);
	}
}
