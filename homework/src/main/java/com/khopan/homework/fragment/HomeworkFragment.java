package com.khopan.homework.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.khopan.homework.AbstractFragment;
import com.khopan.homework.HomeworkApplication;
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

	private static class CalendarLayout extends ViewGroup {
		private final Context context;

		private CalendarLayout(final Context context) {
			super(context);

			if(context == null) {
				throw new IllegalArgumentException("Argument 'context' cannot be null!");
			}

			this.context = context;
		}

		@Override
		protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
			Log.i("Homework", String.format("onLayout(%s: boolean, %d: int, %d: int, %d: int, %d: int)", changed ? "true" : "false", left, top, right, bottom));
		}

		@Override
		protected void onMeasure(int measureWidth, int measureHeight) {
			super.onMeasure(measureWidth, measureHeight);
			Log.i("Homework", String.format("onMeasure(%d: int, %d: height)", MeasureSpec.getSize(measureWidth), MeasureSpec.getSize(measureHeight)));
		}
	}
}
