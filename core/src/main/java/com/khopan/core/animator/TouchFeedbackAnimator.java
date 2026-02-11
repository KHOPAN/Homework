package com.khopan.core.animator;

import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.animation.SeslRecoilAnimator;
import androidx.appcompat.widget.SeslLinearLayoutCompat;

/**
 * A {@link dev.oneuiproject.oneui.utils.SemTouchFeedbackAnimator}
 * that supports foreground animation.
 */
@RequiresApi(Build.VERSION_CODES.Q)
public class TouchFeedbackAnimator {
	private static final int ACTION_PEN_DOWN = 211;
	private static final int ACTION_PEN_UP = 212;

	private final SeslLinearLayoutCompat.ItemBackgroundHolder holder;
	private final SeslRecoilAnimator.Holder recoil;
	private final View holderView;
	private final View recoilView;

	/**
	 * Constructs a {@link com.khopan.core.animator.TouchFeedbackAnimator} instance.
	 *
	 * @param view the target {@link android.view.View}.
	 */
	public TouchFeedbackAnimator(final View view) {
		this(new ItemForegroundHolder(), new SeslRecoilAnimator.Holder(view.getContext()), view, view);
	}

	/**
	 * Constructs a {@link com.khopan.core.animator.TouchFeedbackAnimator} instance.
	 *
	 * @param context the {@link android.content.Context}
	 * @param holderView the {@link android.view.View} used for animating the foreground/background.
	 * @param recoilView the {@link android.view.View} used for animating the recoil animation.
	 */
	public TouchFeedbackAnimator(final Context context, final View holderView, final View recoilView) {
		this(new ItemForegroundHolder(), new SeslRecoilAnimator.Holder(context), holderView, recoilView);
	}

	/**
	 * Constructs a {@link com.khopan.core.animator.TouchFeedbackAnimator} instance.
	 *
	 * @param holder the {@link androidx.appcompat.widget.SeslLinearLayoutCompat.ItemBackgroundHolder}
	 *        instance. Can be an {@link com.khopan.core.animator.ItemForegroundHolder}, for example.
	 * @param recoil the {@link androidx.appcompat.animation.SeslRecoilAnimator.Holder} instance.
	 * @param holderView the {@link android.view.View} used for animating the foreground/background.
	 * @param recoilView the {@link android.view.View} used for animating the recoil animation.
	 */
	public TouchFeedbackAnimator(final SeslLinearLayoutCompat.ItemBackgroundHolder holder, final SeslRecoilAnimator.Holder recoil, final View holderView, final View recoilView) {
		this.holder = holder;
		this.recoil = recoil;
		this.holderView = holderView;
		this.recoilView = recoilView;
	}

	/**
	 * Handles the animation.
	 *
	 * @param event the {@link android.view.MotionEvent}.
	 */
	public void animate(final MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_CANCEL:
			this.holder.setCancel();
			this.recoil.setRelease();
			break;
		case MotionEvent.ACTION_DOWN:
		case TouchFeedbackAnimator.ACTION_PEN_DOWN:
			this.holder.setPress(this.holderView);
			this.recoil.setPress(this.recoilView);
			break;
		case TouchFeedbackAnimator.ACTION_PEN_UP:
		case MotionEvent.ACTION_UP:
			this.holder.setRelease();
			this.recoil.setRelease();
			break;
		}
	}
}
