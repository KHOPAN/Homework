package com.khopan.core.view.card;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.khopan.core.view.card.dialog.Dialog;

/**
 * A {@link com.khopan.core.view.card.CardView} that
 * holds a {@link com.khopan.core.view.card.dialog.Dialog}.
 */
public class DialogCardView extends CardView {
	/**
	 * The {@link com.khopan.core.view.card.dialog.Dialog}.
	 */
	protected Dialog<?> dialog;

	/**
	 * Constructs a new {@link com.khopan.core.view.card.DialogCardView} instance.
	 *
	 * @param context the {@link android.content.Context}.
	 */
	public DialogCardView(@NonNull final Context context) {
		this(context, null, 0);
	}

	/**
	 * Constructs a new {@link com.khopan.core.view.card.DialogCardView} instance.
	 *
	 * @param context the {@link android.content.Context}.
	 * @param attributeSet the {@link android.util.AttributeSet}.
	 */
	public DialogCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	/**
	 * Constructs a new {@link com.khopan.core.view.card.DialogCardView} instance.
	 *
	 * @param context the {@link android.content.Context}.
	 * @param dialog the {@link com.khopan.core.view.card.dialog.Dialog}.
	 */
	public DialogCardView(@NonNull final Context context, final Dialog<?> dialog) {
		this(context, null, 0);
		this.dialog = dialog;
	}

	/**
	 * Constructs a new {@link com.khopan.core.view.card.DialogCardView} instance.
	 *
	 * @param context the {@link android.content.Context}.
	 * @param attributeSet the {@link android.util.AttributeSet}.
	 * @param defaultStyleAttribute the default style attribute.
	 */
	public DialogCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet, final int defaultStyleAttribute) {
		super(context, attributeSet, defaultStyleAttribute);
		this.setOnClickListener(view -> {
			if(this.dialog != null) {
				this.dialog.show();
			}
		});
	}

	/**
	 * @return the {@link com.khopan.core.view.card.dialog.Dialog}.
	 */
	public Dialog<?> getDialog() {
		return this.dialog;
	}

	/**
	 * Sets the {@link com.khopan.core.view.card.dialog.Dialog}.
	 *
	 * @param dialog the {@link com.khopan.core.view.card.dialog.Dialog}.
	 */
	public void setDialog(final Dialog<?> dialog) {
		this.dialog = dialog;

		if(this.dialog != null) {
			this.dialog.setDialogUpdateListener(this::updateSummary);
			this.updateSummary();
		}
	}

	private void updateSummary() {
		if(this.dialog != null) {
			this.setSummary(this.dialog.getSummary());
		}
	}
}
