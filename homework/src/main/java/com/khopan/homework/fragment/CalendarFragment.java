package com.khopan.homework.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.khopan.homework.view.EventCalendarView;

public class CalendarFragment extends Fragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle bundle) {
		final Context context = inflater.getContext();
		final EventCalendarView view = new EventCalendarView(context);
		final int padding = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f, context.getResources().getDisplayMetrics()));
		view.setPadding(padding, 0, padding, 0);
		return view;
	}
}
