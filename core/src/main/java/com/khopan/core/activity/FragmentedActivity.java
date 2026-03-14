package com.khopan.core.activity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.fragment.app.Fragment;

import com.khopan.core.fragment.EmptyFragment;

import dev.oneuiproject.oneui.layout.ToolbarLayout;
import dev.oneuiproject.oneui.widget.RoundedFrameLayout;

/**
 * An abstract {@link com.khopan.core.activity.ToolbarActivity}
 * subclass that holds a {@link androidx.fragment.app.Fragment}.
 */
public abstract class FragmentedActivity extends ToolbarActivity {
	/**
	 * The {@link com.khopan.core.fragment.EmptyFragment} instance.
	 */
	protected final EmptyFragment emptyFragment;

	/**
	 * The {@link com.khopan.core.activity.FragmentedActivity.BackListener}.
	 */
	protected BackListener backListener;

	/**
	 * The {@link androidx.fragment.app.Fragment}.
	 */
	protected Fragment fragment;

	/**
	 * The layout identifier for the internal
	 * {@link dev.oneuiproject.oneui.widget.RoundedFrameLayout}.
	 */
	protected int frameLayoutIdentifier;

	/**
	 * The internal {@link dev.oneuiproject.oneui.widget.RoundedFrameLayout}.
	 */
	protected RoundedFrameLayout fragmentLayout;

	/**
	 * Constructs a new {@link com.khopan.core.activity.FragmentedActivity} instance.
	 */
	public FragmentedActivity() {
		this.emptyFragment = new EmptyFragment();
	}

	/**
	 * @return the {@link com.khopan.core.activity.FragmentedActivity.BackListener}.
	 */
	public BackListener getBackListener() {
		return this.backListener;
	}

	@Override
	public void onBackPressed() {
		if((!(this.fragment instanceof BackListener) || !((BackListener) this.fragment).backPressed()) && (this.backListener == null || !this.backListener.backPressed())) {
			super.onBackPressed();
		}
	}

	/**
	 * Sets the {@link com.khopan.core.activity.FragmentedActivity.BackListener}.
	 *
	 * @param listener the {@link com.khopan.core.activity.FragmentedActivity.BackListener}.
	 */
	public void setBackListener(final BackListener listener) {
		this.backListener = listener;
	}

	/**
	 * Sets the {@link androidx.fragment.app.Fragment}.
	 *
	 * @param fragment the {@link androidx.fragment.app.Fragment}.
	 */
	public void setFragment(final Fragment fragment) {
		this.getSupportFragmentManager().beginTransaction().replace(this.frameLayoutIdentifier, (this.fragment = fragment) == null ? this.emptyFragment : this.fragment).commit();
	}

	@Override
	protected void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.fragmentLayout = new RoundedFrameLayout(this);
		this.fragmentLayout.setId(this.frameLayoutIdentifier = View.generateViewId());
		final int insets = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f, this.metrics));
		this.fragmentLayout.setEdgeInsets(Insets.of(insets, 0, insets, 0));
		this.toolbarLayout.addView(this.fragmentLayout, new ToolbarLayout.ToolbarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	}

	/**
	 * A listener for handling the back button.
	 */
	@FunctionalInterface
	public interface BackListener {
		/**
		 * Handles the back button.
		 *
		 * @return true if the back button has been handled, false otherwise.
		 */
		boolean backPressed();
	}
}
