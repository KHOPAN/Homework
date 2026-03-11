package com.khopan.core.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

/**
 * A {@link com.khopan.core.fragment.ContextedFragment} with
 * a built-in {@link androidx.core.widget.NestedScrollView}.
 */
public abstract class FunctionalFragment extends ContextedFragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup group, @Nullable final Bundle bundle) {
		return new NestedScrollView(this.context);
	}

	@Override
	public void onViewCreated(@NonNull final View view, @Nullable final Bundle bundle) {
		final NestedScrollView scrollView = (NestedScrollView) view;
		scrollView.setFillViewport(true);
		scrollView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
		final int padding = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f, this.metrics));
		scrollView.setPadding(padding, 0, padding, 0);
		scrollView.addView(this.initialize(), new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	}

	/**
	 * Initializes the {@link android.view.View}.
	 *
	 * @return the {@link android.view.View}.
	 */
	protected View initialize() {
		return null;
	}
}
