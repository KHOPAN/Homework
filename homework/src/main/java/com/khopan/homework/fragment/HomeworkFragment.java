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

		/*final ViewPager2 pager = new ViewPager2(context);
		pager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		pager.setAdapter(new CalendarAdapter(context));
		linearLayout.addView(pager);*/

		return linearLayout;
	}

	private static class HomeworkLayout extends ViewGroup {
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
		private double lastY;
		private double lastDivider;

		private HomeworkLayout(@NonNull final Context context) {
			super(context);
			this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			this.scroller = new OverScroller(context);
			this.topView = new ViewPager2(context);
			//this.topView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			this.topView.setAdapter(new CalendarAdapter(context));
			//this.addView(this.topView = new MonthView(context));
			this.addView(this.topView);
			this.bottomView = new View(context);
			this.bottomView.setBackgroundColor(0xFF0000FF);
			this.addView(this.bottomView);
			this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
			this.divider = -1.0d;
			this.position = Position.SPLIT;
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
		protected void onMeasure(int widthMeasure, int heightMeasure) {
			this.setMeasuredDimension(MeasureSpec.getSize(widthMeasure), MeasureSpec.getSize(heightMeasure));
			this.topView.measure(widthMeasure, heightMeasure);
			this.bottomView.measure(widthMeasure, heightMeasure);
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
				this.lastY = event.getY();
				this.lastDivider = this.divider;
				this.activePointer = event.getPointerId(0);
				return true;
			case MotionEvent.ACTION_MOVE: {
				final int pointerIndex = event.findPointerIndex(this.activePointer);

				if(pointerIndex == -1) {
					break;
				}

				final double y = event.getY(pointerIndex);
				double deltaY = this.lastY - y;

				if(!this.dragging && Math.abs(deltaY) > this.touchSlop) {
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
				double deltaY = this.lastY - y;

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

		private enum Position {
			WEEK,
			SPLIT,
			MONTH
		}
	}

	private static class MonthView extends ViewGroup {
		public MonthView(@NonNull final Context context) {
			super(context);

			for(int i = 0; i < 6 * 7; i++) {
				final TextView view = new TextView(context);
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
			final double height = ((double) this.getHeight()) / 6.0d;

			for(int y = 0; y < 6; y++) {
				for(int x = 0; x < 7; x++) {
					this.getChildAt(y * 7 + x).layout((int) Math.round(width * ((double) x)), (int) Math.round(height * ((double) y)), (int) Math.round(width * ((double) (x + 1))), (int) Math.round(height * ((double) (y + 1))));
				}
			}
		}
	}

	private static class CalendarAdapter extends RecyclerView.Adapter<DayHolder> {
		private final Context context;

		private CalendarAdapter(@NonNull final Context context) {
			this.context = context;
		}

		@NonNull
		@Override
		public DayHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int type) {
			return new DayHolder(new MonthView(this.context));
		}

		@Override
		public void onBindViewHolder(@NonNull final DayHolder holder, final int position) {

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
}
