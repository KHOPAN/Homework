package com.khopan.core.view.card;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.khopan.core.view.card.dialog.Dialog;

/**
 * A {@link com.khopan.core.view.card.CardView} that
 * holds a dialog.
 *
 * @param <T> the dialog type.
 */
public abstract class DialogCardView<T extends Dialog> extends CardView {
	/**
	 * The dialog.
	 */
	public final T dialog;

	/**
	 * Constructs a new {@link com.khopan.core.view.card.DialogCardView}.
	 *
	 * @param context the {@link android.content.Context}.
	 */
	public DialogCardView(@NonNull final Context context) {
		this(context, null, 0);
	}

	/**
	 * Constructs a new {@link com.khopan.core.view.card.DialogCardView}.
	 *
	 * @param context the {@link android.content.Context}.
	 * @param attributeSet the {@link android.util.AttributeSet}.
	 */
	public DialogCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	/**
	 * Constructs a new {@link com.khopan.core.view.card.DialogCardView}.
	 *
	 * @param context the {@link android.content.Context}.
	 * @param attributeSet the {@link android.util.AttributeSet}.
	 * @param defaultStyleAttribute the default style attribute.
	 */
	public DialogCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet, final int defaultStyleAttribute) {
		super(context, attributeSet, defaultStyleAttribute);
		this.dialog = this.createDialog(context);
		this.setOnClickListener(view -> this.showDialog());
	}

	/**
	 * Cancels the dialog.
	 */
	public void cancelDialog() {
		this.dialog.dialog.cancel();
	}

	/**
	 * Sets the dialog title.
	 *
	 * @param title the dialog title.
	 */
	public void setDialogTitle(final CharSequence title) {
		this.dialog.dialog.setTitle(title);
	}

	/**
	 * Shows the dialog.
	 */
	public void showDialog() {
		this.dialog.dialog.show();
	}

	/**
	 * Creates a new dialog instance.
	 *
	 * @param context the {@link android.content.Context}.
	 * @return the new dialog instance.
	 */
	protected abstract T createDialog(final Context context);
}
