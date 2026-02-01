package com.khopan.homework.view;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.khopan.core.view.SimpleViewHolder;

public class CalendarPagerHolder {
	final ViewPager2 viewPager;

	private final EventCalendarView view;

	public CalendarPagerHolder(final EventCalendarView view) {
		this.view = view;
		this.viewPager = new ViewPager2(this.view.context);
		this.viewPager.setAdapter(new Adapter());
		this.viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
	}

	private class Adapter extends RecyclerView.Adapter<SimpleViewHolder<CalendarView>> {
		@Override
		public int getItemCount() {
			return 13;
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<CalendarView> holder, final int position) {

		}

		@NonNull
		@Override
		public SimpleViewHolder<CalendarView> onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			final CalendarView calendarView = new CalendarView(CalendarPagerHolder.this.view, 5);
			calendarView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			return new SimpleViewHolder<>(calendarView);
		}
	}
}
