package com.khopan.homework;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager2.widget.ViewPager2;

import com.khopan.homework.calendar.CalendarView;
import com.khopan.homework.calendar.MonthAdapter;

import java.lang.reflect.Method;
import java.util.Calendar;

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
		/*LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(linearLayout);*/

		/*TextView textView = new TextView(this);
		textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		textView.setGravity(Gravity.CENTER);
		textView.setText("Homework");
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0f);
		linearLayout.addView(textView);*/
		//SeslSimpleMonthView calendar = new SeslSimpleMonthView(this);
		//MaterialCalendarView calendar = new MaterialCalendarView(this);
		//CalendarView calendar = new CalendarView(this);
		//calendar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		/*calendar.setDynamicHeightEnabled(true);
		calendar.setPagingEnabled(true);
		calendar.setTopbarVisible(true);
		calendar.addDecorator(new Decorator());*/
		//scrollView.addView(calendar);

		ViewPager2 monthPager = new ViewPager2(this);
		monthPager.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		monthPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

		// Start from current month
		Calendar startCalendar = Calendar.getInstance();

		monthPager.setAdapter(new MonthAdapter(this, startCalendar));
		scrollView.addView(monthPager);
	}
}
