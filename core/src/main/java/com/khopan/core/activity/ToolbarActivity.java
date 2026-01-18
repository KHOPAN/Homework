package com.khopan.core.activity;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.khopan.core.R;

import dev.oneuiproject.oneui.layout.NavDrawerLayout;
import dev.oneuiproject.oneui.layout.ToolbarLayout;

public abstract class ToolbarActivity extends AppCompatActivity {
	protected NavDrawerLayout drawerLayout;
	protected ToolbarLayout toolbarLayout;
	protected boolean useDrawerLayout;

	@Override
	protected void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.toolbarLayout = (ToolbarLayout) this.getLayoutInflater().inflate(this.useDrawerLayout ? R.layout.layout_navigation_drawer : R.layout.layout_toolbar, null);

		if(this.useDrawerLayout) {
			this.drawerLayout = (NavDrawerLayout) this.toolbarLayout;
		}

		this.toolbarLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.toolbarLayout.setExpanded(false, false);
		this.setContentView(this.toolbarLayout);
	}
}
