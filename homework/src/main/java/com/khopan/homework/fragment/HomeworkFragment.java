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
import android.widget.ScrollView;
import android.widget.TextView;

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
		private final ScrollView dayView;
		//private final LinearLayout dayView;

		private double separatorY;
		private double pressedX;
		private double pressedY;

		private CalendarLayout(final Context context) {
			super(context);

			if(context == null) {
				throw new IllegalArgumentException("Argument 'context' cannot be null!");
			}

			this.context = context;
			this.calendarView = new ViewPager2(this.context);
			this.calendarView.setAdapter(new CalendarAdapter());
			this.addView(this.calendarView);
			this.dayView = new ScrollView(this.context);
			final LinearLayout linearLayout = new LinearLayout(this.context);
			linearLayout.setOrientation(LinearLayout.VERTICAL);

			for(int i = 0; i < 100; i++) {
				final TextView view = new TextView(this.context);
				view.setText("" + i);
				linearLayout.addView(view, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			}

			this.dayView.addView(linearLayout, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			/*this.dayView = new LinearLayout(this.context);
			this.dayView.setOrientation(LinearLayout.VERTICAL);
			this.dayView.setBackgroundColor(0xFF0000FF);*/
			this.addView(this.dayView);
			this.separatorY = 0.0d;
			this.pressedX = 0;
			this.pressedY = 0;
		}

		private String action(final int action) {
			switch(action) {
			case MotionEvent.ACTION_DOWN: return "ACTION_DOWN";
			case MotionEvent.ACTION_UP: return "ACTION_UP";
			case MotionEvent.ACTION_MOVE: return "ACTION_MOVE";
			case MotionEvent.ACTION_CANCEL: return "ACTION_CANCEL";
			case MotionEvent.ACTION_OUTSIDE: return "ACTION_OUTSIDE";
			case MotionEvent.ACTION_POINTER_DOWN: return "ACTION_POINTER_DOWN";
			case MotionEvent.ACTION_POINTER_UP: return "ACTION_POINTER_UP";
			case MotionEvent.ACTION_HOVER_MOVE: return "ACTION_HOVER_MOVE";
			case MotionEvent.ACTION_POINTER_INDEX_SHIFT: return "ACTION_POINTER_INDEX_SHIFT";
			case MotionEvent.ACTION_HOVER_ENTER: return "ACTION_HOVER_ENTER";
			case MotionEvent.ACTION_HOVER_EXIT: return "ACTION_HOVER_EXIT";
			case MotionEvent.ACTION_BUTTON_PRESS: return "ACTION_BUTTON_PRESS";
			case MotionEvent.ACTION_BUTTON_RELEASE: return "ACTION_BUTTON_RELEASE";
			default: return null;
			}
		}

		@Override
		public boolean onInterceptTouchEvent(final MotionEvent event) {
			switch(event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				this.pressedX = event.getX();
				this.pressedY = event.getY();
				return false;
			}

			Log.i("Homework", "onInterceptTouchEvent(" + this.action(event.getActionMasked()) + ", " + event.getX() + ", " + event.getY() + ")");
			//return super.onInterceptTouchEvent(event);
			return true;
		}

		@Override
		protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
			final int width = this.getWidth();
			final int separatorY = (int) Math.round(this.separatorY);
			this.calendarView.layout(0, 0, width, separatorY);
			this.dayView.layout(0, separatorY, width, this.getHeight());
		}

		@Override
		protected void onMeasure(final int measureWidth, final int measureHeight) {
			final int width = MeasureSpec.getSize(measureWidth);
			final int height = MeasureSpec.getSize(measureHeight);
			this.setMeasuredDimension(width, height);
			final int measuredWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
			final int separatorY = (int) Math.round(this.separatorY);
			this.calendarView.measure(measuredWidth, MeasureSpec.makeMeasureSpec(separatorY, MeasureSpec.EXACTLY));
			this.dayView.measure(measuredWidth, MeasureSpec.makeMeasureSpec(height - separatorY, MeasureSpec.EXACTLY));
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
