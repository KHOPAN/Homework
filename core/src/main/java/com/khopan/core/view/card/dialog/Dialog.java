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
	 * The {@link com.khopan.core.view.card.dialog.Dialog.DialogUpdateListener}.
	 */
	protected DialogUpdateListener updateListener;

	/**
	 * Constructs a new {@link com.khopan.core.view.card.dialog.Dialog} instance.
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
	 * Cancels the {@link com.khopan.core.view.card.dialog.Dialog}.
	 */
	public void cancel() {
		this.dialog.cancel();
	}

	/**
	 * Sets the {@link com.khopan.core.view.card.dialog.Dialog.DialogUpdateListener}.
	 *
	 * @param listener the {@link com.khopan.core.view.card.dialog.Dialog.DialogUpdateListener}.
	 */
	public void setDialogUpdateListener(final DialogUpdateListener listener) {
		this.updateListener = listener;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the title.
	 */
	public void setTitle(final CharSequence title) {
		this.dialog.setTitle(title);
	}

	/**
	 * Shows the {@link com.khopan.core.view.card.dialog.Dialog}.
	 */
	public void show() {
		this.dialog.show();
	}

	/**
	 * @return the summary.
	 */
	public abstract CharSequence getSummary();

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
	 * Handles a dialog button click.
	 *
	 * @param button the button.
	 * @return true if the event has been handled, false otherwise.
	 */
	protected boolean buttonClicked(final int button) {
		return false;
	}

	/**
	 * A listener for handling dialog state changes.
	 */
	@FunctionalInterface
	public interface DialogUpdateListener {
		/**
		 * Handles the dialog state changes.
		 */
		void dialogUpdated();
	}
}
