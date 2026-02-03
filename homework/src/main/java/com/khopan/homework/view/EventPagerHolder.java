package com.khopan.homework.view;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.khopan.core.view.SimpleViewHolder;

public class EventPagerHolder {
	final ViewPager2 viewPager;

	RecyclerView recyclerView;

	private final EventCalendarView view;

	public EventPagerHolder(final EventCalendarView view) {
		this.view = view;
		this.viewPager = new ViewPager2(this.view.context);
		final RecyclerView.LayoutManager layoutManager = ((RecyclerView) this.viewPager.getChildAt(0)).getLayoutManager();
		this.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
			@Override
			public void onPageSelected(final int position) {
				final EventView view;
				EventPagerHolder.this.recyclerView = layoutManager == null ? null : (view = (EventView) layoutManager.findViewByPosition(position)) == null ? null : view.recyclerView;
			}
		});

		this.viewPager.setAdapter(new Adapter());
		this.viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
	}

	private class Adapter extends RecyclerView.Adapter<SimpleViewHolder<EventView>> {
		@Override
		public int getItemCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<EventView> holder, final int position) {

		}

		@NonNull
		@Override
		public SimpleViewHolder<EventView> onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			return new SimpleViewHolder<>(new EventView(EventPagerHolder.this.view.context));
		}
	}
}
