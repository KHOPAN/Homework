package com.khopan.homework.view;

import androidx.viewpager2.widget.ViewPager2;

public class EventPagerHolder {
	final ViewPager2 viewPager;

	private final EventCalendarView view;

	public EventPagerHolder(final EventCalendarView view) {
		this.view = view;
		this.viewPager = new ViewPager2(this.view.context);
	}
}
