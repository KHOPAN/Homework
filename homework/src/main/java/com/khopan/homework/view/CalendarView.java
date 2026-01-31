package com.khopan.homework.view;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;

@SuppressLint("ViewConstructor")
public class CalendarView extends View {
	private final EventCalendarView view;
	private final int rows;
	private final int strokeSize;
	private final int arcSize;
	private final int dividerSize;
	private final int dividerColor;
	private final RectF outerBounds;
	private final RectF innerBounds;
	private final Paint paint;

	private int width;
	private float cellWidth;
	private float cellTop;
	private float cellBottom;
	private float dividerValue;
	private int dividerColorValue;

	public CalendarView(final EventCalendarView view, final int rows) {
		super(view.context);
		this.view = view;
		this.rows = rows;
		final DisplayMetrics metrics = this.view.context.getResources().getDisplayMetrics();
		this.strokeSize = Math.max(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.0f, metrics)), 1);
		this.arcSize = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, metrics));
		this.dividerSize = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5f, metrics));
		final TypedValue value = new TypedValue();
		this.view.context.getTheme().resolveAttribute(androidx.appcompat.R.attr.listDividerColor, value, true);
		this.dividerColor = this.view.context.getColor(value.resourceId) & 0xFFFFFF;
		this.outerBounds = new RectF();
		this.innerBounds = new RectF();
		this.paint = new Paint();
		this.paint.setTextSize(50.0f);
		Log.d("CalendarView", "Stroke width: " + this.strokeSize + " Arc size: " + this.arcSize + " Divider size: " + this.dividerSize + " Density: " + metrics.density);
	}

	@Override
	protected void onDraw(@NonNull final Canvas canvas) {
		for(int y = 0; y < this.rows; y++) {
			final float rawTop = this.cellTop * y + this.dividerValue;
			final int top = Math.round(rawTop);
			final int bottom = Math.round(rawTop + this.cellBottom);

			if(this.dividerValue > 0.0f) {
				this.paint.setColor(this.dividerColorValue);
				canvas.drawRect(0.0f, top - this.dividerValue, this.width, top, this.paint);
			}

			for(int x = 0; x < 7; x++) {
				final float left = this.cellWidth * x;
				this.drawCell(canvas, Math.round(left), top, Math.round(left + this.cellWidth + this.strokeSize), bottom);
			}
		}
	}

	@Override
	protected void onSizeChanged(final int width, final int height, final int oldWidth, final int oldHeight) {
		this.width = width;
		this.cellWidth = (this.width - this.strokeSize * 8.0f) / 7.0f + this.strokeSize;
		final float value = Math.min(Math.max((this.view.divider - this.view.dividerSplit) / (float) (this.view.dividerMonth - this.view.dividerSplit), 0.0f), 1.0f);
		this.dividerValue = this.dividerSize * value;
		this.dividerColorValue = this.dividerColor | (Math.round(0xFF * value) << 24);
		final float cellHeight = (height - this.strokeSize * (this.rows + (this.rows - 1) * value + 1) - this.dividerValue * this.rows) / (float) this.rows + this.strokeSize;
		this.cellTop = cellHeight + (this.strokeSize + this.dividerSize) * value;
		this.cellBottom = cellHeight + this.strokeSize;
	}

	private void drawCell(final Canvas canvas, final float left, final float top, final float right, final float bottom) {
		this.outerBounds.left = left;
		this.outerBounds.top = top;
		this.outerBounds.right = right;
		this.outerBounds.bottom = bottom;
		this.innerBounds.left = left + this.strokeSize;
		this.innerBounds.top = top + this.strokeSize;
		this.innerBounds.right = right - this.strokeSize;
		this.innerBounds.bottom = bottom - this.strokeSize;

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			final float innerArcSize = this.arcSize - this.strokeSize * 2.0f;
			this.paint.setColor(Color.RED);
			canvas.drawDoubleRoundRect(this.outerBounds, this.arcSize, this.arcSize, this.innerBounds, innerArcSize, innerArcSize, this.paint);
		}

		final Paint.FontMetrics metrics = this.paint.getFontMetrics();
		this.paint.setColor(Color.GREEN);
		canvas.drawText("23", (left + right) / 2.0f - this.paint.measureText("23") / 2.0f, (top + bottom) / 2.0f - (metrics.ascent + metrics.descent) / 2.0f, this.paint);
	}
}
