package com.khopan.core.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dev.oneuiproject.oneui.layout.ToolbarLayout;

/**
 * An abstract {@link android.app.Activity} that implements a
 * {@link dev.oneuiproject.oneui.layout.ToolbarLayout}.
 */
public abstract class ToolbarActivity extends AppCompatActivity {
	/**
	 * The {@link android.util.DisplayMetrics} for this
	 * {@link android.app.Activity}.
	 */
	protected DisplayMetrics metrics;

	/**
	 * The {@link android.content.res.Resources} for this
	 * {@link android.app.Activity}.
	 */
	protected Resources resources;

	/**
	 * The {@link android.content.res.Resources.Theme} for this
	 * {@link android.app.Activity}.
	 */
	protected Resources.Theme theme;

	/**
	 * The {@link dev.oneuiproject.oneui.layout.ToolbarLayout}.
	 */
	protected ToolbarLayout toolbarLayout;

	/**
	 * Creates a new instance of {@link dev.oneuiproject.oneui.layout.ToolbarLayout}.
	 * This is useful when you want to provide a custom {@link dev.oneuiproject.oneui.layout.ToolbarLayout}
	 * implementation, such as {@link dev.oneuiproject.oneui.layout.NavDrawerLayout}.
	 *
	 * @return an instance of {@link dev.oneuiproject.oneui.layout.ToolbarLayout}.
	 */
	protected ToolbarLayout createToolbarLayout() {
		return new ToolbarLayout(this);
	}

	@Override
	protected void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.resources = this.getResources();
		this.metrics = this.resources.getDisplayMetrics();
		this.theme = this.getTheme();
		this.toolbarLayout = this.createToolbarLayout();
		this.toolbarLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.toolbarLayout.setExpanded(false, false);
		this.setContentView(this.toolbarLayout);
	}
}
