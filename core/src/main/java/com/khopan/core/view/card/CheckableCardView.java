package com.khopan.core.view.card;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeslCheckedTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

public class CheckableCardView extends CardView {
	public static final int CHECK_TYPE_SINGLE = 0;
	public static final int CHECK_TYPE_MULTIPLE = 1;

	private final Drawable checkmarkDrawableSingle;
	private final Drawable checkmarkDrawableMultiple;
	private final AppCompatSeslCheckedTextView checkbox;

	private int checkType;

	public CheckableCardView(@NonNull final Context context) {
		this(context, null, 0);
	}

	public CheckableCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	public CheckableCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet, final int defaultStyleAttribute) {
		super(context, attributeSet, defaultStyleAttribute);
		final Resources.Theme theme = context.getTheme();
		final TypedValue value = new TypedValue();
		theme.resolveAttribute(android.R.attr.listChoiceIndicatorSingle, value, true);
		this.checkmarkDrawableSingle = value.resourceId == 0 ? null : ContextCompat.getDrawable(context, value.resourceId);
		theme.resolveAttribute(android.R.attr.listChoiceIndicatorMultiple, value, true);
		this.checkmarkDrawableMultiple = value.resourceId == 0 ? null : ContextCompat.getDrawable(context, value.resourceId);
		this.checkbox = new AppCompatSeslCheckedTextView(context);
		final ConstraintLayout.LayoutParams checkboxParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		final int constraintLayoutIdentifier = this.constraintLayout.getId();
		checkboxParams.bottomToBottom = constraintLayoutIdentifier;
		checkboxParams.startToStart = constraintLayoutIdentifier;
		checkboxParams.topToTop = constraintLayoutIdentifier;
		this.constraintLayout.addView(this.checkbox, checkboxParams);
		this.setCheckType(CheckableCardView.CHECK_TYPE_SINGLE);
	}

	public int getCheckType() {
		return this.checkType;
	}

	@Override
	public boolean performClick() {
		this.checkbox.setChecked(!this.checkbox.isChecked());
		return super.performClick();
	}

	public void setCheckType(final int checkType) {
		this.checkType = checkType == CheckableCardView.CHECK_TYPE_MULTIPLE ? CheckableCardView.CHECK_TYPE_MULTIPLE : CheckableCardView.CHECK_TYPE_SINGLE;
		this.checkbox.setCheckMarkDrawable(this.checkType == CheckableCardView.CHECK_TYPE_SINGLE ? this.checkmarkDrawableSingle : this.checkmarkDrawableMultiple);
	}
}
