package com.khopan.homework.view;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.khopan.core.view.SimpleViewHolder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class EventPagerHolder {
	private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
			.appendText(ChronoField.DAY_OF_WEEK)
			.appendLiteral(' ')
			.appendText(ChronoField.DAY_OF_MONTH)
			.appendLiteral(' ')
			.appendText(ChronoField.MONTH_OF_YEAR)
			.appendLiteral(' ')
			.appendText(ChronoField.YEAR)
			.toFormatter();

	final ViewPager2 viewPager;

	RecyclerView recyclerView;

	private final EventCalendarView view;

	public EventPagerHolder(final EventCalendarView view) {
		this.view = view;
		this.viewPager = new ViewPager2(this.view.context);
		this.viewPager.setAdapter(new Adapter());
		this.viewPager.setCurrentItem((int) LocalDate.now().toEpochDay());
		this.viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
		final RecyclerView.LayoutManager layoutManager = ((RecyclerView) this.viewPager.getChildAt(0)).getLayoutManager();
		this.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
			@Override
			public void onPageSelected(final int position) {
				final EventView view;
				EventPagerHolder.this.recyclerView = layoutManager == null ? null : (view = (EventView) layoutManager.findViewByPosition(position)) == null ? null : view.recyclerView;
			}
		});
	}

	private class Adapter extends RecyclerView.Adapter<SimpleViewHolder<EventView>> {
		@Override
		public int getItemCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<EventView> holder, final int position) {
			holder.itemView.dayView.setText(EventPagerHolder.FORMATTER.format(LocalDate.ofEpochDay(position)));
		}

		@NonNull
		@Override
		public SimpleViewHolder<EventView> onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			return new SimpleViewHolder<>(new EventView(EventPagerHolder.this.view.context));
		}
	}
}
