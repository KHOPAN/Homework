package com.khopan.homework.calendar;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.time.YearMonth;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class CalendarPagerHolder {
	final ViewPager2 viewPager;

	private final EventCalendarView view;

	public CalendarPagerHolder(final EventCalendarView view) {
		this.view = view;
		this.viewPager = new ViewPager2(this.view.context);
		this.viewPager.setAdapter(new Adapter());
		this.viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
		this.viewPager.setCurrentItem((int) ChronoUnit.MONTHS.between(EventCalendarView.EPOCH_MONTH, YearMonth.now()), false);
		((RecyclerView) this.viewPager.getChildAt(0)).seslSetRecoilEnabled(false);
	}

	private class Adapter extends RecyclerView.Adapter<ViewHolder> {
		@Override
		public int getItemViewType(int position) {
			final YearMonth month = EventCalendarView.EPOCH_MONTH.plusMonths(position);
			return (int) Math.ceil((month.lengthOfMonth() + month.atDay(1).getDayOfWeek().getValue() % 7) / 7.0d);
		}

		@Override
		public int getItemCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
			final YearMonth month = EventCalendarView.EPOCH_MONTH.plusMonths(position);
			holder.button.setText(month.format(new DateTimeFormatterBuilder().appendText(ChronoField.MONTH_OF_YEAR).appendLiteral(' ').appendText(ChronoField.YEAR).toFormatter()));
			holder.calendarView.setMonth(month);
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			final LinearLayout linearLayout = new LinearLayout(CalendarPagerHolder.this.view.context);
			linearLayout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			final FrameLayout frameLayout = new FrameLayout(CalendarPagerHolder.this.view.context);
			final Button button = new Button(CalendarPagerHolder.this.view.context);
			button.setBackgroundResource(dev.oneuiproject.oneui.design.R.drawable.oui_des_btn_transparent_bg);
			button.setClickable(true);
			button.setFocusable(true);
			button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26.0f);
			final FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			buttonParams.gravity = Gravity.CENTER_HORIZONTAL;
			frameLayout.addView(button, buttonParams);
			linearLayout.addView(frameLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CalendarPagerHolder.this.view.headerSize));
			final CalendarView calendarView = new CalendarView(CalendarPagerHolder.this.view, viewType);
			linearLayout.addView(calendarView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			return new ViewHolder(linearLayout, button, calendarView);
		}
	}

	private static class ViewHolder extends RecyclerView.ViewHolder {
		private final Button button;
		private final CalendarView calendarView;

		private ViewHolder(final LinearLayout linearLayout, final Button button, final CalendarView calendarView) {
			super(linearLayout);
			this.button = button;
			this.calendarView = calendarView;
		}
	}
}
