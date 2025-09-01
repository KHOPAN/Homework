package com.khopan.homework.calendar;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.viewpager2.widget.ViewPager2;

import java.util.Calendar;

public class CalendarView extends LinearLayout {
	private ViewPager2 monthPager;
	private Calendar startCalendar;

	public CalendarView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		monthPager = new ViewPager2(context);
		monthPager.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT
		));
		monthPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

		// Start from current month
		startCalendar = Calendar.getInstance();

		monthPager.setAdapter(new MonthAdapter(context, startCalendar));
		addView(monthPager);
	}
}
