package com.khopan.homework.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
		private final GestureDetector detector;
		private final View topView;
		private final View bottomView;

		private int divider;

		private HomeworkLayout(@NonNull final Context context) {
			super(context);
			this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			this.context = context;
			this.detector = new GestureDetector(this.context, new GestureDetector.SimpleOnGestureListener() {
				@Override
				public boolean onScroll(MotionEvent downEvent, MotionEvent moveEvent, float distanceX, float distanceY) {
					HomeworkLayout.this.divider -= Math.round(distanceY);
					HomeworkLayout.this.requestLayout();
					return true;
				}
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

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			this.detector.onTouchEvent(event);
			return true;

			/*if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
				this.divider = Math.round(event.getY());
				this.requestLayout();
				return true;
			}*/

			//return super.onTouchEvent(event);
		}
	}

	private static class MonthView extends ViewGroup {
		private final Context context;

		public MonthView(@NonNull final Context context) {
			super(context);
			this.context = context;

			for(int i = 0; i < 6 * 7; i++) {
				final TextView view = new TextView(this.context);
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
