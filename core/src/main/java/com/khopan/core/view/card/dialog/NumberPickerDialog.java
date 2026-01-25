package com.khopan.core.view.card.dialog;

import android.content.Context;

import androidx.picker.widget.SeslNumberPicker;

public class NumberPickerDialog extends Dialog {
	private final SeslNumberPicker picker;

	private NumberPickerListener listener;

	public NumberPickerDialog(final Context context) {
		super(context);
		this.picker = new SeslNumberPicker(context);
		this.picker.setMaxValue(31);
		this.picker.setMinValue(16);
		this.dialog.setView(this.picker);
	}

	public SeslNumberPicker getNumberPicker() {
		return this.picker;
	}

	public NumberPickerListener getNumberPickerListener() {
		return this.listener;
	}

	public void setMaximumValue(final int value) {
		this.picker.setMaxValue(value);
	}

	public void setMinimumValue(final int value) {
		this.picker.setMinValue(value);
	}

	public void setNumberPickerListener(final NumberPickerListener listener) {
		this.listener = listener;
	}

	@FunctionalInterface
	public interface NumberPickerListener {
		void numberPicked(final NumberPickerDialog dialog, final int number);
	}
}
