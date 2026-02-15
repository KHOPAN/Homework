package com.khopan.core.view.card;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
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

/**
 * A {@link com.khopan.core.view.card.CardView} that
 * includes a checkbox.
 */
public class CheckableCardView extends CardView {
	/**
	 * Constant representing the single
	 * checkbox type.
	 */
	public static final int CHECKBOX_TYPE_SINGLE = 0x00;

	/**
	 * Constant representing the multiple
	 * checkbox type.
	 */
	public static final int CHECKBOX_TYPE_MULTIPLE = 0x01;

	/**
	 * The checkbox view.
	 */
	protected AppCompatSeslCheckedTextView checkboxView;

	/**
	 * The {@link com.khopan.core.view.card.CheckableCardView.CheckboxStateListener}.
	 */
	protected CheckboxStateListener listener;

	/**
	 * The checkbox type.
	 */
	protected int checkboxType;

	/**
	 * Constructs a new {@link com.khopan.core.view.card.CheckableCardView}.
	 *
	 * @param context the {@link android.content.Context}.
	 */
	public CheckableCardView(@NonNull final Context context) {
		this(context, null, 0);
	}

	/**
	 * Constructs a new {@link com.khopan.core.view.card.CheckableCardView}.
	 *
	 * @param context the {@link android.content.Context}.
	 * @param attributeSet the {@link android.util.AttributeSet}.
	 */
	public CheckableCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	/**
	 * Constructs a new {@link com.khopan.core.view.card.CheckableCardView}.
	 *
	 * @param context the {@link android.content.Context}.
	 * @param attributeSet the {@link android.util.AttributeSet}.
	 * @param defaultStyleAttribute the default style attribute.
	 */
	public CheckableCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet, final int defaultStyleAttribute) {
		super(context, attributeSet, defaultStyleAttribute);
		final TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.CheckableCardView, defaultStyleAttribute, 0);

		try {
			if(array.hasValue(R.styleable.CheckableCardView_checkboxState)) {
				this.setCheckboxState(array.getBoolean(R.styleable.CheckableCardView_checkboxState, false));
			}

			if(array.hasValue(R.styleable.CheckableCardView_checkboxVisible)) {
				this.setCheckboxVisible(array.getBoolean(R.styleable.CheckableCardView_checkboxVisible, false));
			}

			if(array.hasValue(R.styleable.CheckableCardView_checkboxType)) {
				this.setCheckboxType(array.getInt(R.styleable.CheckableCardView_checkboxType, CheckableCardView.CHECKBOX_TYPE_SINGLE));
			}
		} finally {
			array.recycle();
		}
	}

	/**
	 * @return the checkbox state.
	 */
	public boolean getCheckboxState() {
		return this.checkboxView != null && this.checkboxView.isChecked();
	}

	/**
	 * @return the {@link com.khopan.core.view.card.CheckableCardView.CheckboxStateListener}.
	 */
	public CheckboxStateListener getCheckboxStateListener() {
		return this.listener;
	}

	/**
	 * @return the checkbox type. One of {@link #CHECKBOX_TYPE_SINGLE}
	 *         or {@link #CHECKBOX_TYPE_MULTIPLE}.
	 */
	public int getCheckboxType() {
		return this.checkboxType;
	}

	/**
	 * @return the {@link androidx.appcompat.widget.AppCompatSeslCheckedTextView}.
	 */
	public AppCompatSeslCheckedTextView getCheckboxView() {
		return this.checkboxView;
	}

	/**
	 * @return whether or not the checkbox is visible.
	 */
	public boolean isCheckboxVisible() {
		return this.checkboxView != null && this.checkboxView.getVisibility() == View.VISIBLE;
	}

	/**
	 * Sets the checkbox state.
	 *
	 * @param state the checkbox state.
	 */
	public void setCheckboxState(final boolean state) {
		this.inflateCheckbox();
		this.setState(state);
	}

	/**
	 * Sets the {@link com.khopan.core.view.card.CheckableCardView.CheckboxStateListener}.
	 *
	 * @param listener the {@link com.khopan.core.view.card.CheckableCardView.CheckboxStateListener}.
	 */
	public void setCheckboxStateListener(final CheckboxStateListener listener) {
		this.listener = listener;
	}

	/**
	 * Sets the checkbox type.
	 *
	 * @param checkboxType the checkbox type. Can be one of
	 *                     {@link #CHECKBOX_TYPE_SINGLE}
	 *                     or {@link #CHECKBOX_TYPE_MULTIPLE}.
	 */
	public void setCheckboxType(final int checkboxType) {
		this.checkboxType = checkboxType;
		this.inflateCheckbox();
		this.updateCheckType();
	}

	/**
	 * Sets the visibility of the checkbox,
	 * or creates a new one if one doesn't exist.
	 *
	 * @param visible whether or not the checkbox
	 *        is visible.
	 */
	public void setCheckboxVisible(final boolean visible) {
		if(this.checkboxView != null) {
			this.checkboxView.setVisibility(visible ? View.VISIBLE : View.GONE);
			return;
		}

		if(visible) {
			this.inflateCheckbox();
		}
	}

	private void inflateCheckbox() {
		if(this.checkboxView != null) {
			return;
		}

		final Context context = this.getContext();
		this.checkboxView = new AppCompatSeslCheckedTextView(context);
		final int checkboxViewIdentifier = View.generateViewId();
		this.checkboxView.setId(checkboxViewIdentifier);
		this.updateCheckType();
		final ConstraintLayout.LayoutParams checkboxViewParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		checkboxViewParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
		checkboxViewParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
		checkboxViewParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
		this.constraintLayout.addView(this.checkboxView, checkboxViewParams);
		final ConstraintLayout.LayoutParams iconViewParams = (ConstraintLayout.LayoutParams) this.iconView.getLayoutParams();
		iconViewParams.startToEnd = checkboxViewIdentifier;
		iconViewParams.startToStart = ConstraintLayout.LayoutParams.UNSET;
		this.iconView.setLayoutParams(iconViewParams);
		this.setOnClickListener(view -> this.setState(!this.checkboxView.isChecked()));
	}

	private void setState(final boolean state) {
		if(this.checkboxView.isChecked() == state) {
			return;
		}

		this.checkboxView.setChecked(state);

		if(this.listener != null) {
			this.listener.checkboxStateChanged(this, state);
		}
	}

	private void updateCheckType() {
		final Context context = this.getContext();
		final Resources.Theme theme = context.getTheme();
		final TypedValue value = new TypedValue();
		theme.resolveAttribute((this.checkboxType = this.checkboxType == CheckableCardView.CHECKBOX_TYPE_MULTIPLE ? CheckableCardView.CHECKBOX_TYPE_MULTIPLE : CheckableCardView.CHECKBOX_TYPE_SINGLE) == CheckableCardView.CHECKBOX_TYPE_SINGLE ? android.R.attr.listChoiceIndicatorSingle : android.R.attr.listChoiceIndicatorMultiple, value, true);
		this.checkboxView.setCheckMarkDrawable(value.resourceId == 0 ? null : ContextCompat.getDrawable(context, value.resourceId));
	}

	/**
	 * A listener for handling the checkbox state changes.
	 */
	@FunctionalInterface
	public interface CheckboxStateListener {
		/**
		 * Handles the checkbox state changes.
		 *
		 * @param view the {@link com.khopan.core.view.card.CheckableCardView}.
		 * @param state the new checkbox state.
		 */
		void checkboxStateChanged(final CheckableCardView view, final boolean state);
	}
}
