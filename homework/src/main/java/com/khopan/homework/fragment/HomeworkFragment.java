package com.khopan.homework.fragment;

import static androidx.customview.widget.ViewDragHelper.INVALID_POINTER;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;

import com.khopan.homework.AbstractFragment;
import com.sec.sesl.khopan.homework.R;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
		private final double coefficient;
		private final double deacceleration;
		private final OverScroller scroller;
		private final View topView;
		private final View bottomView;
		private final int touchSlop;
		private int minimum;
		private int maximum;
		private int middle;

		private int divider;
		private VelocityTracker velocityTracker;

		private HomeworkLayout(@NonNull final Context context) {
			super(context);
			final ViewConfiguration configuration = ViewConfiguration.get(context);
			this.touchSlop = configuration.getScaledTouchSlop();
			this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			this.context = context;
			this.coefficient = ViewConfiguration.getScrollFriction() * this.context.getResources().getDisplayMetrics().density * SensorManager.GRAVITY_EARTH * 5291.328d;
			this.deacceleration = Math.log(0.78d) / Math.log(0.9d) / (Math.log(0.78d) / Math.log(0.9d) - 1.0d);
			this.scroller = new OverScroller(this.context);
			this.addView(this.topView = new MonthView(this.context));
			this.bottomView = new View(this.context);
			this.bottomView.setBackgroundColor(0xFF0000FF);
			this.addView(this.bottomView);
			this.divider = 300;
		}

		@Override
		protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
			final int width = this.getWidth();
			final int height = this.getHeight();
			this.topView.layout(0, 0, width, this.divider);
			this.bottomView.layout(0, this.divider, width, height);
			this.minimum = 100;
			this.maximum = height;
			this.middle = height / 2;
		}

		private int lastY;
		private int lastDivider;
		private boolean dragging;
		private int activePointer;

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if(this.velocityTracker == null) {
				this.velocityTracker = VelocityTracker.obtain();
			}

			switch(event.getActionMasked()) {
			case MotionEvent.ACTION_CANCEL:
				if(this.dragging) {
					this.activePointer = MotionEvent.INVALID_POINTER_ID;
					this.dragging = false;
					this.velocityTracker.recycle();
					this.velocityTracker = null;
				}

				break;
			case MotionEvent.ACTION_DOWN:
				if(!this.scroller.isFinished()) {
					this.scroller.abortAnimation();
				}

				this.activePointer = event.getPointerId(0);
				this.lastDivider = this.divider;
				this.lastY = Math.round(event.getY());
				break;
			case MotionEvent.ACTION_MOVE: {
				final int activePointerIndex = event.findPointerIndex(this.activePointer);

				if(activePointerIndex == -1) {
					break;
				}

				final int y = Math.round(event.getY(activePointerIndex));
				int deltaY = this.lastY - y;

				if(!this.dragging && Math.abs(deltaY) > this.touchSlop) {
					this.dragging = true;
					deltaY -= Math.round(ViewConfiguration.get(this.context).getScaledTouchSlop() * Math.signum(deltaY));
				}

				if(this.dragging) {
					this.divider = Math.min(Math.max(this.lastDivider - deltaY, this.minimum), this.maximum);
					this.requestLayout();
				}

				break;
			}
			case MotionEvent.ACTION_POINTER_DOWN:
				this.activePointer = event.getPointerId(event.getActionIndex());
				break;
			case MotionEvent.ACTION_POINTER_UP: {
				final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
				final int pointer = event.getPointerId(pointerIndex);

				if(this.activePointer != pointer) {
					break;
				}

				this.activePointer = event.getPointerId(pointerIndex == 0 ? 1 : 0);

				if(this.velocityTracker != null) {
					this.velocityTracker.clear();
				}

				break;
			}
			case MotionEvent.ACTION_UP: {
				if(!this.dragging || this.velocityTracker == null) {
					break;
				}

				this.velocityTracker.computeCurrentVelocity(1000);
				final double velocity = this.velocityTracker.getYVelocity(this.activePointer) / 10.0d;
				final double finalDivider = Math.exp(Math.log(Math.abs(velocity) / this.coefficient * 0.35d) * this.deacceleration) * this.coefficient * Math.signum(velocity) + ((double) this.divider);

				final double distanceMinimum = Math.abs(this.minimum - finalDivider);
				final double distanceMiddle = Math.abs(this.middle - finalDivider);
				final double distanceMaximum = Math.abs(this.maximum - finalDivider);
				final double nearest = Math.min(Math.min(distanceMinimum, distanceMiddle), distanceMaximum);

				if(nearest == distanceMinimum) {
					this.scroller.startScroll(0, this.divider, 0, this.minimum - this.divider);
				} else if(nearest == distanceMiddle) {
					this.scroller.startScroll(0, this.divider, 0, this.middle - this.divider);
				} else if(nearest == distanceMaximum) {
					this.scroller.startScroll(0, this.divider, 0, this.maximum - this.divider);
				}

				this.activePointer = MotionEvent.INVALID_POINTER_ID;
				this.dragging = false;
				this.velocityTracker.recycle();
				this.velocityTracker = null;
				this.postInvalidateOnAnimation();
				break;
			}
			}

			if(this.velocityTracker != null) {
				this.velocityTracker.addMovement(event);
			}

			return true;
		}

		@Override
		public void computeScroll() {
			if(this.scroller.computeScrollOffset()) {
				this.divider = Math.min(Math.max(this.scroller.getCurrY(), this.minimum), this.maximum);
				this.requestLayout();
				this.invalidate();
			}
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
			final double width = ((double) this.getWidth()) / 7.0d;
			final double height = ((double) this.getHeight()) / 6.0d;

			for(int i = 0; i < this.getChildCount(); i++) {
				final View view = this.getChildAt(i);
				int x = i % 7;
				int y = i / 7;
				view.layout((int) Math.round(width * ((double) x)), (int) Math.round(height * ((double) y)), (int) Math.round(width * ((double) (x + 1))), (int) Math.round(height * ((double) (y + 1))));
			}
		}
	}
}
