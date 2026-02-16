package com.khopan.core.view.card;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.khopan.core.view.card.dialog.NumberPickerDialog;

/**
 * A {@link com.khopan.core.view.card.DialogCardView} that
 * holds a number picker.
 */
public class NumberPickerCardView extends DialogCardView<NumberPickerDialog> {
	/**
	 * Constructs a new {@link com.khopan.core.view.card.NumberPickerCardView}.
	 *
	 * @param context the {@link android.content.Context}.
	 */
	public NumberPickerCardView(@NonNull final Context context) {
		this(context, null, 0);
	}

	/**
	 * Constructs a new {@link com.khopan.core.view.card.NumberPickerCardView}.
	 *
	 * @param context the {@link android.content.Context}.
	 * @param attributeSet the {@link android.util.AttributeSet}.
	 */
	public NumberPickerCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	/**
	 * Constructs a new {@link com.khopan.core.view.card.NumberPickerCardView}.
	 *
	 * @param context the {@link android.content.Context}.
	 * @param attributeSet the {@link android.util.AttributeSet}.
	 * @param defaultStyleAttribute the default style attribute.
	 */
	public NumberPickerCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet, final int defaultStyleAttribute) {
		super(context, attributeSet, defaultStyleAttribute);
	}

	/**
	 * @return the maximum picker value.
	 */
	public int getMaximumValue() {
		return this.dialog.picker.getMaxValue();
	}

	/**
	 * @return the minimum picker value.
	 */
	public int getMinimumValue() {
		return this.dialog.picker.getMinValue();
	}

	/**
	 * @return the {@link com.khopan.core.view.card.dialog.NumberPickerDialog.NumberPickerListener}.
	 */
	public NumberPickerDialog.NumberPickerListener getNumberPickerListener() {
		return this.dialog.getNumberPickerListener();
	}

	/**
	 * @return the current picker value.
	 */
	public int getValue() {
		return this.dialog.picker.getValue();
	}

	/**
	 * Sets the maximum picker value.
	 *
	 * @param value the maximum picker value.
	 */
	public void setMaximumValue(final int value) {
		this.dialog.picker.setMaxValue(value);
	}

	/**
	 * Sets the minimum picker value.
	 *
	 * @param value the minimum picker value.
	 */
	public void setMinimumValue(final int value) {
		this.dialog.picker.setMinValue(value);
	}

	/**
	 * Sets the {@link com.khopan.core.view.card.dialog.NumberPickerDialog.NumberPickerListener}.
	 *
	 * @param listener the {@link com.khopan.core.view.card.dialog.NumberPickerDialog.NumberPickerListener}.
	 */
	public void setNumberPickerListener(final NumberPickerDialog.NumberPickerListener listener) {
		this.dialog.setNumberPickerListener(listener);
	}

	/**
	 * Sets the current picker value.
	 *
	 * @param value the picker value.
	 */
	public void setValue(final int value) {
		this.dialog.picker.setValue(value);
	}

	@Override
	protected NumberPickerDialog createDialog(final Context context) {
		return new NumberPickerDialog(context);
	}
}
