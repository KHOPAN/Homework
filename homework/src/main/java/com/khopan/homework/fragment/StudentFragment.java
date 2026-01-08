package com.khopan.homework.fragment;

import android.view.View;
import android.widget.LinearLayout;

import com.khopan.core.fragment.FunctionalFragment;

public class StudentFragment extends FunctionalFragment {
	@Override
	protected View initialize() {
		return new LinearLayout(this.context);
	}
}
