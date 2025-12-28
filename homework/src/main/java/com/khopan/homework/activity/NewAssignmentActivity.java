package com.khopan.homework.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.khopan.core.activity.ToolbarActivity;
import com.khopan.homework.R;

import dev.oneuiproject.oneui.layout.ToolbarLayout;
import dev.oneuiproject.oneui.widget.RoundedLinearLayout;

public class NewAssignmentActivity extends ToolbarActivity {
	@Override
	public void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		//this.toolbarLayout.setExpandable(false);
		this.toolbarLayout.setShowNavigationButtonAsBack(true);
		this.toolbarLayout.setTitle(this.getString(R.string.newAssignment));
		final NestedScrollView scrollView = new NestedScrollView(this);
		scrollView.setLayoutParams(new ToolbarLayout.ToolbarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		scrollView.setFillViewport(true);
		scrollView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
		final LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		final RoundedLinearLayout serverLayout = new RoundedLinearLayout(this);
		serverLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		serverLayout.setBackgroundColor(this.getColor(dev.oneuiproject.oneui.design.R.color.oui_des_background_color));
		serverLayout.setOrientation(LinearLayout.VERTICAL);
		/*final CardItemView serverAddressView = new CardItemView(this);
		serverAddressView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		serverAddressView.setTitle("Server Address");
		serverAddressView.setSummary("server.com");
		serverAddressView.setClickable(true);
		serverLayout.addView(serverAddressView);*/
		final EditText edit = new EditText(this);
		edit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		serverLayout.addView(edit);
		linearLayout.addView(serverLayout);

		scrollView.addView(linearLayout);
		this.toolbarLayout.addView(scrollView);
	}
}
