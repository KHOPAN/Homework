package com.khopan.core.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.Objects;

/**
 * A resizable {@link android.graphics.drawable.Drawable} wrapper.
 */
public class ResizableDrawable extends Drawable implements Drawable.Callback {
	private final Drawable drawable;

	private int width;
	private int height;

	/**
	 * Constructs a new {@link com.khopan.core.drawable.ResizableDrawable} instance.
	 *
	 * @param drawable the {@link android.graphics.drawable.Drawable}.
	 */
	public ResizableDrawable(final Drawable drawable) {
		this.drawable = Objects.requireNonNull(drawable);
		this.drawable.setCallback(this);
		this.width = -1;
		this.height = -1;
	}

	/**
	 * Constructs a new {@link com.khopan.core.drawable.ResizableDrawable} instance.
	 *
	 * @param context the {@link android.content.Context}
	 * @param drawable the {@link android.graphics.drawable.Drawable} resource.
	 */
	public ResizableDrawable(final Context context, @DrawableRes final int drawable) {
		this(AppCompatResources.getDrawable(context, drawable));
	}

	@Override
	public void draw(@NonNull final Canvas canvas) {
		this.drawable.draw(canvas);
	}

	@Override
	public int getIntrinsicHeight() {
		return this.height < 0 ? this.drawable.getIntrinsicHeight() : this.height;
	}

	@Override
	public int getIntrinsicWidth() {
		return this.width < 0 ? this.drawable.getIntrinsicWidth() : this.width;
	}

	@Override
	public int getOpacity() {
		return this.drawable.getOpacity();
	}

	@Override
	public void invalidateDrawable(@NonNull final Drawable drawable) {
		this.invalidateSelf();
	}

	@Override
	public boolean isStateful() {
		return this.drawable.isStateful();
	}

	@Override
	public void scheduleDrawable(@NonNull final Drawable drawable, @NonNull final Runnable runnable, final long time) {
		this.scheduleSelf(runnable, time);
	}

	@Override
	public void setAlpha(final int alpha) {
		this.drawable.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(@Nullable final ColorFilter colorFilter) {
		this.drawable.setColorFilter(colorFilter);
	}

	@Override
	public void unscheduleDrawable(@NonNull final Drawable drawable, @NonNull final Runnable runnable) {
		this.unscheduleSelf(runnable);
	}

	@Override
	protected void onBoundsChange(@NonNull final Rect bounds) {
		this.drawable.setBounds(bounds.left, bounds.top, this.width < 0 ? bounds.right : this.width - bounds.left, this.height < 0 ? bounds.bottom : this.height - bounds.top);
	}

	@Override
	protected boolean onLevelChange(final int level) {
		return this.drawable.setLevel(level);
	}

	@Override
	protected boolean onStateChange(@NonNull final int[] state) {
		final boolean changed = this.drawable.setState(state);

		if(changed) {
			this.invalidateSelf();
		}

		return changed;
	}

	/**
	 * Sets the {@link android.graphics.drawable.Drawable}'s height.
	 *
	 * @param height the height
	 */
	public void setHeight(final int height) {
		this.height = height;
		this.setBounds(this.getBounds());
	}

	/**
	 * Sets the {@link android.graphics.drawable.Drawable}'s size.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public void setSize(final int width, final int height) {
		this.width = width;
		this.height = height;
		this.setBounds(this.getBounds());
	}

	/**
	 * Sets the {@link android.graphics.drawable.Drawable}'s width.
	 *
	 * @param width the width
	 */
	public void setWidth(final int width) {
		this.width = width;
		this.setBounds(this.getBounds());
	}
}
