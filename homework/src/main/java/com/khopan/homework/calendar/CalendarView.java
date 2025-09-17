package com.khopan.homework.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
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
import java.util.Random;

class CalendarView extends View {
	private static final YearMonth EPOCH = YearMonth.from(LocalDate.ofEpochDay(0L));

	private final Context context;
	private final Paint paint;

	private YearMonth currentMonth;
	private LocalDate currentDate;
	private int rows;

	private CalendarView(@NonNull final Context context) {
		super(context);
		this.context = context;
		this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.paint = new Paint();
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		final int width = this.getWidth();
		final float cellWidth = ((float) width) / 7.0f;
		final float cellHeight = ((float) this.getHeight()) / ((float) this.rows);

		for(int y = 0; y < this.rows; y++) {
			for(int x = 0; x < 7; x++) {
				final LocalDate date = this.currentDate.plusDays(y * 7L + x);
				this.paint.setColor(0xFFFF0000);
				this.paint.setStyle(Paint.Style.STROKE);
				this.paint.setStrokeWidth(5.0f);
				canvas.drawRoundRect(cellWidth * x, cellHeight * y, cellWidth * (x + 1), cellHeight * (y + 1), 10.0f, 10.0f, this.paint);
				this.paint.setStyle(Paint.Style.FILL);
				this.paint.setColor(0xFF00FF00);
				final String text = String.valueOf(date.getDayOfMonth());
				canvas.drawText(text, cellWidth * (((float) x) + 0.5f) - this.paint.measureText(text) / 2.0f, cellHeight * y + 10.0f, this.paint);
			}
		}
	}

	static @NonNull View create(@NonNull final Context context) {
		final ViewPager2 pager = new ViewPager2(context);
		pager.setAdapter(new Adapter(context));
		pager.setCurrentItem((int) ChronoUnit.MONTHS.between(CalendarView.EPOCH, YearMonth.now()));
		return pager;
	}

	private static class Adapter extends RecyclerView.Adapter<ViewHolder> {
		private final Context context;

		private Adapter(@NonNull final Context context) {
			this.context = context;
		}

		@Override
		public int getItemCount() {
			return Integer.MAX_VALUE;
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			return new ViewHolder(new CalendarView(this.context));
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
