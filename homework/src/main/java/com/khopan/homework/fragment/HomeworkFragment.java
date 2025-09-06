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
		private final OverScroller scroller;
		private final GestureDetector detector;
		private final View topView;
		private final View bottomView;

		private int divider;
		private VelocityTracker velocityTracker;

		private HomeworkLayout(@NonNull final Context context) {
			super(context);
			final ViewConfiguration configuration = ViewConfiguration.get(context);
			this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			this.context = context;
			this.scroller = new OverScroller(this.context);
			this.detector = new GestureDetector(this.context, new GestureDetector.SimpleOnGestureListener() {
				/*@Override
				public boolean onScroll(MotionEvent downEvent, MotionEvent moveEvent, float distanceX, float distanceY) {
					HomeworkLayout.this.divider -= Math.round(distanceY);
					HomeworkLayout.this.requestLayout();
					return true;
				}*/
			});

			this.topView = new MonthView(this.context);//new View(this.context);
			//this.topView.setBackgroundColor(0xFFFF0000);
			this.addView(this.topView);
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
		}

		private int lastY;
		private int lastDivider;
		private boolean dragging;

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if(this.velocityTracker == null) {
				this.velocityTracker = VelocityTracker.obtain();
			}

			switch(event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				if(!this.scroller.isFinished()) {
					this.scroller.abortAnimation();
				}

				this.lastY = Math.round(event.getY());
				this.lastDivider = this.divider;
				break;
			case MotionEvent.ACTION_MOVE:
				final int y = Math.round(event.getY());
				int deltaY = this.lastY - y;

				if(!this.dragging && Math.abs(deltaY) > ViewConfiguration.get(this.context).getScaledTouchSlop()) {
					this.dragging = true;
					deltaY += ViewConfiguration.get(this.context).getScaledTouchSlop() * (deltaY > 0 ? -1 : 1);
				}

				if(!this.dragging) {
					break;
				}

				this.divider = this.lastDivider - deltaY;
				this.requestLayout();
				break;
			case MotionEvent.ACTION_UP:
				if(!this.dragging || this.velocityTracker == null) {
					break;
				}

				final int height = this.getHeight();
				this.velocityTracker.computeCurrentVelocity(1000);

				if(this.divider + calculateY(Math.round(this.velocityTracker.getYVelocity())) < height / 2) {
					this.scroller.startScroll(0, this.divider, 0, -this.divider);
				} else {
					this.scroller.startScroll(0, this.divider, 0, height - this.divider);
				}

				//this.velocityTracker.computeCurrentVelocity(1000);
				//this.scroller.fling(0, this.divider, 0, Math.round(this.velocityTracker.getYVelocity()), 0, 0, 100, 10000);
				//final int change = 700 - this.divider;
				//Log.i("Test", "Velocity: " + this.velocityTracker.getYVelocity() + " Change: " + change);
				//Log.i("Homework", "Predicted: " + this.predictFinalY(this.divider, this.velocityTracker.getYVelocity()));
				//Log.i("Homework", "Better Prediction: " + (this.divider + this.calculateY(Math.round(this.velocityTracker.getYVelocity()))));
				//this.scroller.startScroll(0, this.divider, 0, change, (int) Math.round(((double) this.velocityTracker.getYVelocity()) / ((double) change)));
				this.postInvalidateOnAnimation();
				break;
			}

			if(this.velocityTracker != null) {
				this.velocityTracker.addMovement(event);
			}

			return true;
		}

		@Override
		public void computeScroll() {
			if(this.scroller.computeScrollOffset()) {
				this.divider = this.scroller.getCurrY();
				Log.i("Homework", "Actual: " + this.divider);
				this.requestLayout();
				this.invalidate();
			}
		}

		private int predictFinalY(int startY, float velocityY) {
			// Get default friction used by Scroller
			final float friction = ViewConfiguration.getScrollFriction();
			final float physicalCoeff = getContext().getResources().getDisplayMetrics().density
					* 160.0f * 39.37f * SensorManager.GRAVITY_EARTH;  // ~ 386.0878 * density

			final float deceleration = physicalCoeff * friction;

			// Distance = v^2 / (2 * a)
			float distance = (velocityY * velocityY) / (2 * deceleration);

			if (velocityY < 0) distance = -distance;

			return startY + Math.round(distance);
		}

		private int calculateY(final int velocity) {
			float DECELERATION_RATE = (float) (Math.log(0.78) / Math.log(0.9));
			float INFLEXION = 0.35f;
			final float ppi = context.getResources().getDisplayMetrics().density * 160.0f;
			float mPhysicalCoeff = SensorManager.GRAVITY_EARTH // g (m/s^2)
					* 39.37f // inch/meter
					* ppi
					* 0.84f;
			float mFlingFriction = ViewConfiguration.getScrollFriction();

			final double l = Math.log(INFLEXION * Math.abs(velocity) / (mFlingFriction * mPhysicalCoeff));
			final double decelMinusOne = DECELERATION_RATE - 1.0;
			return (int) Math.round(mFlingFriction * mPhysicalCoeff * Math.exp(DECELERATION_RATE / decelMinusOne * l) * Math.signum(velocity));
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
