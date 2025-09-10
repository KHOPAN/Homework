package com.khopan.homework.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.khopan.homework.AbstractFragment;
import com.sec.sesl.khopan.homework.R;

public class HomeworkFragment extends AbstractFragment {
	public HomeworkFragment() {
		super(R.drawable.ic_oui_alarm, R.string.applicationName);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle bundle) {
		return new CalendarLayout(this.getContext());
	}

	private static class CalendarAdapter extends RecyclerView.Adapter<CalendarDayHolder> {
		@Override
		public int getItemCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public void onBindViewHolder(@NonNull final CalendarDayHolder holder, final int position) {

		}

		@NonNull
		@Override
		public CalendarDayHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			return new CalendarDayHolder(new View(parent.getContext()));
		}
	}

	private static class CalendarDayHolder extends RecyclerView.ViewHolder {
		public CalendarDayHolder(@NonNull final View view) {
			super(view);
			view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			view.setBackgroundColor(0xFFFF0000);
		}
	}

	private static class CalendarLayout extends ViewGroup {
		private final Context context;
		private final ViewPager2 calendarView;
		private final LinearLayout dayView;

		private int separatorY;

		private CalendarLayout(final Context context) {
			super(context);

			if(context == null) {
				throw new IllegalArgumentException("Argument 'context' cannot be null!");
			}

			this.context = context;
			this.calendarView = new ViewPager2(this.context);
			this.calendarView.setAdapter(new CalendarAdapter());
			this.addView(this.calendarView);
			this.dayView = new LinearLayout(this.context);
			this.dayView.setOrientation(LinearLayout.VERTICAL);
			this.dayView.setBackgroundColor(0xFF0000FF);
			this.addView(this.dayView);
			this.separatorY = 0;
		}

		@Override
		public boolean onInterceptTouchEvent(final MotionEvent event) {
			Log.i("Homework", "onInterceptTouchEvent(" + event.getX() + ", " + event.getY() + ")");
			//return super.onInterceptTouchEvent(event);
			return true;
		}

		@Override
		protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
			final int width = this.getWidth();
			this.calendarView.layout(0, 0, width, this.separatorY);
			this.dayView.layout(0, this.separatorY, width, this.getHeight());
		}

		@Override
		protected void onMeasure(final int measureWidth, final int measureHeight) {
			final int width = MeasureSpec.getSize(measureWidth);
			final int height = MeasureSpec.getSize(measureHeight);
			this.setMeasuredDimension(width, height);
			final int measuredWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
			this.calendarView.measure(measuredWidth, MeasureSpec.makeMeasureSpec(this.separatorY, MeasureSpec.EXACTLY));
			this.dayView.measure(measuredWidth, MeasureSpec.makeMeasureSpec(height - this.separatorY, MeasureSpec.EXACTLY));
		}

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouchEvent(final MotionEvent event) {
			Log.i("Homework", "onTouchEvent(" + event.getX() + ", " + event.getY() + ")");
			//return super.onTouchEvent(event);
			return true;
		}
	}
}
