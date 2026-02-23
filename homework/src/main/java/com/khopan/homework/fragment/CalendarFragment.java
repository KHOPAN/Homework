package com.khopan.homework.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.khopan.core.fragment.ContextedFragment;
import com.khopan.homework.calendar.EventCalendarView;

public class CalendarFragment extends ContextedFragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle bundle) {
		final EventCalendarView view = new EventCalendarView(this.context);
		final int padding = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f, this.metrics));
		view.setPadding(padding, 0, padding, 0);
		return view;
	}
}
