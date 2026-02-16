package com.khopan.core.view.card;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.graphics.drawable.SeslRecoilDrawable;
import androidx.appcompat.util.SeslRoundedCorner;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;

import com.khopan.core.R;
import com.khopan.core.animator.TouchFeedbackAnimator;

import java.lang.reflect.Field;

import dev.oneuiproject.oneui.widget.RoundedLinearLayout;

/**
 * A {@link android.view.View} that displays
 * a card item with a title, summary, icon,
 * and dividers. It is designed to be used
 * as a row item in a list or similar container.
 */
public class CardView extends RoundedLinearLayout {
	/**
	 * Constant representing the top
	 * card location.
	 */
	public static final int CARD_LOCATION_TOP = 0x01;

	/**
	 * Constant representing the middle
	 * card location.
	 */
	public static final int CARD_LOCATION_MIDDLE = 0x00;

	/**
	 * Constant representing the bottom
	 * card location.
	 */
	public static final int CARD_LOCATION_BOTTOM = 0x02;

	/**
	 * Constant representing the bottom
	 * divider is preferred.
	 */
	public static final int CARD_PREFERRED_DIVIDER_BOTTOM = 0x04;

	/**
	 * Constant representing the top
	 * divider is preferred.
	 */
	public static final int CARD_PREFERRED_DIVIDER_TOP = 0x00;

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

	/**
	 * The end icon view.
	 */
	public final AppCompatImageView endView;

	/**
	 * The icon view.
	 */
	public final AppCompatImageView iconView;

	/**
	 * The {@link androidx.constraintlayout.widget.ConstraintLayout}.
	 */
	public final ConstraintLayout constraintLayout;

	/**
	 * The spacer view.
	 */
	public final Space spacerView;

	/**
	 * The summary view.
	 */
	public final TextView summaryView;

	/**
	 * The title view.
	 */
	public final TextView titleView;

	/**
	 * Whether or not the dividers are
	 * anchored to the title view.
	 */
	protected boolean anchorDividersToTitle;

	/**
	 * The bottom divider view.
	 */
	protected DividerView bottomDividerView;

	/**
	 * The top divider view.
	 */
	protected DividerView topDividerView;

	private final int dividerHeight;
	private final int paddingEnd;
	private final int paddingStart;
	private final Paint paint;

	/**
	 * Constructs a new {@link com.khopan.core.view.card.CardView}.
	 *
	 * @param context the {@link android.content.Context}.
	 */
	public CardView(@NonNull final Context context) {
		this(context, null, 0);
	}

	/**
	 * Constructs a new {@link com.khopan.core.view.card.CardView}.
	 *
	 * @param context the {@link android.content.Context}.
	 * @param attributeSet the {@link android.util.AttributeSet}.
	 */
	public CardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	/**
	 * Constructs a new {@link com.khopan.core.view.card.CardView}.
	 *
	 * @param context the {@link android.content.Context}.
	 * @param attributeSet the {@link android.util.AttributeSet}.
	 * @param defaultStyleAttribute the default style attribute.
	 */
	@SuppressLint({"PrivateResource", "ClickableViewAccessibility"})
	public CardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet, final int defaultStyleAttribute) {
		super(context, attributeSet, defaultStyleAttribute);
		this.setOrientation(RoundedLinearLayout.VERTICAL);
		this.paint = new Paint();
		final Resources.Theme theme = context.getTheme();
		final TypedValue value = new TypedValue();
		theme.resolveAttribute(androidx.appcompat.R.attr.listDividerColor, value, true);
		final Resources resources = context.getResources();
		this.paint.setColor(resources.getColor(value.resourceId, theme));
		this.dividerHeight = resources.getDimensionPixelSize(androidx.appcompat.R.dimen.sesl_list_divider_height);
		final int iconViewIdentifier = View.generateViewId();
		final int titleViewIdentifier = View.generateViewId();
		final int summaryViewIdentifier = View.generateViewId();
		final int spacerViewIdentifier = View.generateViewId();
		final int endViewIdentifier = View.generateViewId();
		this.constraintLayout = new ConstraintLayout(context);
		this.constraintLayout.setClickable(true);
		this.constraintLayout.setFocusable(true);
		final TouchFeedbackAnimator animator = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ? new TouchFeedbackAnimator(this.constraintLayout) : null;
		this.constraintLayout.setOnTouchListener((layout, event) -> {
			if(this.isEnabled() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
				animator.animate(event);
			}

			return false;
		});

		theme.resolveAttribute(androidx.appcompat.R.attr.listChoiceBackgroundIndicator, value, true);
		this.constraintLayout.setForeground(AppCompatResources.getDrawable(context, value.resourceId));
		theme.resolveAttribute(androidx.appcompat.R.attr.listPreferredItemPaddingStart, value, true);
		this.constraintLayout.setPaddingRelative(this.paddingStart = resources.getDimensionPixelSize(value.resourceId), 0, 0, 0);
		this.iconView = new AppCompatImageView(context);
		this.iconView.setId(iconViewIdentifier);
		this.iconView.setPaddingRelative(0, 0, resources.getDimensionPixelSize(dev.oneuiproject.oneui.design.R.dimen.oui_des_cardview_icon_margin_end), 0);
		this.iconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		this.iconView.setVisibility(View.GONE);
		final int iconViewSize = resources.getDimensionPixelSize(dev.oneuiproject.oneui.design.R.dimen.oui_des_cardview_icon_size);
		final ConstraintLayout.LayoutParams iconViewParams = new ConstraintLayout.LayoutParams(iconViewSize, iconViewSize);
		iconViewParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
		iconViewParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
		iconViewParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
		this.constraintLayout.addView(this.iconView, iconViewParams);
		this.titleView = new TextView(context);
		this.titleView.setId(titleViewIdentifier);
		this.titleView.getViewTreeObserver().addOnPreDrawListener(() -> {
			if(this.bottomDividerView != null) {
				this.bottomDividerView.invalidate();
			}

			if(this.topDividerView != null) {
				this.topDividerView.invalidate();
			}

			return true;
		});

		this.titleView.setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_START);
		theme.resolveAttribute(androidx.appcompat.R.attr.textAppearanceListItem, value, true);
		this.titleView.setTextAppearance(value.resourceId);
		final ConstraintLayout.LayoutParams titleViewParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		titleViewParams.bottomToTop = summaryViewIdentifier;
		titleViewParams.constrainedWidth = true;
		titleViewParams.endToStart = endViewIdentifier;
		titleViewParams.horizontalBias = 0.0f;
		titleViewParams.startToEnd = iconViewIdentifier;
		titleViewParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
		final int marginTop = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14.0f, resources.getDisplayMetrics()));
		titleViewParams.topMargin = marginTop;
		titleViewParams.verticalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED;
		final int marginEnd = resources.getDimensionPixelSize(androidx.preference.R.dimen.sesl_preference_dot_frame_size);
		titleViewParams.setMarginEnd(marginEnd);
		this.constraintLayout.addView(this.titleView, titleViewParams);
		this.summaryView = new TextView(context);
		this.summaryView.setId(summaryViewIdentifier);
		this.summaryView.setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_START);
		theme.resolveAttribute(android.R.attr.textAppearanceSmall, value, true);
		this.summaryView.setTextAppearance(value.resourceId);
		this.summaryView.setVisibility(View.GONE);
		final ConstraintLayout.LayoutParams summaryViewParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		summaryViewParams.bottomToTop = spacerViewIdentifier;
		summaryViewParams.constrainedWidth = true;
		summaryViewParams.endToStart = endViewIdentifier;
		summaryViewParams.horizontalBias = 0.0f;
		summaryViewParams.startToStart = titleViewIdentifier;
		summaryViewParams.topToBottom = titleViewIdentifier;
		summaryViewParams.setMarginEnd(marginEnd);
		this.constraintLayout.addView(this.summaryView, summaryViewParams);
		this.spacerView	= new Space(context);
		this.spacerView.setId(spacerViewIdentifier);
		final ConstraintLayout.LayoutParams spacerViewParams = new ConstraintLayout.LayoutParams(0, marginTop);
		spacerViewParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
		spacerViewParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
		spacerViewParams.topToBottom = summaryViewIdentifier;
		this.constraintLayout.addView(this.spacerView, spacerViewParams);
		this.endView = new AppCompatImageView(context);
		this.endView.setId(endViewIdentifier);
		this.endView.setVisibility(View.GONE);
		final ConstraintLayout.LayoutParams endViewParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		endViewParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
		endViewParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
		endViewParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
		theme.resolveAttribute(androidx.appcompat.R.attr.listPreferredItemPaddingEnd, value, true);
		endViewParams.setMarginEnd(this.paddingEnd = resources.getDimensionPixelSize(value.resourceId));
		this.constraintLayout.addView(this.endView, endViewParams);
		this.addView(this.constraintLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		final TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.CardView, defaultStyleAttribute, 0);

		try {
			this.setAnchorDividersToTitle(array.getBoolean(R.styleable.CardView_anchorDividersToTitle, true));
			int roundedCorners = 0;
			boolean bottomDividerEnabled = false;
			boolean topDividerEnabled = true;

			if(array.hasValue(R.styleable.CardView_bottomDividerVisible)) {
				bottomDividerEnabled = array.getBoolean(R.styleable.CardView_bottomDividerVisible, true);
			}

			if(array.hasValue(R.styleable.CardView_topDividerVisible)) {
				topDividerEnabled = array.getBoolean(R.styleable.CardView_topDividerVisible, true);
			}

			if(array.hasValue(R.styleable.CardView_cardLocation)) {
				final int location = array.getInt(R.styleable.CardView_cardLocation, CardView.CARD_LOCATION_MIDDLE);
				final int locationFlag = location & 0b11;
				final boolean bottomDivider = (location & CardView.CARD_PREFERRED_DIVIDER_BOTTOM) != 0;
				roundedCorners = (locationFlag == CardView.CARD_LOCATION_TOP ? SeslRoundedCorner.ROUNDED_CORNER_TOP_LEFT | SeslRoundedCorner.ROUNDED_CORNER_TOP_RIGHT : 0) | (locationFlag == CardView.CARD_LOCATION_BOTTOM ? SeslRoundedCorner.ROUNDED_CORNER_BOTTOM_LEFT | SeslRoundedCorner.ROUNDED_CORNER_BOTTOM_RIGHT : 0);
				bottomDividerEnabled = bottomDivider && locationFlag != CardView.CARD_LOCATION_BOTTOM;
				topDividerEnabled = !bottomDivider && locationFlag != CardView.CARD_LOCATION_TOP;
			}

			this.setRoundedCorners(roundedCorners);
			this.setBottomDividerVisible(bottomDividerEnabled);
			this.setTopDividerVisible(topDividerEnabled);

			if(array.hasValue(R.styleable.CardView_endIcon)) {
				this.setEndIcon(array.getDrawable(R.styleable.CardView_endIcon));
			}

			if(array.hasValue(R.styleable.CardView_icon)) {
				this.setIcon(array.getDrawable(R.styleable.CardView_icon));
			}

			if(array.hasValue(R.styleable.CardView_endIconTint)) {
				CardView.setTint(this.endView.getDrawable(), array.getColor(R.styleable.CardView_endIconTint, 0));
			}

			if(array.hasValue(R.styleable.CardView_iconTint)) {
				CardView.setTint(this.iconView.getDrawable(), array.getColor(R.styleable.CardView_iconTint, 0));
			}

			if(array.hasValue(R.styleable.CardView_summary)) {
				this.setSummary(array.getString(R.styleable.CardView_summary));
			}

			if(array.hasValue(R.styleable.CardView_title)) {
				this.setTitle(array.getString(R.styleable.CardView_title));
			}
		} finally {
			array.recycle();
		}
	}

	/**
	 * @return whether or not the dividers are
	 *         anchored to the title view.
	 */
	public boolean areDividersAnchoredToTitle() {
		return this.anchorDividersToTitle;
	}

	/**
	 * @return the bottom divider {@link android.view.View}.
	 */
	public View getBottomDividerView() {
		return this.bottomDividerView;
	}

	/**
	 * @return the end icon.
	 */
	public Drawable getEndIcon() {
		return this.endView == null ? null : this.endView.getDrawable();
	}

	/**
	 * @return the icon.
	 */
	public Drawable getIcon() {
		return this.iconView == null ? null : this.iconView.getDrawable();
	}

	/**
	 * @return the summary text.
	 */
	public CharSequence getSummary() {
		return this.summaryView.getText();
	}

	/**
	 * @return the title text.
	 */
	public CharSequence getTitle() {
		return this.titleView.getText();
	}

	/**
	 * @return the top divider {@link android.view.View}.
	 */
	public View getTopDividerView() {
		return this.topDividerView;
	}

	@Override
	public boolean hasOnClickListeners() {
		return this.constraintLayout.hasOnClickListeners();
	}

	@RequiresApi(Build.VERSION_CODES.R)
	@Override
	public boolean hasOnLongClickListeners() {
		return this.constraintLayout.hasOnLongClickListeners();
	}

	/**
	 * @return whether or not the bottom divider is visible.
	 */
	public boolean isBottomDividerVisible() {
		return this.bottomDividerView != null && this.bottomDividerView.getVisibility() == View.VISIBLE;
	}

	/**
	 * @return whether or not the top divider is visible.
	 */
	public boolean isTopDividerVisible() {
		return this.topDividerView != null && this.topDividerView.getVisibility() == View.VISIBLE;
	}

	/**
	 * Resets the foreground state and the animations.
	 */
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

	/**
	 * Sets whether or not the dividers will be
	 * anchored to the title view. This can be useful,
	 * for example, when using
	 * {@link android.animation.LayoutTransition}; the
	 * dividers will animate smoothly along with the
	 * title view.
	 *
	 * @param anchor whether or not to anchor the
	 *        dividers to the title view.
	 */
	public void setAnchorDividersToTitle(final boolean anchor) {
		this.anchorDividersToTitle = anchor;

		if(this.bottomDividerView != null && this.bottomDividerView.getVisibility() == View.VISIBLE) {
			this.bottomDividerView.invalidate();
		}

		if(this.topDividerView != null && this.topDividerView.getVisibility() == View.VISIBLE) {
			this.topDividerView.invalidate();
		}
	}

	/**
	 * Sets the visibility of the bottom divider,
	 * or creates a new one if one doesn't exist.
	 *
	 * @param visible whether or not the bottom divider
	 *        is visible.
	 */
	public void setBottomDividerVisible(final boolean visible) {
		if(this.bottomDividerView != null) {
			this.bottomDividerView.setVisibility(visible ? View.VISIBLE : View.GONE);
			return;
		}

		if(visible) {
			this.addView(this.bottomDividerView = new DividerView(), this.getChildCount());
		}
	}

	/**
	 * Sets the card location.
	 *
	 * @param location the location. Can be one of {@link #CARD_LOCATION_TOP},
	 *        {@link #CARD_LOCATION_MIDDLE}, or {@link #CARD_LOCATION_BOTTOM},
	 *        masked with one of {@link #CARD_PREFERRED_DIVIDER_BOTTOM} or
	 *        {@link #CARD_PREFERRED_DIVIDER_TOP}.
	 */
	public void setCardLocation(final int location) {
		final int locationFlag = location & 0b11;
		final boolean bottomDivider = (location & CardView.CARD_PREFERRED_DIVIDER_BOTTOM) != 0;
		this.setRoundedCorners((locationFlag == CardView.CARD_LOCATION_TOP ? SeslRoundedCorner.ROUNDED_CORNER_TOP_LEFT | SeslRoundedCorner.ROUNDED_CORNER_TOP_RIGHT : 0) | (locationFlag == CardView.CARD_LOCATION_BOTTOM ? SeslRoundedCorner.ROUNDED_CORNER_BOTTOM_LEFT | SeslRoundedCorner.ROUNDED_CORNER_BOTTOM_RIGHT : 0));
		this.setBottomDividerVisible(bottomDivider && locationFlag != CardView.CARD_LOCATION_BOTTOM);
		this.setTopDividerVisible(!bottomDivider && locationFlag != CardView.CARD_LOCATION_TOP);
	}

	/**
	 * Sets the card location. This method assumes this
	 * {@link com.khopan.core.view.card.CardView} is on a list.
	 * This method prefers the top divider.
	 *
	 * @param index the index on the list.
	 * @param size the size of the list.
	 */
	public void setCardLocation(final int index, final int size) {
		this.setCardLocation(index, size, CardView.CARD_PREFERRED_DIVIDER_TOP);
	}

	/**
	 * Sets the card location. This method assumes this
	 * {@link com.khopan.core.view.card.CardView} is on a list.
	 *
	 * @param index the index on the list.
	 * @param size the size of the list.
	 * @param preferredDivider the preferred divider location.
	 *        Can be one of {@link #CARD_PREFERRED_DIVIDER_BOTTOM}
	 *        or {@link #CARD_PREFERRED_DIVIDER_TOP}.
	 */
	public void setCardLocation(final int index, final int size, final int preferredDivider) {
		this.setCardLocation((index == 0 ? CardView.CARD_LOCATION_TOP : index == size - 1 ? CardView.CARD_LOCATION_BOTTOM : CardView.CARD_LOCATION_MIDDLE) | preferredDivider);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		this.constraintLayout.setEnabled(enabled);
		this.constraintLayout.setAlpha(enabled ? 1.0f : 0.4f);
	}

	/**
	 * Sets the end icon.
	 *
	 * @param icon the end icon.
	 */
	public void setEndIcon(final Drawable icon) {
		this.endView.setImageDrawable(icon);
		this.endView.setVisibility(icon == null ? View.GONE : View.VISIBLE);
	}

	/**
	 * Sets the icon.
	 *
	 * @param icon the icon.
	 */
	public void setIcon(final Drawable icon) {
		this.iconView.setImageDrawable(icon);
		this.iconView.setVisibility(icon == null ? View.GONE : View.VISIBLE);
	}

	@Override
	public void setOnClickListener(@Nullable final OnClickListener listener) {
		this.constraintLayout.setOnClickListener(listener);
	}

	@Override
	public void setOnLongClickListener(@Nullable final OnLongClickListener listener) {
		this.constraintLayout.setOnLongClickListener(listener);
	}

	/**
	 * Sets the summary text.
	 *
	 * @param summary the summary text.
	 */
	public void setSummary(final CharSequence summary) {
		this.summaryView.setText(summary);
		this.summaryView.setVisibility(summary == null ? View.GONE : View.VISIBLE);
	}

	/**
	 * Sets the title text.
	 *
	 * @param title the title text.
	 */
	public void setTitle(final CharSequence title) {
		this.titleView.setText(title);
	}

	/**
	 * Sets the visibility of the top divider,
	 * or creates a new one if one doesn't exist.
	 *
	 * @param visible whether or not the top divider
	 *        is visible.
	 */
	public void setTopDividerVisible(final boolean visible) {
		if(this.topDividerView != null) {
			this.topDividerView.setVisibility(visible ? View.VISIBLE : View.GONE);
			return;
		}

		if(visible) {
			this.addView(this.topDividerView = new DividerView(), 0);
		}
	}

	private static void setTint(final Drawable drawable, final int tint) {
		if(drawable != null) {
			DrawableCompat.setTint(drawable, tint);
		}
	}

	/**
	 * A custom {@link android.view.View} used for rendering
	 * the {@link com.khopan.core.view.card.CardView} dividers.
	 */
	protected class DividerView extends View {
		private final int[] locations;

		private DividerView() {
			super(CardView.this.getContext());
			final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CardView.this.dividerHeight);
			params.setMarginEnd(CardView.this.paddingEnd);
			params.setMarginStart(CardView.this.paddingStart);
			this.setLayoutParams(params);
			this.locations = new int[2];
		}

		@Override
		protected void onDraw(@NonNull final Canvas canvas) {
			final int offset;

			if(CardView.this.anchorDividersToTitle) {
				CardView.this.titleView.getLocationOnScreen(this.locations);
				final int titleView = this.locations[0];
				this.getLocationOnScreen(this.locations);
				offset = titleView - this.locations[0];
			} else {
				offset = 0;
			}

			final boolean rightToLeft = this.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
			canvas.drawRect(CardView.this.anchorDividersToTitle && !rightToLeft ? offset : 0.0f, 0.0f, CardView.this.anchorDividersToTitle && rightToLeft ? offset + CardView.this.titleView.getWidth() : this.getWidth(), this.getHeight(), CardView.this.paint);
		}
	}
}
