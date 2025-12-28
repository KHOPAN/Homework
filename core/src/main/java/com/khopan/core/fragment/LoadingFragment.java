package com.khopan.core.fragment;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.SeslProgressBar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.khopan.core.CoreLayout;

public class LoadingFragment extends FunctionalFragment {
	private final String text;

	public LoadingFragment() {
		this(null);
	}

	public LoadingFragment(@Nullable final String text) {
		this.text = text == null ? "" : text;
	}

	@Override
	protected View initialize() {
		final ConstraintLayout constraintLayout = new ConstraintLayout(this.context);
		final int constraintLayoutIdentifier = View.generateViewId();
		constraintLayout.setId(constraintLayoutIdentifier);
		CoreLayout.setLayoutTransition(constraintLayout);
		final SeslProgressBar progressBar = new SeslProgressBar(new ContextThemeWrapper(this.context, androidx.constraintlayout.widget.R.style.Widget_AppCompat_ProgressBar));
		final int progressBarIdentifier = View.generateViewId();
		progressBar.setId(progressBarIdentifier);
		final ConstraintLayout.LayoutParams progressBarParameters = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		progressBarParameters.leftToLeft = progressBarParameters.topToTop = progressBarParameters.rightToRight = progressBarParameters.bottomToBottom = constraintLayoutIdentifier;
		progressBar.setLayoutParams(progressBarParameters);
		progressBar.setIndeterminate(true);
		constraintLayout.addView(progressBar);
		final TextView textView = new TextView(this.context);
		final ConstraintLayout.LayoutParams textViewParameters = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		textViewParameters.leftToLeft = textViewParameters.rightToRight = constraintLayoutIdentifier;
		textViewParameters.topToBottom = progressBarIdentifier;
		textView.setLayoutParams(textViewParameters);
		textView.setGravity(Gravity.CENTER);
		textView.setText(this.text);
		constraintLayout.addView(textView);
		return constraintLayout;
	}
}
