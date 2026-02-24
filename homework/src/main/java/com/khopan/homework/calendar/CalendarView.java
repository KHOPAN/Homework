package com.khopan.homework.calendar;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;

import com.khopan.homework.database.HomeworkDatabase;
import com.khopan.homework.database.entity.Assignment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private float selectorX;
	private float selectorY;
	private float startSelectorX;
	private float startSelectorY;
	private float targetSelectorX;
	private float targetSelectorY;

	private LocalDate date;

	private boolean pressed;
	private int pressedLocation;

	public CalendarView(final EventCalendarView view, final int rows) {
		super(view.context);
		this.view = view;
		this.rows = rows;
		this.outerBounds = new RectF();
		this.innerBounds = new RectF();
		this.paint = new Paint();
		this.paint.setTextSize(50.0f);
		this.setClickable(true);
		this.animator = new ValueAnimator();
		this.animator.setInterpolator(new DecelerateInterpolator());
		this.animator.addUpdateListener(animator -> {
			final float time = (float) animator.getAnimatedValue();
			this.selectorX = (this.targetSelectorX - this.startSelectorX) * time + this.startSelectorX;
			this.selectorY = (this.targetSelectorY - this.startSelectorY) * time + this.startSelectorY;
			this.invalidate();
		});

		this.map = new HashMap<>();
	}

	@Override
	protected void onDraw(@NonNull final Canvas canvas) {
		if(this.date == null) {
			return;
		}

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
				this.paint.setColor(x == 0 ? 0xFFDB332A : 0xFFFAFAFF);
				final LocalDate date = this.date.plusDays(y * 7L + x);

				if(this.month.getMonthValue() != date.getMonthValue() || this.month.getYear() != date.getYear()) {
					this.paint.setAlpha(64);
				}

				this.drawCell(canvas, y * 7 + x, date, date.getDayOfMonth(), Math.round(left), top, Math.round(left + this.cellWidth + this.view.strokeSize), bottom);
			}
		}

		this.drawSelector(canvas, this.selectorX, this.selectorY);
	}

	@Override
	protected void onSizeChanged(final int width, final int height, final int oldWidth, final int oldHeight) {
		this.width = width;
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

	private final Map<Integer, List<String>> map;
	private YearMonth month;

	void setMonth(final YearMonth month) {
		this.month = month;
		final LocalDate start = this.month.atDay(1);
		this.date = start.minusDays(this.pressedLocation = start.getDayOfWeek().getValue() % 7);
		this.setSelector(this.pressedLocation);
		new Thread(() -> {
			final Map<Integer, List<String>> map = new HashMap<>();

			for(int y = 0; y < this.rows; y++) {
				for(int x = 0; x < 7; x++) {
					final int index = y * 7 + x;
					final long time = this.date.plusDays(index).toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC);
					final List<Assignment> list = HomeworkDatabase.getInstance(this.view.context.getApplicationContext()).getAssignment().range(time, time + 86400);

					if(list == null || list.isEmpty()) {
						continue;
					}

					final List<String> strings = new ArrayList<>();

					for(final Assignment assignment : list) {
						strings.add(assignment.title);
					}

					map.put(index, strings);
				}
			}

			this.post(() -> {
				this.map.clear();
				this.map.putAll(map);
				this.invalidate();
			});
		}).start();
	}

	private void drawCell(final Canvas canvas, final int index, final LocalDate date, final int day, final float left, final float top, final float right, final float bottom) {
		final Paint.FontMetrics metrics = this.paint.getFontMetrics();
		final String text = Integer.toString(day);
		float y = top - metrics.ascent;
		canvas.drawText(text, (left + right) / 2.0f - this.paint.measureText(text) / 2.0f, y, this.paint);
		final List<String> list = this.map.get(index);

		if(list == null) {
			return;
		}

		for(final String line : list) {
			final TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
			paint.setTextSize(this.paint.getTextSize());
			paint.setColor(0xFFFFFFFF);
			final StaticLayout layout = StaticLayout.Builder.obtain(line, 0, line.length(), paint, Math.round(right - left))
					.setEllipsize(TextUtils.TruncateAt.END)
					.setMaxLines(2)
					.build();

			canvas.save();
			canvas.clipRect(left, top, right, bottom);
			canvas.translate(left, y);
			layout.draw(canvas);
			y += layout.getHeight();
			canvas.restore();
		}
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
			this.paint.setColor(0xFF545454);
			canvas.drawDoubleRoundRect(this.outerBounds, this.view.arcSize, this.view.arcSize, this.innerBounds, innerArcSize, innerArcSize, this.paint);
		}
	}

	@Override
	public boolean dispatchTouchEvent(final MotionEvent event) {
		final float touchX = event.getX();
		final float touchY = event.getY();

		switch(event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			for(int y = 0; y < this.rows; y++) {
				final float rawTop = this.cellTop * y + this.cellOffset;
				final int top = Math.round(rawTop);
				final int bottom = Math.round(rawTop + this.cellBottom);

				for(int x = 0; x < 7; x++) {
					final float left = this.cellWidth * x;

					if(touchX >= Math.round(left) && touchY >= top && touchX <= Math.round(left + this.cellWidth + this.view.strokeSize) && touchY <= bottom) {
						this.pressed = true;
						this.pressedLocation = y * 7 + x;
						return true;
					}
				}
			}

			break;
		case MotionEvent.ACTION_UP:
			if(!this.pressed) {
				break;
			}

			this.pressed = false;
			this.select(this.pressedLocation);
			return true;
		}

		return super.dispatchTouchEvent(event);
	}

	void select(final int position) {
		final LocalDate date = this.date.plusDays(position);

		if(this.month.getMonthValue() != date.getMonthValue() || this.month.getYear() != date.getYear()) {
			this.view.calendarView.viewPager.setCurrentItem((int) ChronoUnit.MONTHS.between(EventCalendarView.EPOCH_MONTH, date));
		}

		this.view.eventView.viewPager.setCurrentItem((int) date.toEpochDay());
		this.setSelector(position);
	}

	private void setSelector(final int position) {
		this.startSelectorX = this.selectorX;
		this.startSelectorY = this.selectorY;
		this.targetSelectorX = position % 7;
		this.targetSelectorY = position / 7;
		this.animator.setFloatValues(0.0f, 1.0f);
		this.animator.start();
	}
}
