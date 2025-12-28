package com.khopan.core.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dev.oneuiproject.oneui.layout.DrawerLayout;
import dev.oneuiproject.oneui.layout.ToolbarLayout;

public abstract class ToolbarActivity extends AppCompatActivity {
	protected boolean useDrawerLayout;
	protected ToolbarLayout toolbarLayout;

	@Override
	protected void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.toolbarLayout = this.useDrawerLayout ? new DrawerLayout(this, null) : new ToolbarLayout(this);
		this.toolbarLayout.setExpanded(false, false);
		this.setContentView(this.toolbarLayout);
	}
}
