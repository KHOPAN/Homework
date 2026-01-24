package com.khopan.core.view.card;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeslCheckedTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.khopan.core.R;

public class CheckableCardView extends CardView {
	public static final int CHECKBOX_TYPE_SINGLE = 0x00;
	public static final int CHECKBOX_TYPE_MULTIPLE = 0x01;

	protected final Drawable checkmarkDrawableSingle;
	protected final Drawable checkmarkDrawableMultiple;

	protected AppCompatSeslCheckedTextView checkbox;
	protected int checkboxType;
	protected CheckboxStateListener listener;

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
		final TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.CheckableCardView, defaultStyleAttribute, 0);

		try {
			if(array.hasValue(R.styleable.CheckableCardView_checkboxState)) {
				this.setCheckboxState(array.getBoolean(R.styleable.CheckableCardView_checkboxState, false));
			}

			if(array.hasValue(R.styleable.CheckableCardView_checkboxVisible)) {
				this.setCheckboxVisible(array.getBoolean(R.styleable.CheckableCardView_checkboxVisible, false));
			}

			if(array.hasValue(R.styleable.CheckableCardView_checkboxType)) {
				this.setEndIconTint(array.getInt(R.styleable.CheckableCardView_checkboxType, CheckableCardView.CHECKBOX_TYPE_SINGLE));
			}
		} finally {
			array.recycle();
		}
	}

	public boolean getCheckboxState() {
		return this.checkbox != null && this.checkbox.isChecked();
	}

	public CheckboxStateListener getCheckboxStateListener() {
		return this.listener;
	}

	public int getCheckboxType() {
		return this.checkboxType;
	}

	public AppCompatSeslCheckedTextView getCheckboxView() {
		return this.checkbox;
	}

	public Drawable getCheckmarkDrawableMultiple() {
		return this.checkmarkDrawableMultiple;
	}

	public Drawable getCheckmarkDrawableSingle() {
		return this.checkmarkDrawableSingle;
	}

	public boolean isCheckboxVisible() {
		return this.checkbox != null && this.checkbox.getVisibility() == View.VISIBLE;
	}

	public void setCheckboxState(final boolean state) {
		this.inflateCheckbox();
		this.setState(state);
	}

	public void setCheckboxStateListener(final CheckboxStateListener listener) {
		this.listener = listener;
	}

	public void setCheckboxType(final int checkboxType) {
		this.checkboxType = checkboxType;
		this.inflateCheckbox();
		this.updateCheckType();
	}

	public void setCheckboxVisible(final boolean visible) {
		if(this.checkbox != null) {
			this.checkbox.setVisibility(visible ? View.VISIBLE : View.GONE);
			return;
		}

		if(visible) {
			this.inflateCheckbox();
		}
	}

	private void inflateCheckbox() {
		if(this.checkbox != null) {
			return;
		}

		final Context context = this.getContext();
		this.checkbox = new AppCompatSeslCheckedTextView(context);
		final int checkboxIdentifier = View.generateViewId();
		this.checkbox.setId(checkboxIdentifier);
		final ConstraintLayout.LayoutParams checkboxParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		final int constraintLayoutIdentifier = this.constraintLayout.getId();
		checkboxParams.bottomToBottom = constraintLayoutIdentifier;
		checkboxParams.startToStart = constraintLayoutIdentifier;
		checkboxParams.topToTop = constraintLayoutIdentifier;
		this.updateCheckType();
		this.constraintLayout.addView(this.checkbox, checkboxParams);
		final View iconView = this.constraintLayout.findViewById(R.id.icon_view);
		final ConstraintLayout.LayoutParams iconViewParams = (ConstraintLayout.LayoutParams) iconView.getLayoutParams();
		iconViewParams.startToStart = ConstraintLayout.LayoutParams.UNSET;
		iconViewParams.startToEnd = checkboxIdentifier;
		iconView.setLayoutParams(iconViewParams);
		this.setOnClickListener(view -> this.setState(!this.checkbox.isChecked()));
	}

	private void setState(final boolean state) {
		if(this.checkbox.isChecked() == state) {
			return;
		}

		this.checkbox.setChecked(state);

		if(this.listener != null) {
			this.listener.checkboxStateChanged(this, state);
		}
	}

	private void updateCheckType() {
		this.checkbox.setCheckMarkDrawable((this.checkboxType = this.checkboxType == CheckableCardView.CHECKBOX_TYPE_MULTIPLE ? CheckableCardView.CHECKBOX_TYPE_MULTIPLE : CheckableCardView.CHECKBOX_TYPE_SINGLE) == CheckableCardView.CHECKBOX_TYPE_SINGLE ? this.checkmarkDrawableSingle : this.checkmarkDrawableMultiple);
	}

	@FunctionalInterface
	public interface CheckboxStateListener {
		void checkboxStateChanged(final CheckableCardView view, final boolean state);
	}
}
