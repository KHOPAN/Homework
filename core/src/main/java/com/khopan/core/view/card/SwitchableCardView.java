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

/**
 * A {@link com.khopan.core.view.card.CheckableCardView} that
 * includes a switch and a vertical switch divider.
 */
public class SwitchableCardView extends CheckableCardView {
	/**
	 * The switch view.
	 */
	protected SwitchCompat switchView;

	/**
	 * The {@link com.khopan.core.view.card.SwitchableCardView.SwitchStateListener}.
	 */
	protected SwitchStateListener listener;

	/**
	 * The divider view.
	 */
	protected View dividerView;

	/**
	 * Constructs a new {@link com.khopan.core.view.card.SwitchableCardView}.
	 *
	 * @param context the {@link android.content.Context}.
	 */
	public SwitchableCardView(@NonNull final Context context) {
		this(context, null, 0);
	}

	/**
	 * Constructs a new {@link com.khopan.core.view.card.SwitchableCardView}.
	 *
	 * @param context the {@link android.content.Context}.
	 * @param attributeSet the {@link android.util.AttributeSet}.
	 */
	public SwitchableCardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	/**
	 * Constructs a new {@link com.khopan.core.view.card.SwitchableCardView}.
	 *
	 * @param context the {@link android.content.Context}.
	 * @param attributeSet the {@link android.util.AttributeSet}.
	 * @param defaultStyleAttribute the default style attribute.
	 */
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

	/**
	 * @return the divider view.
	 */
	public View getDividerView() {
		return this.dividerView;
	}

	/**
	 * @return the switch state.
	 */
	public boolean getSwitchState() {
		return this.switchView != null && this.switchView.isChecked();
	}

	/**
	 * @return the {@link com.khopan.core.view.card.SwitchableCardView.SwitchStateListener}.
	 */
	public SwitchStateListener getSwitchStateListener() {
		return this.listener;
	}

	/**
	 * @return the {@link androidx.appcompat.widget.SwitchCompat}.
	 */
	public SwitchCompat getSwitchView() {
		return this.switchView;
	}

	/**
	 * @return whether or not the switch divider is visible.
	 */
	public boolean isSwitchDividerVisible() {
		return this.switchView != null && this.dividerView.getVisibility() == View.VISIBLE;
	}

	/**
	 * @return whether or not the switch is visible.
	 */
	public boolean isSwitchVisible() {
		return this.switchView != null && this.switchView.getVisibility() == View.VISIBLE;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);

		if(this.switchView != null) {
			this.switchView.setEnabled(enabled);
		}
	}

	/**
	 * Sets the visibility of the switch divider,
	 * or creates a new one if one doesn't exist.
	 *
	 * @param visible whether or not the switch divider
	 *        is visible.
	 */
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

	/**
	 * Sets the switch state.
	 *
	 * @param state the switch state.
	 */
	public void setSwitchState(final boolean state) {
		this.inflateSwitch();
		this.switchView.setChecked(state);
	}

	/**
	 * Instantly sets the switch state
	 * with no animation.
	 *
	 * @param state the switch state.
	 */
	public void setSwitchStateInstant(final boolean state) {
		this.inflateSwitch();
		this.switchView.setCheckedWithoutAnimation(state);
	}

	/**
	 * Sets the {@link com.khopan.core.view.card.SwitchableCardView.SwitchStateListener}.
	 *
	 * @param listener the {@link com.khopan.core.view.card.SwitchableCardView.SwitchStateListener}.
	 */
	public void setSwitchStateListener(final SwitchStateListener listener) {
		this.listener = listener;
	}

	/**
	 * Sets the visibility of the switch,
	 * or creates a new one if one doesn't exist.
	 *
	 * @param visible whether or not the switch
	 *        is visible.
	 */
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
		final int switchViewIdentifier = View.generateViewId();
		this.switchView.setId(switchViewIdentifier);
		this.switchView.setClickable(true);
		this.switchView.setEnabled(this.isEnabled());
		this.switchView.setFocusable(true);
		this.switchView.setOnCheckedChangeListener((switchView, state) -> {
			if(this.listener != null) {
				this.listener.switchStateChanged(this, state);
			}
		});

		final Resources.Theme theme = context.getTheme();
		final TypedValue value = new TypedValue();
		theme.resolveAttribute(android.R.attr.listPreferredItemPaddingEnd, value, true);
		final Resources resources = context.getResources();
		final DisplayMetrics metrics = resources.getDisplayMetrics();
		this.switchView.setPaddingRelative(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12.0f, metrics)), 0, resources.getDimensionPixelSize(value.resourceId), 0);
		final ConstraintLayout.LayoutParams switchViewParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
		switchViewParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
		switchViewParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
		switchViewParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
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
		final ConstraintLayout.LayoutParams endViewParams = (ConstraintLayout.LayoutParams) this.endView.getLayoutParams();
		endViewParams.endToEnd = ConstraintLayout.LayoutParams.UNSET;
		endViewParams.endToStart = switchViewIdentifier;
		this.endView.setLayoutParams(endViewParams);
		this.setOnClickListener(view -> this.switchView.toggle());
	}

	/**
	 * A listener for handling the switch state changes.
	 */
	@FunctionalInterface
	public interface SwitchStateListener {
		/**
		 * Handles the switch state changes.
		 *
		 * @param view the {@link com.khopan.core.view.card.SwitchableCardView}.
		 * @param state the new switch state.
		 */
		void switchStateChanged(final SwitchableCardView view, final boolean state);
	}
}
