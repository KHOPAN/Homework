package com.khopan.homework.calendar;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

class EventView extends LinearLayout {
	private final Context context;

	private EventView(Context context) {
		super(context);
		this.context = context;
	}

	static @NonNull View create(@NonNull final Context context) {
		//final ViewPager2 pager = new ViewPager2(context);
		//pager.setAdapter(new EventView.Adapter(context));
		//return pager;
		return new EventView(context);
	}

	/*private static class Adapter extends RecyclerView.Adapter<EventView.ViewHolder> {
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
	}*/
}
