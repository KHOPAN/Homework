package com.khopan.core.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dev.oneuiproject.oneui.layout.ToolbarLayout;

public abstract class FragmentedActivity extends ToolbarActivity {
	protected FrameLayout fragmentLayout;
	protected int frameLayoutIdentifier;
	protected Fragment fragment;

	@Override
	protected void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.fragmentLayout = new FrameLayout(this);
		this.fragmentLayout.setLayoutParams(new ToolbarLayout.ToolbarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.fragmentLayout.setId(this.frameLayoutIdentifier = View.generateViewId());
		this.toolbarLayout.addView(this.fragmentLayout);
	}

	@Override
	public void onBackPressed() {
		if(!(this.fragment instanceof OnBackListener && ((OnBackListener) this.fragment).onBackPressed())) {
			super.onBackPressed();
		}
	}

	public void setFragment(@Nullable final Fragment fragment) {
		if((this.fragment = fragment) == null) {
			this.fragmentLayout.removeAllViews();
		} else {
			this.getSupportFragmentManager().beginTransaction().replace(this.frameLayoutIdentifier, this.fragment).commit();
		}
	}
}
