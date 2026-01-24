package com.khopan.core.view.card;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SwitchCardView extends CheckableCardView {
	private SwitchCompat switchView;
	private View dividerView;

	public SwitchCardView(@NonNull final Context context) {
		this(context, null, 0);
	}

	public SwitchCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	public SwitchCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet, final int defaultStyleAttribute) {
		super(context, attributeSet, defaultStyleAttribute);
		this.initializeSwitch(true);
	}

	private void initializeSwitch(final boolean dividerVisible) {
		if(this.switchView != null) {
			return;
		}

		final Context context = this.getContext();
		this.switchView = new SwitchCompat(context);
		this.switchView.setClickable(true);
		this.switchView.setFocusable(false);
		final int switchViewIdentifier = View.generateViewId();
		this.switchView.setId(switchViewIdentifier);
		final Resources.Theme theme = context.getTheme();
		final TypedValue value = new TypedValue();
		theme.resolveAttribute(android.R.attr.listPreferredItemPaddingEnd, value, true);
		final Resources resources = context.getResources();
		final DisplayMetrics metrics = resources.getDisplayMetrics();
		this.switchView.setPaddingRelative(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12.0f, metrics)), 0, Math.round(value.getDimension(metrics)), 0);
		final ConstraintLayout.LayoutParams switchViewParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
		final int constraintLayoutIdentifier = this.constraintLayout.getId();
		switchViewParams.bottomToBottom = constraintLayoutIdentifier;
		switchViewParams.endToEnd = constraintLayoutIdentifier;
		switchViewParams.topToTop = constraintLayoutIdentifier;
		this.constraintLayout.addView(this.switchView, switchViewParams);
		this.dividerView = new View(context);
		theme.resolveAttribute(androidx.appcompat.R.attr.switchDividerColor, value, true);
		this.dividerView.setBackgroundResource(value.resourceId);
		this.dividerView.setVisibility(dividerVisible ? View.VISIBLE : View.GONE);
		@SuppressLint("PrivateResource")
		final ConstraintLayout.LayoutParams dividerViewParams = new ConstraintLayout.LayoutParams(resources.getDimensionPixelSize(androidx.appcompat.R.dimen.sesl_switch_divider_height), Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22.0f, metrics)));
		dividerViewParams.bottomToBottom = constraintLayoutIdentifier;
		dividerViewParams.endToStart = switchViewIdentifier;
		dividerViewParams.topToTop = constraintLayoutIdentifier;
		this.constraintLayout.addView(this.dividerView, dividerViewParams);
	}
}
