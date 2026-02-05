package com.khopan.homework.view;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.khopan.homework.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;

public class CalendarPagerHolder {
	final ViewPager2 viewPager;

	private final EventCalendarView view;

	public CalendarPagerHolder(final EventCalendarView view) {
		this.view = view;
		this.viewPager = new ViewPager2(this.view.context);
		this.viewPager.setAdapter(new Adapter());
		this.viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
		this.viewPager.setCurrentItem((int) ChronoUnit.MONTHS.between(YearMonth.of(1970, 1), YearMonth.now()));
	}

	private class Adapter extends RecyclerView.Adapter<ViewHolder> {
		public int getRowsInMonth(final YearMonth yearMonth) {
			final LocalDate firstOfMonth = yearMonth.atDay(1);
			final int dayOfWeekValue = firstOfMonth.getDayOfWeek().getValue();
			final int offset = (dayOfWeekValue % 7);
			final int daysInMonth = yearMonth.lengthOfMonth();
			return (int) Math.ceil((double) (daysInMonth + offset) / 7);
		}

		@Override
		public int getItemViewType(int position) {
			return this.getRowsInMonth(YearMonth.of(1970, 1).plusMonths(position));
		}

		@Override
		public int getItemCount() {
			return (int) ChronoUnit.MONTHS.between(YearMonth.of(1970, 1), YearMonth.now()) + 1;
		}

		@Override
		public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
			final YearMonth month = YearMonth.of(1970, 1).plusMonths(position);
			holder.button.setText(month.format(new DateTimeFormatterBuilder().appendText(ChronoField.MONTH_OF_YEAR).appendLiteral(' ').appendText(ChronoField.YEAR).toFormatter()));
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			Log.d("EventCalendarView", "Count: " + this.getItemCount());
			final LinearLayout linearLayout = new LinearLayout(CalendarPagerHolder.this.view.context);
			linearLayout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			linearLayout.setOrientation(LinearLayout.VERTICAL);

			final Button button = new Button(CalendarPagerHolder.this.view.context);
			button.setBackgroundResource(dev.oneuiproject.oneui.design.R.drawable.oui_des_btn_transparent_bg);
			button.setClickable(true);
			button.setFocusable(true);
			button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26.0f);
			var x = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CalendarPagerHolder.this.view.headerSize);
			//x.gravity = Gravity.CENTER_HORIZONTAL;
			//x.bottomMargin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26.0f, CalendarPagerHolder.this.view.context.getResources().getDisplayMetrics()));
			linearLayout.addView(button, x);
			linearLayout.addView(new CalendarView(CalendarPagerHolder.this.view, viewType), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

			// 2.05625
			return new ViewHolder(linearLayout, button);
		}
	}

	private static class ViewHolder extends RecyclerView.ViewHolder {
		private final LinearLayout linearLayout;
		private final Button button;

		private ViewHolder(final LinearLayout linearLayout, final Button button) {
			super(linearLayout);
			this.linearLayout = linearLayout;
			this.button = button;
		}
	}
}
