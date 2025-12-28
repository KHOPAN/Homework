package com.khopan.core;

import android.animation.LayoutTransition;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public class CoreLayout {
	private CoreLayout() {}

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
