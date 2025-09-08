package com.khopan.homework.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.khopan.homework.AbstractFragment;
import com.sec.sesl.khopan.homework.R;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

public class HomeworkFragment extends AbstractFragment {
	public HomeworkFragment() {
		super(R.drawable.ic_oui_alarm, R.string.applicationName);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle bundle) {
		final Context context = this.getContext();

		if(context == null) {
			return null;
		}

		final LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		final TextView textView = new TextView(context);
		textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		textView.setText("Title - Stub");
		linearLayout.addView(textView);
		linearLayout.addView(new HomeworkLayout(context));
		return linearLayout;
	}

	private static class HomeworkLayout extends ViewGroup {
		private final Context context;
		private final OverScroller scroller;
		private final ViewPager2 topView;
		private final View bottomView;
		private final double touchSlop;

		private double divider;
		private Position position;
		private double positionWeek;
		private double positionSplit;
		private double positionMonth;
		private boolean dragging;
		private int activePointer;
		private double lastX;
		private double lastY;
		private double lastDivider;

		private HomeworkLayout(@NonNull final Context context) {
			super(context);
			this.context = context;
			this.scroller = new OverScroller(this.context);
			this.topView = new ViewPager2(this.context);
			this.topView.setAdapter(new CalendarAdapter());
			this.topView.setCurrentItem(Integer.MAX_VALUE / 2, false);
			this.addView(this.topView);
			this.bottomView = new View(this.context);
			this.bottomView.setBackgroundColor(0xFF0000FF);
			this.addView(this.bottomView);
			this.touchSlop = ViewConfiguration.get(this.context).getScaledTouchSlop();
			this.divider = -1.0d;
			this.position = Position.SPLIT;
			this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}

		@Override
		public void computeScroll() {
			if(this.scroller.computeScrollOffset()) {
				this.divider = Math.min(Math.max(this.scroller.getCurrY(), this.positionWeek), this.positionMonth);
				this.requestLayout();
				this.invalidate();
			}
		}

		@Override
		public boolean onInterceptTouchEvent(MotionEvent event) {
			switch(event.getActionMasked()) {
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				if(this.dragging) {
					this.activePointer = MotionEvent.INVALID_POINTER_ID;
					this.dragging = false;
				}

				return false;
			case MotionEvent.ACTION_DOWN:
				this.lastX = event.getX();
				this.lastY = event.getY();
				this.lastDivider = this.divider;
				this.activePointer = event.getPointerId(0);
				this.dragging = false;
				return false;
			case MotionEvent.ACTION_MOVE: {
				final int pointerIndex = event.findPointerIndex(this.activePointer);

				if(pointerIndex == -1) {
					break;
				}

				double deltaY = this.lastY - ((double) event.getY(pointerIndex));

				if(!this.dragging && Math.abs(deltaY) > Math.abs(this.lastX - ((double) event.getX())) && Math.abs(deltaY) > this.touchSlop) {
					this.dragging = true;
					deltaY -= Math.signum(deltaY) * this.touchSlop;
				}

				if(this.dragging) {
					this.divider = this.lastDivider - deltaY;
					this.requestLayout();
				}

				return this.dragging;
			}
			}

			return super.onInterceptTouchEvent(event);
		}

		@Override
		protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
			final int width = this.getWidth();
			final int height = this.getHeight();
			this.positionWeek = ((double) height) / 10.0d;
			this.positionSplit = ((double) height) / 2.0d;
			this.positionMonth = height;

			if(this.divider < 0.0d) {
				this.divider = Position.WEEK.equals(this.position) ? this.positionWeek : Position.MONTH.equals(this.position) ? this.positionMonth : this.positionSplit;
			}

			final int divider = (int) Math.round(this.divider = Math.min(Math.max(this.divider, this.positionWeek), this.positionMonth));
			this.topView.layout(0, 0, width, divider);
			this.bottomView.layout(0, divider, width, height);
		}

		@Override
		protected void onMeasure(final int widthMeasure, final int heightMeasure) {
			final int width = MeasureSpec.getSize(widthMeasure);
			final int height = MeasureSpec.getSize(heightMeasure);
			this.setMeasuredDimension(width, height);
			this.positionWeek = ((double) height) / 10.0d;
			this.positionSplit = ((double) height) / 2.0d;
			this.positionMonth = height;

			if(this.divider < 0.0d) {
				this.divider = Position.WEEK.equals(this.position) ? this.positionWeek : Position.MONTH.equals(this.position) ? this.positionMonth : this.positionSplit;
			}

			final int divider = (int) Math.round(this.divider = Math.min(Math.max(this.divider, this.positionWeek), this.positionMonth));
			this.topView.measure(widthMeasure, MeasureSpec.makeMeasureSpec(divider, MeasureSpec.EXACTLY));
			this.bottomView.measure(widthMeasure, MeasureSpec.makeMeasureSpec(height - divider, MeasureSpec.EXACTLY));
		}

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			switch(event.getActionMasked()) {
			case MotionEvent.ACTION_CANCEL:
				if(this.dragging) {
					this.activePointer = MotionEvent.INVALID_POINTER_ID;
					this.dragging = false;
				}

				return true;
			case MotionEvent.ACTION_DOWN:
				this.lastX = event.getX();
				this.lastY = event.getY();
				this.lastDivider = this.divider;
				this.activePointer = event.getPointerId(0);
				this.dragging = false;
				return true;
			case MotionEvent.ACTION_MOVE: {
				final int pointerIndex = event.findPointerIndex(this.activePointer);

				if(pointerIndex == -1) {
					break;
				}

				double deltaY = this.lastY - ((double) event.getY(pointerIndex));

				if(!this.dragging && Math.abs(deltaY) > Math.abs(this.lastX - ((double) event.getX())) && Math.abs(deltaY) > this.touchSlop) {
					this.dragging = true;
					deltaY -= Math.signum(deltaY) * this.touchSlop;
				}

				if(this.dragging) {
					this.divider = this.lastDivider - deltaY;
					this.requestLayout();
				}

				return true;
			}
			case MotionEvent.ACTION_POINTER_DOWN:
				this.activePointer = event.getPointerId(event.getActionIndex());
				return true;
			case MotionEvent.ACTION_POINTER_UP: {
				final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

				if(this.activePointer == event.getPointerId(pointerIndex)) {
					this.activePointer = event.getPointerId(pointerIndex == 0 ? 1 : 0);
				}

				return true;
			}
			case MotionEvent.ACTION_UP: {
				if(!this.dragging) {
					break;
				}

				final int pointerIndex = event.findPointerIndex(this.activePointer);

				if(pointerIndex == -1) {
					break;
				}

				final double y = event.getY(pointerIndex);
				final double deltaY = this.lastY - y;

				switch(this.position) {
				case WEEK:
					this.position = deltaY > 0 ? Position.WEEK : Position.SPLIT;
					break;
				case MONTH:
					this.position = deltaY > 0 ? Position.SPLIT : Position.MONTH;
					break;
				default:
					this.position = deltaY > 0 ? Position.WEEK : Position.MONTH;
					break;
				}

				this.scroller.startScroll(0, (int) Math.round(this.divider), 0, (int) Math.round((Position.WEEK.equals(this.position) ? this.positionWeek : Position.MONTH.equals(this.position) ? this.positionMonth : this.positionSplit) - this.divider));
				this.activePointer = MotionEvent.INVALID_POINTER_ID;
				this.dragging = false;
				this.postInvalidateOnAnimation();
				return true;
			}
			}

			return super.onTouchEvent(event);
		}

		private class MonthView extends ViewGroup {
			private final boolean sixRows;

			public MonthView(final boolean sixRows) {
				super(HomeworkLayout.this.context);
				this.sixRows = sixRows;

				for(int i = 0; i < (this.sixRows ? 6 : 5) * 7; i++) {
					final TextView view = new TextView(HomeworkLayout.this.context);
					view.setText(String.format(Locale.getDefault(), "%d", i));
					view.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
					this.addView(view);
				}
			}

			@Override
			protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
			/*// 2 -> 0
			// 10 -> height
			final double width = ((double) this.getWidth()) / 7.0d;
			//final double height = ((double) this.getHeight()) / 6.0d;
			final double height = ((double) ((ViewGroup) this.getParent()).getHeight()) / 10.0d;
			final int translationX = (int) Math.round((((ViewGroup) this.getParent()).getHeight() / (double) this.getHeight() - 2.0d) / 8.0d * -height);

			for(int i = 0; i < this.getChildCount(); i++) {
				final View view = this.getChildAt(i);
				int x = i % 7;
				int y = i / 7;
				view.layout((int) Math.round(width * ((double) x)), (int) Math.round(height * ((double) y)) + translationX, (int) Math.round(width * ((double) (x + 1))), (int) Math.round(height * ((double) (y + 1))) + translationX);
			}*/

				final double width = ((double) this.getWidth()) / 7.0d;
				final double height = Math.max(this.getHeight(), HomeworkLayout.this.positionSplit) / (this.sixRows ? 6.0d : 5.0d);
				final double factor = (1.0d - (this.getHeight() - HomeworkLayout.this.positionWeek) / (HomeworkLayout.this.positionSplit - HomeworkLayout.this.positionWeek));
				final int offset;

				if(this.getHeight() < HomeworkLayout.this.positionSplit) {
					offset = -(int) Math.round(height * factor * 1.0d);
				} else {
					offset = 0;
				}

				for(int y = 0; y < (this.sixRows ? 6 : 5); y++) {
					for(int x = 0; x < 7; x++) {
						this.getChildAt(y * 7 + x).layout((int) Math.round(width * ((double) x)), (int) Math.round(height * ((double) y)) + offset, (int) Math.round(width * ((double) (x + 1))), (int) Math.round(height * ((double) (y + 1))) + offset);
					}
				}
			}
		}

		private class CalendarAdapter extends RecyclerView.Adapter<DayHolder> {
			private static final int INITIAL_POSITION = Integer.MAX_VALUE / 2;

			@NonNull
			@Override
			public DayHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int type) {
				return new DayHolder(new MonthView(type == 1));
			}

			@Override
			public void onBindViewHolder(@NonNull final DayHolder holder, final int position) {
				final int offsetFromCenter = position - INITIAL_POSITION;
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
				cal.add(java.util.Calendar.MONTH, offsetFromCenter);
				int year = cal.get(java.util.Calendar.YEAR);
				int month = cal.get(java.util.Calendar.MONTH); // 0-based
				//((MonthView) holder.itemView).setMonth(year, month);
			}

			@Override
			public int getItemViewType(int position) {
				final Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.add(Calendar.MONTH, position - CalendarAdapter.INITIAL_POSITION);
				return this.hasSixRows(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1) ? 1 : 0;
			}

			/**
			 * Determines if a given month and year will occupy 6 rows on a calendar.
			 * Assumes the calendar week starts on Sunday.
			 *
			 * @param year The year.
			 * @param month The month (1-12).
			 * @return true if the month occupies 6 rows, false otherwise.
			 */
			public boolean hasSixRows(int year, int month) {
				LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
				int daysInMonth = firstDayOfMonth.lengthOfMonth();
				DayOfWeek startDay = firstDayOfMonth.getDayOfWeek();

				// Adjust for a Sunday-based calendar (Sunday=1, Monday=2, etc.)
				int startDayValue = startDay.getValue() % 7 + 1;

				if (daysInMonth == 31 && (startDayValue == 6 || startDayValue == 7)) {
					// A 31-day month starting on Friday or Saturday
					return true;
				} else if (daysInMonth == 30 && startDayValue == 7) {
					// A 30-day month starting on Saturday
					return true;
				}

				return false;
			}

			@Override
			public int getItemCount() {
				return Integer.MAX_VALUE;
			}
		}

		private static class DayHolder extends RecyclerView.ViewHolder {
			public DayHolder(@NonNull final View view) {
				super(view);
				view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			}
		}

		private enum Position {
			WEEK,
			SPLIT,
			MONTH
		}
	}
}
