package com.khopan.core.view.card;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class DialogCardView extends SwitchableCardView {
	public DialogCardView(@NonNull final Context context) {
		this(context, null, 0);
	}

	public DialogCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	public DialogCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet, final int defaultStyleAttribute) {
		super(context, attributeSet, defaultStyleAttribute);
		this.setOnClickListener(view -> this.showDialog());
	}

	protected void showDialog() {

	}
}
