package com.khopan.core.fragment;

import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.khopan.core.CoreLayout;

public class CenterButtonTextFragment extends FunctionalFragment {
	private final String text;
	private final String buttonText;
	private final Runnable listener;

	public CenterButtonTextFragment() {
		this(null, null, null);
	}

	public CenterButtonTextFragment(@Nullable final String text, @Nullable final String buttonText, @Nullable final Runnable listener) {
		this.text = text == null ? "" : text;
		this.buttonText = buttonText == null ? "" : buttonText;
		this.listener = listener;
	}

	@Override
	protected View initialize() {
		final ConstraintLayout constraintLayout = new ConstraintLayout(this.context);
		final int constraintLayoutIdentifier = View.generateViewId();
		constraintLayout.setId(constraintLayoutIdentifier);
		CoreLayout.setLayoutTransition(constraintLayout);
		final TypedValue value = new TypedValue();
		this.context.getTheme().resolveAttribute(androidx.appcompat.R.attr.buttonStyle, value, true);
		final AppCompatButton button = new AppCompatButton(new ContextThemeWrapper(this.context, value.data));
		final int buttonIdentifier = View.generateViewId();
		button.setId(buttonIdentifier);
		final ConstraintLayout.LayoutParams buttonParameters = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		buttonParameters.leftToLeft = buttonParameters.topToTop = buttonParameters.rightToRight = buttonParameters.bottomToBottom = constraintLayoutIdentifier;
		button.setLayoutParams(buttonParameters);
		button.setOnClickListener(view -> {
			if(this.listener != null) {
				this.listener.run();
			}
		});

		button.setText(this.buttonText);
		constraintLayout.addView(button);
		final TextView textView = new TextView(this.context);
		final ConstraintLayout.LayoutParams textViewParameters = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		textViewParameters.leftToLeft = textViewParameters.topToTop = textViewParameters.rightToRight = constraintLayoutIdentifier;
		textViewParameters.bottomToTop = buttonIdentifier;
		textView.setLayoutParams(textViewParameters);
		textView.setGravity(Gravity.CENTER);
		textView.setSelected(false);
		textView.setText(this.text);
		textView.setTextAppearance(dev.oneuiproject.oneui.design.R.style.OneUI_SearchHighlightedTextAppearance);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
		constraintLayout.addView(textView);
		return constraintLayout;
	}
}
