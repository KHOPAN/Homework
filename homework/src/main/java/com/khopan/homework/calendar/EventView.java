package com.khopan.homework.calendar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

class EventView {
	static @NonNull View create(@NonNull final Context context) {
		final ViewPager2 pager = new ViewPager2(context);
		pager.setAdapter(new EventView.Adapter(context));
		return pager;
	}

	private static class Adapter extends RecyclerView.Adapter<EventView.ViewHolder> {
		private final Context context;

		private Adapter(@NonNull final Context context) {
			this.context = context;
		}

		@NonNull
		@Override
		public EventView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			return null;
		}

		@Override
		public void onBindViewHolder(@NonNull EventView.ViewHolder holder, int position) {

		}

		@Override
		public int getItemCount() {
			return 0;
		}
	}

	private static class ViewHolder extends RecyclerView.ViewHolder {
		private ViewHolder(@NonNull final View view) {
			super(view);
		}
	}
}
