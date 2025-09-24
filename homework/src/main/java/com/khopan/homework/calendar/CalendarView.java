package com.khopan.homework.calendar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

class CalendarView extends View {
	private static final YearMonth EPOCH = YearMonth.from(LocalDate.ofEpochDay(0L));

	private final EventCalendarView view;
	private final Paint paint;

	private YearMonth currentMonth;
	private LocalDate currentDate;
	private int rows;

	private CalendarView(@NonNull final EventCalendarView view) {
		super(view.context);
		this.view = view;
		this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.paint = new Paint();
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		final double factor = Math.min(Math.max(1.0d - (this.view.separatorY - this.view.positionWeek) / (this.view.positionSplit - this.view.positionWeek), 0.0d), 1.0d);

		final int width = this.getWidth();
		final float cellWidth = ((float) width) / 7.0f;
		final float cellHeight = ((float) Math.max(this.getHeight(), this.view.positionSplit)) / ((float) this.rows);
		final float offset = (float) (-cellHeight * factor);

		for(int y = 0; y < this.rows; y++) {
			for(int x = 0; x < 7; x++) {
				final LocalDate date = this.currentDate.plusDays(y * 7L + x).minusDays(this.currentMonth.atDay(1).get(ChronoField.DAY_OF_WEEK) - 1);
				this.paint.setColor(0xFFFF0000);
				this.paint.setStyle(Paint.Style.STROKE);
				this.paint.setStrokeWidth(5.0f);
				canvas.drawRoundRect(cellWidth * x, cellHeight * y + offset, cellWidth * (x + 1), cellHeight * (y + 1) + offset, 10.0f, 10.0f, this.paint);
				this.paint.setStyle(Paint.Style.FILL);
				this.paint.setColor(0xFF00FF00);
				final String text = String.valueOf(date.getDayOfMonth());
				canvas.drawText(text, cellWidth * (((float) x) + 0.5f) - this.paint.measureText(text) / 2.0f, cellHeight * y + 10.0f + offset, this.paint);
			}
		}
	}

	static @NonNull View create(@NonNull final EventCalendarView view) {
		final ViewPager2 pager = new ViewPager2(view.context);
		pager.setAdapter(new Adapter(view));
		pager.setCurrentItem((int) ChronoUnit.MONTHS.between(CalendarView.EPOCH, YearMonth.now()));
		return pager;
	}

	private static class Adapter extends RecyclerView.Adapter<ViewHolder> {
		private final EventCalendarView view;

		private Adapter(@NonNull final EventCalendarView view) {
			this.view = view;
		}

		@Override
		public int getItemCount() {
			return Integer.MAX_VALUE;
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			return new ViewHolder(new CalendarView(view));
		}

		@Override
		public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
			holder.view.currentMonth = CalendarView.EPOCH.plusMonths(position);
			holder.view.currentDate = LocalDate.of(holder.view.currentMonth.getYear(), holder.view.currentMonth.getMonth(), 1);
			holder.view.rows = this.getCalendarRows(holder.view.currentMonth.getYear(), holder.view.currentMonth.getMonthValue());
		}

		public int getCalendarRows(int year, int month) {
			return (int) Math.ceil(((double) (Month.of(month).length(IsoChronology.INSTANCE.isLeapYear(year)) + LocalDate.of(year, month, 1).getDayOfWeek().getValue() % 7)) / 7.0d);
		}
	}

	private static class ViewHolder extends RecyclerView.ViewHolder {
		private final CalendarView view;

		private ViewHolder(@NonNull final CalendarView view) {
			super(view);
			this.view = view;
		}
	}
}
