package com.khopan.core.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class ContextedFragment extends Fragment {
	protected Context context;
	protected Resources resources;
	protected DisplayMetrics metrics;
	protected Resources.Theme theme;

	@Override
	public void onAttach(@NonNull final Context context) {
		super.onAttach(context);
		this.context = context;
		this.resources = this.context.getResources();
		this.metrics = this.resources.getDisplayMetrics();
		this.theme = this.context.getTheme();
	}
}
