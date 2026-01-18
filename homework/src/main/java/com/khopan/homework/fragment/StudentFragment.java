package com.khopan.homework.fragment;

import android.view.View;

import com.khopan.core.fragment.FunctionalFragment;
import com.khopan.homework.view.EventCalendarView;

public class StudentFragment extends FunctionalFragment {
	@Override
	protected View initialize() {
		return new EventCalendarView(this.context);
	}
}
