package com.khopan.core.view.card.dialog;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.khopan.core.R;

/**
 * An abstract wrapper class for
 * {@link androidx.appcompat.app.AlertDialog}.
 */
public abstract class Dialog {
	/**
	 * The {@link androidx.appcompat.app.AlertDialog}.
	 */
	public final AlertDialog dialog;

	/**
	 * The {@link android.content.Context}.
	 */
	protected final Context context;

	/**
	 * The {@link android.content.DialogInterface.OnClickListener}.
	 */
	protected final DialogInterface.OnClickListener dialogListener;

	/**
	 * Constructs a new {@link com.khopan.core.view.card.dialog.Dialog}.
	 *
	 * @param context the {@link android.content.Context}.
	 */
	public Dialog(final Context context) {
		this.context = context;
		this.dialogListener = (dialog, button) -> this.buttonClicked(button);
		final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
		this.buildDialog(builder);
		this.dialog = builder.create();
	}

	/**
	 * Cancels the dialog.
	 */
	public void cancel() {
		this.dialog.cancel();
	}

	/**
	 * Sets the dialog title.
	 *
	 * @param title the dialog title.
	 */
	public void setTitle(final CharSequence title) {
		this.dialog.setTitle(title);
	}

	/**
	 * Shows the dialog.
	 */
	public void show() {
		this.dialog.show();
	}

	/**
	 * Builds the {@link androidx.appcompat.app.AlertDialog}.
	 *
	 * @param builder the {@link androidx.appcompat.app.AlertDialog.Builder}.
	 */
	protected void buildDialog(final AlertDialog.Builder builder) {
		builder.setNegativeButton(R.string.dialog_button_negative, this.dialogListener);
		builder.setPositiveButton(R.string.dialog_button_positive, this.dialogListener);
	}

	/**
	 * Handles the dialog button clicks.
	 *
	 * @param button the button.
	 * @return true if the event has been handled, false otherwise.
	 */
	protected boolean buttonClicked(final int button) {
		return false;
	}
}
