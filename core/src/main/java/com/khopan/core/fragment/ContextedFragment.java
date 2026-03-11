package com.khopan.core.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A {@link androidx.fragment.app.Fragment} with
 * prefetched {@link android.content.Context},
 * {@link android.content.res.Resources},
 * {@link android.util.DisplayMetrics}, and
 * {@link android.content.res.Resources.Theme}.
 */
public abstract class ContextedFragment extends Fragment {
	/**
	 * The {@link android.content.Context}.
	 */
	protected Context context;

	/**
	 * The {@link android.content.res.Resources}.
	 */
	protected Resources resources;

	/**
	 * The {@link android.util.DisplayMetrics}.
	 */
	protected DisplayMetrics metrics;

	/**
	 * {@link android.content.res.Resources.Theme}.
	 */
	protected Resources.Theme theme;

	@Override
	public void onAttach(@NonNull final Context context) {
		this.context = context;
		this.resources = this.context.getResources();
		this.metrics = this.resources.getDisplayMetrics();
		this.theme = this.context.getTheme();
		super.onAttach(context);
	}
}
