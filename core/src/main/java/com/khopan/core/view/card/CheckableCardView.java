package com.khopan.core.view.card;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeslCheckedTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.khopan.core.R;

public class CheckableCardView extends CardView {
	private final AppCompatSeslCheckedTextView checkbox;

	public CheckableCardView(@NonNull final Context context) {
		this(context, null, 0);
	}

	public CheckableCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	public CheckableCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet, final int defaultStyleAttribute) {
		super(context, attributeSet, defaultStyleAttribute);
		final ConstraintLayout constraintLayout = this.findViewById(R.id.constraint_layout);
		this.checkbox = new AppCompatSeslCheckedTextView(context);
		final TypedValue value = new TypedValue();
		context.getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle, value, true);
		this.checkbox.setCheckMarkDrawable(value.resourceId);
		final ConstraintLayout.LayoutParams checkboxParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		final int constraintLayoutIdentifier = constraintLayout.getId();
		checkboxParams.bottomToBottom = constraintLayoutIdentifier;
		checkboxParams.startToStart = constraintLayoutIdentifier;
		checkboxParams.topToTop = constraintLayoutIdentifier;
		constraintLayout.addView(this.checkbox, checkboxParams);
	}

	@Override
	public boolean performClick() {
		this.checkbox.setChecked(!this.checkbox.isChecked());
		return super.performClick();
	}
}
