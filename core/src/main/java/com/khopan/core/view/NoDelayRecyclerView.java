package com.khopan.core.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class NoDelayRecyclerView extends RecyclerView {
	public NoDelayRecyclerView(@NonNull final Context context) {
		super(context);
	}

	public NoDelayRecyclerView(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	public NoDelayRecyclerView(@NonNull final Context context, @Nullable final AttributeSet attributeSet, final int defaultStyleAttribute) {
		super(context, attributeSet, defaultStyleAttribute);
	}

	@Override
	public boolean shouldDelayChildPressedState() {
		return false;
	}
}
