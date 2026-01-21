package com.khopan.core.view.card;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.graphics.drawable.SeslRecoilDrawable;
import androidx.appcompat.util.SeslRoundedCorner;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.khopan.core.R;
import com.khopan.core.animator.TouchFeedbackAnimator;

import java.lang.reflect.Field;

import dev.oneuiproject.oneui.widget.RoundedLinearLayout;

public class CardView extends RoundedLinearLayout {
	public static final int CARD_LOCATION_TOP = 0;
	public static final int CARD_LOCATION_MIDDLE = 1;
	public static final int CARD_LOCATION_BOTTOM = 2;

	private static final Field RECOIL_ANIMATOR_FIELD;

	static {
		Field field;

		try {
			field = SeslRecoilDrawable.class.getDeclaredField("mAnimator");
			field.setAccessible(true);
		} catch(final Throwable ignored) {
			field = null;
		}

		RECOIL_ANIMATOR_FIELD = field;
	}

	private final LayoutInflater inflater;
	private final ConstraintLayout constraintLayout;
	private final TextView titleView;
	private final TextView summaryView;
	private final TouchFeedbackAnimator animator;

	private View topDividerView;

	public CardView(@NonNull final Context context) {
		this(context, null, 0);
	}

	public CardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	public CardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet, final int defaultStyleAttribute) {
		super(context, attributeSet, defaultStyleAttribute);
		this.setOrientation(RoundedLinearLayout.VERTICAL);
		this.inflater = LayoutInflater.from(context);
		final View view = this.inflater.inflate(R.layout.view_card, this, true);
		this.constraintLayout = view.findViewById(R.id.constraint_layout);
		this.titleView = view.findViewById(R.id.title_view);
		this.summaryView = view.findViewById(R.id.summary_view);
		this.animator = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ? new TouchFeedbackAnimator(context, this, this.constraintLayout) : null;
		this.parseAttributes(context, attributeSet, defaultStyleAttribute);
	}

	@Override
	public boolean dispatchTouchEvent(final MotionEvent event) {
		if(this.isEnabled() && Build.VERSION.SDK_INT >= 29) {
			this.animator.animate(event);
		}

		return super.dispatchTouchEvent(event);
	}

	public CharSequence getSummary() {
		return this.summaryView.getText();
	}

	public CharSequence getTitle() {
		return this.titleView.getText();
	}

	public boolean isTopDividerEnabled() {
		return this.topDividerView != null && this.topDividerView.getVisibility() == View.VISIBLE;
	}

	public void resetForegroundState() {
		final Drawable foreground = this.constraintLayout.getForeground();

		if(foreground instanceof SeslRecoilDrawable) {
			try {
				final ValueAnimator animator = (ValueAnimator) CardView.RECOIL_ANIMATOR_FIELD.get(foreground);

				if(animator != null) {
					animator.cancel();
					animator.setFloatValues(0.0f, 0.0f);
					animator.setCurrentFraction(0.0f);
				}
			} catch(final Throwable ignored) {}
		}
	}

	public void setCardLocation(final int location) {
		this.setRoundedCorners((location == CardView.CARD_LOCATION_TOP ? SeslRoundedCorner.ROUNDED_CORNER_TOP_LEFT | SeslRoundedCorner.ROUNDED_CORNER_TOP_RIGHT : 0) | (location == CardView.CARD_LOCATION_BOTTOM ? SeslRoundedCorner.ROUNDED_CORNER_BOTTOM_LEFT | SeslRoundedCorner.ROUNDED_CORNER_BOTTOM_RIGHT : 0));
		this.setTopDividerEnabled(location != CardView.CARD_LOCATION_TOP);
	}

	public void setCardLocation(final int index, final int size) {
		this.setCardLocation(index == 0 ? CardView.CARD_LOCATION_TOP : index == size - 1 ? CardView.CARD_LOCATION_BOTTOM : CardView.CARD_LOCATION_MIDDLE);
	}

	public void setSummary(final CharSequence summary) {
		this.summaryView.setText(summary);
		this.summaryView.setVisibility(summary == null ? View.GONE : View.VISIBLE);
	}

	public void setTitle(final CharSequence title) {
		this.titleView.setText(title);
	}

	public void setTopDividerEnabled(final boolean enabled) {
		if(this.topDividerView != null) {
			this.topDividerView.setVisibility(enabled ? View.VISIBLE : View.GONE);
			return;
		}

		if(enabled) {
			this.addView(this.topDividerView = this.inflater.inflate(dev.oneuiproject.oneui.design.R.layout.oui_des_widget_card_item_divider, this, false), 0);
			this.topDividerView.setVisibility(View.VISIBLE);
		}
	}

	protected void parseAttributes(final Context context, final AttributeSet attributeSet, final int defaultStyleAttribute) {
		final TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.CardView, defaultStyleAttribute, 0);

		try {
			int roundedCorners = 0;
			boolean topDividerEnabled = true;

			if(array.hasValue(R.styleable.CardView_topDividerEnabled)) {
				topDividerEnabled = array.getBoolean(R.styleable.CardView_topDividerEnabled, true);
			}

			if(array.hasValue(R.styleable.CardView_cardLocation)) {
				final int location = array.getInt(R.styleable.CardView_cardLocation, CardView.CARD_LOCATION_MIDDLE);
				roundedCorners = (location == CardView.CARD_LOCATION_TOP ? SeslRoundedCorner.ROUNDED_CORNER_TOP_LEFT | SeslRoundedCorner.ROUNDED_CORNER_TOP_RIGHT : 0) | (location == CardView.CARD_LOCATION_BOTTOM ? SeslRoundedCorner.ROUNDED_CORNER_BOTTOM_LEFT | SeslRoundedCorner.ROUNDED_CORNER_BOTTOM_RIGHT : 0);
				topDividerEnabled = location != CardView.CARD_LOCATION_TOP;
			}

			this.setRoundedCorners(roundedCorners);
			this.setSummary(array.getString(R.styleable.CardView_summary));
			this.setTitle(array.getString(R.styleable.CardView_title));
			this.setTopDividerEnabled(topDividerEnabled);
		} finally {
			array.recycle();
		}
	}
}
