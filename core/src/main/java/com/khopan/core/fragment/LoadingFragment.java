package com.khopan.core.fragment;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.SeslProgressBar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.khopan.core.CoreLayout;

/**
 * A {@link com.khopan.core.fragment.FunctionalFragment} that
 * displays a loading progress bar and optionally a text.
 */
public class LoadingFragment extends FunctionalFragment {
	private final String text;

	/**
	 * Constructs a new {@link com.khopan.core.fragment.LoadingFragment} instance.
	 *
	 * @param text the text.
	 */
	public LoadingFragment(final String text) {
		this.text = text;
	}

	@Override
	protected View initialize() {
		final ConstraintLayout constraintLayout = new ConstraintLayout(this.context);
		CoreLayout.setLayoutTransition(constraintLayout);
		final SeslProgressBar progressBar = new SeslProgressBar(new ContextThemeWrapper(this.context, androidx.constraintlayout.widget.R.style.Widget_AppCompat_ProgressBar));
		final int progressBarIdentifier = View.generateViewId();
		progressBar.setId(progressBarIdentifier);
		progressBar.setIndeterminate(true);
		final ConstraintLayout.LayoutParams progressBarParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		progressBarParams.leftToLeft = progressBarParams.topToTop = progressBarParams.rightToRight = progressBarParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
		constraintLayout.addView(progressBar, progressBarParams);

		if(this.text != null) {
			final TextView textView = new TextView(this.context);
			textView.setGravity(Gravity.CENTER);
			textView.setText(this.text);
			final ConstraintLayout.LayoutParams textViewParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			textViewParams.leftToLeft = textViewParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
			textViewParams.topToBottom = progressBarIdentifier;
			constraintLayout.addView(textView, textViewParams);
		}

		return constraintLayout;
	}
}
