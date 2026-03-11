package com.khopan.core;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;

/**
 * A layout-related utility class.
 */
public class CoreLayout {
	private CoreLayout() {}

	/**
	 * Forces enable the scrollbars of a {@link android.view.View}.
	 *
	 * @param view the {@link android.view.View}.
	 * @param horizontal whether to enable the horizontal scrollbar or not.
	 * @param vertical whether to enable the vertical scrollbar or not.
	 */
	public static void forceEnableScrollbars(final View view, final boolean horizontal, final boolean vertical) {
		final Context context = view.getContext();

		if(context == null) {
			return;
		}

		try {
			final Method method = View.class.getDeclaredMethod("initializeScrollbars", TypedArray.class);
			method.setAccessible(true);
			method.invoke(view, context.obtainStyledAttributes(null, new int[] {}));
		} catch(Throwable ignored) {}

		view.setHorizontalScrollBarEnabled(horizontal);
		view.setVerticalScrollBarEnabled(vertical);
	}

	/**
	 * Forces view remeasure.
	 *
	 * @param view the {@link android.view.View}.
	 */
	public static void forceRemeasure(final View view) {
		view.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(view.getHeight(), View.MeasureSpec.EXACTLY));
		view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
		view.invalidate();
	}

	/**
	 * Forces view remeasure.
	 *
	 * @param view the {@link android.view.View}.
	 * @param visible the {@link java.lang.Runnable} to set the view
	 *                to visible.
	 * @param invisible the {@link java.lang.Runnable} to set the view
	 *                   to invisible.
	 */
	public static void forceRemeasure(final ViewGroup view, final Runnable visible, final Runnable invisible) {
		final LayoutTransition transition = view.getLayoutTransition();
		view.setLayoutTransition(null);
		visible.run();
		CoreLayout.forceRemeasure(view);
		invisible.run();
		CoreLayout.forceRemeasure(view);
		view.setLayoutTransition(transition);
	}

	/**
	 * Enables a {@link android.view.View}'s {@link android.animation.LayoutTransition}.
	 *
	 * @param view {@link android.view.View}.
	 */
	public static void setLayoutTransition(@NonNull final ViewGroup view) {
		final LayoutTransition transition = new LayoutTransition();
		transition.enableTransitionType(LayoutTransition.APPEARING);
		transition.enableTransitionType(LayoutTransition.DISAPPEARING);
		transition.enableTransitionType(LayoutTransition.CHANGE_APPEARING);
		transition.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
		transition.enableTransitionType(LayoutTransition.CHANGING);
		view.setLayoutTransition(transition);
	}
}
