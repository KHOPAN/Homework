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
import java.time.temporal.ChronoUnit;

class CalendarView extends View {
	private final Context context;
	private final Paint paint;

	private CalendarView(@NonNull final Context context) {
		super(context);
		this.context = context;
		this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.paint = new Paint();
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		this.paint.setColor(0xFFFF0000);
		canvas.drawRect(0.0f, 0.0f, 100.0f, 100.0f, this.paint);
	}

	static @NonNull View create(@NonNull final Context context) {
		final ViewPager2 pager = new ViewPager2(context);
		pager.setAdapter(new Adapter(context));
		pager.setCurrentItem((int) ChronoUnit.MONTHS.between(LocalDate.ofEpochDay(0L), LocalDate.now()));
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
			//position - Integer.MAX_VALUE / 2;
			Log.i("Homework", "Position: " + position);
		}
	}

	private static class ViewHolder extends RecyclerView.ViewHolder {
		private ViewHolder(@NonNull final CalendarView view) {
			super(view);
		}
	}
}
