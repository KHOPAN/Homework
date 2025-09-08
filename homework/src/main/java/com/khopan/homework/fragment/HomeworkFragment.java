package com.khopan.homework.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
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
import java.time.Month;
import java.time.YearMonth;
import java.time.chrono.IsoChronology;
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
			//this.bottomView = new View(this.context);
			//this.bottomView.setBackgroundColor(0xFF0000FF);
			this.bottomView = new TextView(this.context);
			((TextView) this.bottomView).setText("EVENTS GOES HERE!");
			((TextView) this.bottomView).setGravity(Gravity.CENTER);
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
			private final int rows;

			public MonthView(final int rows) {
				super(HomeworkLayout.this.context);
				this.rows = rows;

				for(int i = 0; i < this.rows * 7; i++) {
					/*final TextView view = new TextView(HomeworkLayout.this.context);
					view.setText(String.format(Locale.getDefault(), "%d", i));
					view.setTextAlignment(TextView.TET_ALIGNMENT_CENTER);
					this.addView(view);*/
					final LinearLayout layout = new LinearLayout(HomeworkLayout.this.context);
					layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
					layout.setOrientation(LinearLayout.VERTICAL);
					final TextView view = new TextView(HomeworkLayout.this.context);
					view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					view.setText(String.format(Locale.getDefault(), "%d", i));
					view.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
					layout.addView(view);
					final View test = new View(HomeworkLayout.this.context);
					test.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
					test.setBackgroundColor(0xFF00FF00);
					layout.addView(test);
					//layout.setBackgroundColor(0xFF00FF00);
					this.addView(layout);
				}
			}

			@Override
			protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
				final double width = ((double) this.getWidth()) / 7.0d;
				final double height = Math.max(this.getHeight(), HomeworkLayout.this.positionSplit) / ((double) this.rows);
				final double factor = (1.0d - (this.getHeight() - HomeworkLayout.this.positionWeek) / (HomeworkLayout.this.positionSplit - HomeworkLayout.this.positionWeek));
				final int offset = this.getHeight() < HomeworkLayout.this.positionSplit ? (int) Math.round(-height * factor) : 0;

				for(int y = 0; y < this.rows; y++) {
					for(int x = 0; x < 7; x++) {
						this.getChildAt(y * 7 + x).layout((int) Math.round(width * ((double) x)), (int) Math.round(height * ((double) y)) + offset, (int) Math.round(width * ((double) (x + 1))), (int) Math.round(height * ((double) (y + 1))) + offset);
						this.getChildAt(y * 7 + x).requestLayout();
					}
				}
			}

			@Override
			protected void onMeasure(int widthMeasure, int heightMeasure) {
				final int widths = MeasureSpec.getSize(widthMeasure);
				final int heights = MeasureSpec.getSize(heightMeasure);
				this.setMeasuredDimension(widths, heights);
				final double width = ((double) widths) / 7.0d;
				final double height = Math.max(heights, HomeworkLayout.this.positionSplit) / ((double) this.rows);

				for(int y = 0; y < this.rows; y++) {
					for(int x = 0; x < 7; x++) {
						this.getChildAt(y * 7 + x).measure(MeasureSpec.makeMeasureSpec((int) width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY));
						this.getChildAt(y * 7 + x).requestLayout();
					}
				}
			}
		}

		private class CalendarAdapter extends RecyclerView.Adapter<DayHolder> {
			private static final int INITIAL_POSITION = Integer.MAX_VALUE / 2;

			@NonNull
			@Override
			public DayHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int type) {
				return new DayHolder(new MonthView(type));
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
				//return this.hasSixRows(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1) ? 1 : 0;
				return  this.getCalendarRows(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
			}

			public int getCalendarRows(int year, int month) {
				return (int) Math.ceil(((double) (Month.of(month).length(IsoChronology.INSTANCE.isLeapYear(year)) + LocalDate.of(year, month, 1).getDayOfWeek().getValue() % 7)) / 7.0d);
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
