package com.khopan.core.fragment;

import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.khopan.core.CoreLayout;

/**
 * A {@link com.khopan.core.fragment.FunctionalFragment} that
 * displays a text over a button.
 */
public class CenterButtonTextFragment extends FunctionalFragment {
	private final String text;
	private final String buttonText;
	private final Runnable listener;

	/**
	 * Constructs a new {@link com.khopan.core.fragment.CenterButtonTextFragment} instance.
	 *
	 * @param text the text.
	 * @param buttonText the button text.
	 * @param listener the button press listener.
	 */
	public CenterButtonTextFragment(final String text, final String buttonText, final Runnable listener) {
		this.text = text;
		this.buttonText = buttonText;
		this.listener = listener;
	}

	@Override
	protected View initialize() {
		final ConstraintLayout constraintLayout = new ConstraintLayout(this.context);
		CoreLayout.setLayoutTransition(constraintLayout);
		final ConstraintLayout.LayoutParams buttonParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		final int buttonIdentifier = View.generateViewId();

		if(this.buttonText != null) {
			final TypedValue value = new TypedValue();
			this.theme.resolveAttribute(androidx.appcompat.R.attr.buttonStyle, value, true);
			final AppCompatButton button = new AppCompatButton(new ContextThemeWrapper(this.context, value.data));
			button.setId(buttonIdentifier);
			button.setOnClickListener(view -> {
				if(this.listener != null) {
					this.listener.run();
				}
			});

			button.setText(this.buttonText);
			buttonParams.leftToLeft = buttonParams.rightToRight = buttonParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
			constraintLayout.addView(button, buttonParams);
		}

		final ConstraintLayout.LayoutParams textViewParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		final int textViewIdentifier = View.generateViewId();

		if(this.text != null) {
			final TextView textView = new TextView(this.context);
			textView.setId(textViewIdentifier);
			textView.setGravity(Gravity.CENTER);
			textView.setSelected(false);
			textView.setText(this.text);
			textView.setTextAppearance(dev.oneuiproject.oneui.design.R.style.OneUI_SearchHighlightedTextAppearance);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
			textViewParams.leftToLeft = textViewParams.topToTop = textViewParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
			constraintLayout.addView(textView, textViewParams);
		}

		if(this.text == null) {
			buttonParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
		} else {
			buttonParams.topToBottom = textViewIdentifier;
		}

		if(this.buttonText == null) {
			textViewParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
		} else {
			textViewParams.bottomToTop = buttonIdentifier;
		}

		return constraintLayout;
	}
}
