package com.khopan.core.activity;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dev.oneuiproject.oneui.layout.ToolbarLayout;

public abstract class ToolbarActivity extends AppCompatActivity {
	protected ToolbarLayout toolbarLayout;

	protected ToolbarLayout createToolbarLayout() {
		return new ToolbarLayout(this);
	}

	@Override
	protected void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.toolbarLayout = this.createToolbarLayout();
		this.toolbarLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.toolbarLayout.setExpanded(false, false);
		this.setContentView(this.toolbarLayout);
	}
}
