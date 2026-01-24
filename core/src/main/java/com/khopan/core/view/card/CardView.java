package com.khopan.core.view.card;

import android.animation.LayoutTransition;
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
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.graphics.drawable.SeslRecoilDrawable;
import androidx.appcompat.util.SeslRoundedCorner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;

import com.khopan.core.CoreLayout;
import com.khopan.core.R;
import com.khopan.core.animator.TouchFeedbackAnimator;

import java.lang.reflect.Field;

import dev.oneuiproject.oneui.widget.RoundedLinearLayout;

public class CardView extends RoundedLinearLayout {
	public static final int CARD_LOCATION_TOP = 0x01;
	public static final int CARD_LOCATION_MIDDLE = 0x00;
	public static final int CARD_LOCATION_BOTTOM = 0x02;

	public static final int CARD_PREFERRED_DIVIDER_BOTTOM = 0x04;
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

	protected final ConstraintLayout constraintLayout;
	protected final TextView titleView;
	protected final TextView summaryView;

	protected boolean anchorDividersToTitle;
	protected ImageView endIconView;
	protected ImageView iconView;
	protected FrameLayout iconHolderView;
	protected DividerView bottomDividerView;
	protected DividerView topDividerView;

	public CardView(@NonNull final Context context) {
		this(context, null, 0);
	}

	public CardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	@SuppressLint("ClickableViewAccessibility")
	public CardView(@NonNull final Context context, @Nullable final AttributeSet attributeSet, final int defaultStyleAttribute) {
		super(context, attributeSet, defaultStyleAttribute);
		this.setOrientation(RoundedLinearLayout.VERTICAL);
		final View view = LayoutInflater.from(context).inflate(R.layout.view_card, this, true);
		this.constraintLayout = view.findViewById(R.id.constraint_layout);
		final TouchFeedbackAnimator animator = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ? new TouchFeedbackAnimator(this.constraintLayout) : null;
		this.constraintLayout.setOnTouchListener((layout, event) -> {
			if(this.isEnabled() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
				animator.animate(event);
			}

			return false;
		});

		this.titleView = view.findViewById(R.id.title_view);
		this.summaryView = view.findViewById(R.id.summary_view);
		final ValueAnimator.AnimatorUpdateListener listener = (animation) -> {
			if(this.bottomDividerView != null) {
				this.bottomDividerView.invalidate();
			}

			if(this.topDividerView != null) {
				this.topDividerView.invalidate();
			}
		};

		for(int i = LayoutTransition.CHANGE_APPEARING; i <= LayoutTransition.CHANGING; i++) {
			final ValueAnimator animation = (ValueAnimator) this.constraintLayout.getLayoutTransition().getAnimator(i);

			if(animation != null) {
				animation.addUpdateListener(listener);
			}
		}

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
				this.setEndIconTint(array.getColor(R.styleable.CardView_endIconTint, 0));
			}

			if(array.hasValue(R.styleable.CardView_iconTint)) {
				this.setIconTint(array.getColor(R.styleable.CardView_iconTint, 0));
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

	public boolean areDividersAnchoredToTitle() {
		return this.anchorDividersToTitle;
	}

	public Drawable getEndIcon() {
		return this.endIconView == null ? null : this.endIconView.getDrawable();
	}

	public ImageView getEndIconView() {
		return this.endIconView;
	}

	public Drawable getIcon() {
		return this.iconView == null ? null : this.iconView.getDrawable();
	}

	public ImageView getIconView() {
		return this.iconView;
	}

	public CharSequence getSummary() {
		return this.summaryView.getText();
	}

	public TextView getSummaryView() {
		return this.summaryView;
	}

	public CharSequence getTitle() {
		return this.titleView.getText();
	}

	public TextView getTitleView() {
		return this.titleView;
	}

	public boolean isBottomDividerVisible() {
		return this.bottomDividerView != null && this.bottomDividerView.getVisibility() == View.VISIBLE;
	}

	public boolean isTopDividerVisible() {
		return this.topDividerView != null && this.topDividerView.getVisibility() == View.VISIBLE;
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

	public void setAnchorDividersToTitle(final boolean anchor) {
		this.anchorDividersToTitle = anchor;

		if(this.bottomDividerView != null && this.bottomDividerView.getVisibility() == View.VISIBLE) {
			this.bottomDividerView.invalidate();
		}

		if(this.topDividerView != null && this.topDividerView.getVisibility() == View.VISIBLE) {
			this.topDividerView.invalidate();
		}
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

	public void setBottomDividerVisible(final boolean visible) {
		if(this.bottomDividerView != null) {
			this.bottomDividerView.setVisibility(visible ? View.VISIBLE : View.GONE);
			return;
		}

		if(visible) {
			this.addView(this.bottomDividerView = new DividerView(), this.getChildCount());
		}
	}

	public void setCardLocation(final int location) {
		final int locationFlag = location & 0b11;
		final boolean bottomDivider = (location & CardView.CARD_PREFERRED_DIVIDER_BOTTOM) != 0;
		this.setRoundedCorners((locationFlag == CardView.CARD_LOCATION_TOP ? SeslRoundedCorner.ROUNDED_CORNER_TOP_LEFT | SeslRoundedCorner.ROUNDED_CORNER_TOP_RIGHT : 0) | (locationFlag == CardView.CARD_LOCATION_BOTTOM ? SeslRoundedCorner.ROUNDED_CORNER_BOTTOM_LEFT | SeslRoundedCorner.ROUNDED_CORNER_BOTTOM_RIGHT : 0));
		this.setBottomDividerVisible(bottomDivider && locationFlag != CardView.CARD_LOCATION_BOTTOM);
		this.setTopDividerVisible(!bottomDivider && locationFlag != CardView.CARD_LOCATION_TOP);
	}

	public void setCardLocation(final int index, final int size) {
		this.setCardLocation(index, size, CardView.CARD_PREFERRED_DIVIDER_TOP);
	}

	public void setCardLocation(final int index, final int size, final int preferredDivider) {
		this.setCardLocation((index == 0 ? CardView.CARD_LOCATION_TOP : index == size - 1 ? CardView.CARD_LOCATION_BOTTOM : CardView.CARD_LOCATION_MIDDLE) | preferredDivider);
	}

	public void setEndIcon(final Drawable icon) {
		this.inflateEndIconView();
		this.endIconView.setImageDrawable(icon);
		this.endIconView.setVisibility(icon == null ? View.GONE : View.VISIBLE);
	}

	public void setEndIconTint(final int tint) {
		this.inflateEndIconView();
		final Drawable drawable = this.endIconView.getDrawable();

		if(drawable != null) {
			DrawableCompat.setTint(drawable, tint);
		}
	}

	public void setIcon(final Drawable icon) {
		this.inflateIconView();
		this.iconView.setImageDrawable(icon);
		final int visibility = icon == null ? View.GONE : View.VISIBLE;
		this.iconView.setVisibility(visibility);
		this.iconHolderView.setVisibility(visibility);
	}

	public void setIconTint(final int tint) {
		this.inflateIconView();
		final Drawable drawable = this.iconView.getDrawable();

		if(drawable != null) {
			DrawableCompat.setTint(drawable, tint);
		}
	}

	@Override
	public void setOnClickListener(@Nullable final OnClickListener listener) {
		this.constraintLayout.setOnClickListener(listener);
	}

	@Override
	public void setOnLongClickListener(@Nullable final OnLongClickListener listener) {
		this.constraintLayout.setOnLongClickListener(listener);
	}

	public void setSummary(final CharSequence summary) {
		this.summaryView.setText(summary);
		this.summaryView.setVisibility(summary == null ? View.GONE : View.VISIBLE);
	}

	public void setTitle(final CharSequence title) {
		this.titleView.setText(title);
	}

	public void setTopDividerVisible(final boolean visible) {
		if(this.topDividerView != null) {
			this.topDividerView.setVisibility(visible ? View.VISIBLE : View.GONE);
			return;
		}

		if(visible) {
			this.addView(this.topDividerView = new DividerView(), 0);
		}
	}

	private void inflateEndIconView() {
		if(this.endIconView == null) {
			this.endIconView = (ImageView) this.constraintLayout.<ViewStub>findViewById(R.id.end_view).inflate();
			this.endIconView.setVisibility(View.GONE);
		}
	}

	private void inflateIconView() {
		if(this.iconView == null) {
			CoreLayout.forceRemeasure(this.constraintLayout, () -> {
				this.iconHolderView = (FrameLayout) this.constraintLayout.<ViewStub>findViewById(R.id.icon_view).inflate();
				this.iconHolderView.setVisibility(View.VISIBLE);
				this.iconView = this.iconHolderView.findViewById(dev.oneuiproject.oneui.design.R.id.cardview_icon);
				this.iconView.setVisibility(View.VISIBLE);
			}, () -> {
				this.iconHolderView.setVisibility(View.GONE);
				this.iconView.setVisibility(View.GONE);
			});
		}
	}

	protected class DividerView extends View {
		private final Paint paint;
		private final int[] locations;

		private DividerView() {
			super(CardView.this.getContext());
			final Context context = CardView.this.getContext();
			final Resources resources = context.getResources();
			@SuppressLint("PrivateResource")
			final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(androidx.appcompat.R.dimen.sesl_list_divider_height));
			final Resources.Theme theme = context.getTheme();
			final TypedValue value = new TypedValue();
			theme.resolveAttribute(androidx.appcompat.R.attr.listPreferredItemPaddingEnd, value, true);
			final DisplayMetrics metrics = resources.getDisplayMetrics();
			params.setMarginEnd(Math.round(value.getDimension(metrics)));
			theme.resolveAttribute(androidx.appcompat.R.attr.listPreferredItemPaddingStart, value, true);
			params.setMarginStart(Math.round(value.getDimension(metrics)));
			this.setLayoutParams(params);
			this.paint = new Paint();
			theme.resolveAttribute(androidx.appcompat.R.attr.listDividerColor, value, true);
			this.paint.setColor(resources.getColor(value.resourceId, theme));
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
			canvas.drawRect(CardView.this.anchorDividersToTitle && !rightToLeft ? offset : 0.0f, 0.0f, CardView.this.anchorDividersToTitle && rightToLeft ? offset + CardView.this.titleView.getWidth() : this.getWidth(), this.getHeight(), this.paint);
		}
	}
}
