package com.khopan.core.view.card.dialog;

import android.content.Context;
import android.content.DialogInterface;

import androidx.picker.widget.SeslNumberPicker;

/**
 * A {@link com.khopan.core.view.card.dialog.Dialog} wrapper for
 * {@link androidx.picker.widget.SeslNumberPicker}.
 */
public class NumberPickerDialog extends Dialog {
	/**
	 * The {@link androidx.picker.widget.SeslNumberPicker}.
	 */
	public final SeslNumberPicker picker;

	/**
	 * The {@link com.khopan.core.view.card.dialog.NumberPickerDialog.NumberPickerListener}.
	 */
	protected NumberPickerListener listener;

	/**
	 * Constructs a new {@link com.khopan.core.view.card.dialog.NumberPickerDialog}.
	 *
	 * @param context the {@link android.content.Context}.
	 */
	public NumberPickerDialog(final Context context) {
		super(context);
		this.dialog.setView(this.picker = new SeslNumberPicker(context));
	}

	/**
	 * @return the maximum picker value.
	 */
	public int getMaximumValue() {
		return this.picker.getMaxValue();
	}

	/**
	 * @return the minimum picker value.
	 */
	public int getMinimumValue() {
		return this.picker.getMinValue();
	}

	/**
	 * @return the {@link com.khopan.core.view.card.dialog.NumberPickerDialog.NumberPickerListener}.
	 */
	public NumberPickerListener getNumberPickerListener() {
		return this.listener;
	}

	/**
	 * @return the current picker value.
	 */
	public int getValue() {
		return this.picker.getValue();
	}

	/**
	 * Sets the maximum picker value.
	 *
	 * @param value the maximum picker value.
	 */
	public void setMaximumValue(final int value) {
		this.picker.setMaxValue(value);
	}

	/**
	 * Sets the minimum picker value.
	 *
	 * @param value the minimum picker value.
	 */
	public void setMinimumValue(final int value) {
		this.picker.setMinValue(value);
	}

	/**
	 * Sets the {@link com.khopan.core.view.card.dialog.NumberPickerDialog.NumberPickerListener}.
	 *
	 * @param listener the {@link com.khopan.core.view.card.dialog.NumberPickerDialog.NumberPickerListener}.
	 */
	public void setNumberPickerListener(final NumberPickerListener listener) {
		this.listener = listener;
	}

	/**
	 * Sets the current picker value.
	 *
	 * @param value the picker value.
	 */
	public void setValue(final int value) {
		this.picker.setValue(value);
	}

	@Override
	protected boolean buttonClicked(final int button) {
		if(button != DialogInterface.BUTTON_POSITIVE) {
			return false;
		}

		if(this.listener != null) {
			this.listener.numberPicked(this, this.picker.getValue());
		}

		return true;
	}

	/**
	 * A listener for handling when the number is picked.
	 */
	@FunctionalInterface
	public interface NumberPickerListener {
		/**
		 * Handles the picked number.
		 *
		 * @param dialog the {@link com.khopan.core.view.card.dialog.NumberPickerDialog}.
		 * @param number the picked number.
		 */
		void numberPicked(final NumberPickerDialog dialog, final int number);
	}
}
