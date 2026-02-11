package com.khopan.core.activity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.fragment.app.Fragment;

import dev.oneuiproject.oneui.layout.ToolbarLayout;
import dev.oneuiproject.oneui.widget.RoundedFrameLayout;

public abstract class FragmentedActivity extends ToolbarActivity {
	protected Fragment fragment;
	protected RoundedFrameLayout fragmentLayout;
	protected int frameLayoutIdentifier;

	@Override
	public void onBackPressed() {
		if(!(this.fragment instanceof BackListener && ((BackListener) this.fragment).backPressed())) {
			super.onBackPressed();
		}
	}

	public void setFragment(final Fragment fragment) {
		if((this.fragment = fragment) == null) {
			this.fragmentLayout.removeAllViews();
		} else {
			this.getSupportFragmentManager().beginTransaction().replace(this.frameLayoutIdentifier, this.fragment).commit();
		}
	}

	@Override
	protected void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.fragmentLayout = new RoundedFrameLayout(this);
		this.fragmentLayout.setId(this.frameLayoutIdentifier = View.generateViewId());
		final int insets = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f, this.getResources().getDisplayMetrics()));
		this.fragmentLayout.setEdgeInsets(Insets.of(insets, 0, insets, 0));
		this.toolbarLayout.addView(this.fragmentLayout, new ToolbarLayout.ToolbarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	}

	@FunctionalInterface
	public interface BackListener {
		boolean backPressed();
	}
}
