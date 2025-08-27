package com.khopan.homework;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.picker.widget.SeslDatePicker;

import java.lang.reflect.Method;

import dev.oneuiproject.oneui.layout.ToolbarLayout;

public class HomeworkApplication extends AppCompatActivity {
	@Override
	public void onCreate(@Nullable Bundle bundle) {
		super.onCreate(bundle);
		ToolbarLayout toolbarLayout = new ToolbarLayout(this, null);
		this.setContentView(toolbarLayout);
		toolbarLayout.setTitle("Homework");
		toolbarLayout.setExpanded(false, false);
		toolbarLayout.setNavigationButtonVisible(false);
		FrameLayout frameLayout = new FrameLayout(this);
		frameLayout.setLayoutParams(new ToolbarLayout.ToolbarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		toolbarLayout.addView(frameLayout);
		NestedScrollView scrollView = new NestedScrollView(this);
		scrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		scrollView.setFillViewport(true);
		scrollView.setOverScrollMode(NestedScrollView.OVER_SCROLL_ALWAYS);

		try {
			String methodName = "initializeScrollbars";
			@SuppressLint("DiscouragedPrivateApi")
			Method method = View.class.getDeclaredMethod(methodName, TypedArray.class);
			method.setAccessible(true);
			method.invoke(scrollView, this.obtainStyledAttributes(null, new int[] {}));
		} catch(Throwable ignored) {

		}

		scrollView.setVerticalScrollBarEnabled(true);
		frameLayout.addView(scrollView);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(linearLayout);

		/*TextView textView = new TextView(this);
		textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		textView.setGravity(Gravity.CENTER);
		textView.setText("Homework");
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0f);
		linearLayout.addView(textView);*/
		CalendarView calendar = new CalendarView(this);
		calendar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		linearLayout.addView(calendar);
	}
}
