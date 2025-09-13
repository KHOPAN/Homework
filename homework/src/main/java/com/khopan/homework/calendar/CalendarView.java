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
	private static final LocalDate EPOCH = LocalDate.ofEpochDay(0L);

	private final Context context;
	private final Paint paint;

	private LocalDate currentDate;
	private int rows;

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
		final int height = this.getHeight();
		final float cellHeight = ((float) height) / ((float) this.rows);

		for(int i = 0; i < this.rows; i++) {
			this.renderRow();
		}
	}

	private void renderRow() {

	}

	static @NonNull View create(@NonNull final Context context) {
		final ViewPager2 pager = new ViewPager2(context);
		pager.setAdapter(new Adapter(context));
		pager.setCurrentItem((int) ChronoUnit.MONTHS.between(CalendarView.EPOCH, LocalDate.now()));
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
			holder.view.currentDate = CalendarView.EPOCH.plusMonths(position);
		}
	}

	private static class ViewHolder extends RecyclerView.ViewHolder {
		private final CalendarView view;

		private ViewHolder(@NonNull final CalendarView view) {
			super(view);
			this.view = view;
		}
	}
}
