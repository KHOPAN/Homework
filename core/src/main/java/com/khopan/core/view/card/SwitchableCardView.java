package com.khopan.core.view.card;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.khopan.core.R;

public class SwitchableCardView extends CheckableCardView {
	protected SwitchStateListener listener;
	protected SwitchCompat switchView;
	protected View dividerView;

	public SwitchableCardView(@NonNull final Context context) {
		this(context, null, 0);
	}

	public SwitchableCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	public SwitchableCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet, final int defaultStyleAttribute) {
		super(context, attributeSet, defaultStyleAttribute);
		final TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.SwitchableCardView, defaultStyleAttribute, 0);

		try {
			if(array.hasValue(R.styleable.SwitchableCardView_switchDividerVisible)) {
				this.setSwitchDividerVisible(array.getBoolean(R.styleable.SwitchableCardView_switchDividerVisible, false));
			}

			if(array.hasValue(R.styleable.SwitchableCardView_switchState)) {
				this.setSwitchState(array.getBoolean(R.styleable.SwitchableCardView_switchState, false));
			}

			if(array.hasValue(R.styleable.SwitchableCardView_switchVisible)) {
				this.setSwitchVisible(array.getBoolean(R.styleable.SwitchableCardView_switchVisible, false));
			}
		} finally {
			array.recycle();
		}
	}

	public View getDividerView() {
		return this.dividerView;
	}

	public boolean getSwitchState() {
		return this.switchView != null && this.switchView.isChecked();
	}

	public SwitchStateListener getSwitchStateListener() {
		return this.listener;
	}

	public SwitchCompat getSwitchView() {
		return this.switchView;
	}

	public boolean isSwitchDividerVisible() {
		return this.switchView != null && this.dividerView.getVisibility() == View.VISIBLE;
	}

	public boolean isSwitchVisible() {
		return this.switchView != null && this.switchView.getVisibility() == View.VISIBLE;
	}

	public void setSwitchDividerVisible(final boolean visible) {
		if(!visible && this.switchView == null) {
			return;
		}

		this.inflateSwitch();
		this.dividerView.setVisibility(visible ? View.VISIBLE : View.GONE);

		if(visible) {
			this.switchView.setVisibility(View.VISIBLE);
		}
	}

	public void setSwitchState(final boolean state) {
		this.inflateSwitch();
		this.switchView.setChecked(state);
	}

	public void setSwitchStateInstant(final boolean state) {
		this.inflateSwitch();
		this.switchView.setCheckedWithoutAnimation(state);
	}

	public void setSwitchStateListener(final SwitchStateListener listener) {
		this.listener = listener;
	}

	public void setSwitchVisible(final boolean visible) {
		if(!visible && this.switchView == null) {
			return;
		}

		this.inflateSwitch();
		this.switchView.setVisibility(visible ? View.VISIBLE : View.GONE);
	}

	private void inflateSwitch() {
		if(this.switchView != null) {
			return;
		}

		final Context context = this.getContext();
		this.switchView = new SwitchCompat(context);
		this.switchView.setClickable(true);
		this.switchView.setFocusable(true);
		final int switchViewIdentifier = View.generateViewId();
		this.switchView.setId(switchViewIdentifier);
		final Resources.Theme theme = context.getTheme();
		final TypedValue value = new TypedValue();
		theme.resolveAttribute(android.R.attr.listPreferredItemPaddingEnd, value, true);
		final Resources resources = context.getResources();
		final DisplayMetrics metrics = resources.getDisplayMetrics();
		this.switchView.setPaddingRelative(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12.0f, metrics)), 0, Math.round(value.getDimension(metrics)), 0);
		this.switchView.setOnCheckedChangeListener((switchView, state) -> {
			if(this.listener != null) {
				this.listener.switchStateChanged(this, state);
			}
		});

		final ConstraintLayout.LayoutParams switchViewParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
		final int constraintLayoutIdentifier = this.constraintLayout.getId();
		switchViewParams.bottomToBottom = constraintLayoutIdentifier;
		switchViewParams.endToEnd = constraintLayoutIdentifier;
		switchViewParams.topToTop = constraintLayoutIdentifier;
		this.constraintLayout.addView(this.switchView, switchViewParams);
		this.dividerView = new View(context);
		theme.resolveAttribute(androidx.appcompat.R.attr.switchDividerColor, value, true);
		this.dividerView.setBackgroundResource(value.resourceId);
		this.dividerView.setVisibility(View.GONE);
		@SuppressLint("PrivateResource")
		final ConstraintLayout.LayoutParams dividerViewParams = new ConstraintLayout.LayoutParams(resources.getDimensionPixelSize(androidx.appcompat.R.dimen.sesl_switch_divider_height), Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22.0f, metrics)));
		dividerViewParams.bottomToBottom = switchViewIdentifier;
		dividerViewParams.endToStart = switchViewIdentifier;
		dividerViewParams.topToTop = switchViewIdentifier;
		this.constraintLayout.addView(this.dividerView, dividerViewParams);
		final View endIconView = this.constraintLayout.findViewById(R.id.end_view);
		final ConstraintLayout.LayoutParams endIconViewParams = (ConstraintLayout.LayoutParams) endIconView.getLayoutParams();
		endIconViewParams.endToEnd = ConstraintLayout.LayoutParams.UNSET;
		endIconViewParams.endToStart = switchViewIdentifier;
		endIconView.setLayoutParams(endIconViewParams);
		this.setOnClickListener(view -> this.switchView.toggle());
	}

	@FunctionalInterface
	public interface SwitchStateListener {
		void switchStateChanged(final SwitchableCardView view, final boolean state);
	}
}
