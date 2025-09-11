package com.khopan.homework.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import androidx.core.view.NestedScrollingParent3;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.khopan.homework.AbstractFragment;
import com.sec.sesl.khopan.homework.R;

import java.util.Random;

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
		private final CalendarLayout layout;

		private CalendarAdapter(final CalendarLayout layout) {
			this.layout = layout;
		}

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
			return new CalendarDayHolder(new CalendarDay(this.layout));
		}
	}

	private static class CalendarDay extends View {
		private final CalendarLayout layout;
		private final Paint paint;

		public CalendarDay(final CalendarLayout layout) {
			super(layout.context);
			this.layout = layout;
			this.paint = new Paint();
			this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		}

		@Override
		protected void onDraw(final Canvas canvas) {
			final int width = this.getWidth();
			final int height = this.getHeight();
			final double cellWidth = ((double) width) / 7.0d;
			final double cellHeight = Math.max(height, this.layout.separatorY) / 6.0d;

			for(int y = 0; y < 6; y++) {
				for(int x = 0; x < 7; x++) {
					this.renderDay(new Random(y * 6 + x), canvas, (int) Math.round(cellWidth * ((double) x)), (int) Math.round(cellHeight * ((double) y)), (int) Math.round(cellWidth * ((double) (x + 1))), (int) Math.round(cellHeight * ((double) (y + 1))));
				}
			}
		}

		private void renderDay(Random random, final Canvas canvas, final double left, final double top, final double right, final double bottom) {
			this.paint.setColor(random.nextInt(0xFFFFFF + 1) | 0xFF000000);
			this.paint.setStyle(Paint.Style.FILL);
			canvas.drawRect((float) left, (float) top, (float) right, (float) bottom, this.paint);
			this.paint.setColor(0xFF000000);
			this.paint.setTextSize(50.0f);
			canvas.drawText("Hello", (float) left, (float) top, this.paint);
			this.paint.setColor(0xFFFF0000);
			this.paint.setStyle(Paint.Style.STROKE);
			this.paint.setStrokeWidth(4.0f);
			canvas.drawRoundRect((float) left, (float) top, (float) right, (float) bottom, 20.0f, 20.0f, this.paint);
		}
	}

	private static class CalendarDayHolder extends RecyclerView.ViewHolder {
		public CalendarDayHolder(@NonNull final CalendarDay view) {
			super(view);
		}
	}

	private static class CalendarLayout extends ViewGroup {
		private final Context context;
		private final ViewPager2 calendarView;
		private final NestedScrollView dayView;
		//private final LinearLayout dayView;

		private float separatorY;
		private float pressedX;
		private float pressedY;

		private CalendarLayout(final Context context) {
			super(context);

			if(context == null) {
				throw new IllegalArgumentException("Argument 'context' cannot be null!");
			}

			this.context = context;
			this.calendarView = new ViewPager2(this.context);
			this.calendarView.setAdapter(new CalendarAdapter(this));
			this.addView(this.calendarView);
			this.dayView = new NestedScrollView(this.context);
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
			this.dayView.setNestedScrollingEnabled(true);
			this.addView(this.dayView);
			this.separatorY = 650.0f;
			this.pressedX = 0.0f;
			this.pressedY = 0.0f;
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
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			}

			Log.i("Homework", "onInterceptTouchEvent(" + this.action(event.getActionMasked()) + ", " + event.getX() + ", " + event.getY() + ")");
			return super.onInterceptTouchEvent(event);
			//return true;
		}

		@Override
		protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
			final int width = this.getWidth();
			final int separatorY = Math.round(this.separatorY);
			this.calendarView.layout(0, 0, width, separatorY);
			this.dayView.layout(0, separatorY, width, this.getHeight());
		}

		@Override
		protected void onMeasure(final int measureWidth, final int measureHeight) {
			final int width = MeasureSpec.getSize(measureWidth);
			final int height = MeasureSpec.getSize(measureHeight);
			this.setMeasuredDimension(width, height);
			final int measuredWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
			final int separatorY = Math.round(this.separatorY);
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

		/*@Override
		public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
			Log.i("Homework", "onNestedScroll()");
		}

		@Override
		public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
			Log.i("Homework", "onStartNestedScroll()");
			return true;
		}

		@Override
		public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
			Log.i("Homework", "onNestedScrollAccepted()");
		}

		@Override
		public void onStopNestedScroll(@NonNull View target, int type) {
			Log.i("Homework", "onStopNestedScroll()");
		}

		@Override
		public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
			Log.i("Homework", "onNestedScroll()");
		}

		@Override
		public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
			Log.i("Homework", "onNestedPreScroll()");
		}*/
	}
}
