package com.khopan.core;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;

import dev.oneuiproject.oneui.widget.CardItemView;
import dev.oneuiproject.oneui.widget.SwitchItemView;

public class CoreLayout {
	private CoreLayout() {}

	public static void fixOffCenteredCardItemView(final Object itemView) {
		if(itemView instanceof CardItemView) {
			((CardItemView) itemView).setSummary("null");
			((CardItemView) itemView).setSummary(null);
		}

		if(itemView instanceof SwitchItemView) {
			((SwitchItemView) itemView).setSummary("null");
			((SwitchItemView) itemView).setSummary(null);
		}
	}

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

	public static void forceRemeasure(final View view) {
		view.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(view.getHeight(), View.MeasureSpec.EXACTLY));
		view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
		view.invalidate();
	}

	public static void forceRemeasure(final ViewGroup view, final Runnable visible, final Runnable invisible) {
		final LayoutTransition transition = view.getLayoutTransition();
		view.setLayoutTransition(null);
		visible.run();
		CoreLayout.forceRemeasure(view);
		invisible.run();
		CoreLayout.forceRemeasure(view);
		view.setLayoutTransition(transition);
	}

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
