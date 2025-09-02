package com.khopan.homework;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dev.oneuiproject.oneui.layout.DrawerLayout;

public class HomeworkApplication extends AppCompatActivity {
	@Override
	public void onCreate(@Nullable Bundle bundle) {
		super.onCreate(bundle);
		final DrawerLayout drawerLayout = new DrawerLayout(this, null);
		drawerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		drawerLayout.setExpandable(false);
		drawerLayout.setExpanded(false);
		drawerLayout.setTitle("Homework");
		this.setContentView(drawerLayout);
		/*CoordinatorLayout root = new CoordinatorLayout(this);

		AppBarLayout bar = new AppBarLayout(this);
		bar.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		CollapsingToolbarLayout toolbar = new CollapsingToolbarLayout(this);
		toolbar.setLayoutParams(new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		Button button = new Button(this);
		button.setText("Hello, world!");
		toolbar.addView(button);

		bar.addView(toolbar);

		ScrollView scrollView = new ScrollView(this);
		scrollView.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		Button test = new Button(this);
		test.setText("Test");
		scrollView.addView(test);

		root.addView(scrollView);

		root.addView(bar);

		this.setContentView(root);
		/*TestLayout layout = new TestLayout(this);

		Button first = new Button(this);
		first.setText("First");

		Button second = new Button(this);
		second.setText("Second");

		layout.setViews(first, second);
		this.setContentView(layout);*/
		//View top = new View(this);
		//top.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		//top.setBackgroundColor(Color.RED);

		/*ViewPager2 monthPager = new ViewPager2(this);
		//monthPager.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		monthPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

		// Start from current month
		Calendar startCalendar = Calendar.getInstance();

		monthPager.setAdapter(new MonthAdapter(this, startCalendar));
		//scrollView.addView(monthPager);

		Button button = new Button(this);
		button.setText("Test");
		button.setNestedScrollingEnabled(true);

		TwoStageScroller scroller = new TwoStageScroller(this);
		scroller.setViews(button, new View(this));
		this.setContentView(scroller);

		/*super.onCreate(bundle);
		final Resources resources = this.getResources();
		final ToolbarLayout toolbar = new ToolbarLayout(this, null);
		toolbar.setExpanded(false, false);
		toolbar.setTitle("Homework");
		toolbar.setNavigationButtonVisible(true);
		this.setContentView(toolbar);*/

		//toolbar.setTitle(resources.getString(R.string.title));
		/*ToolbarLayout toolbarLayout = new ToolbarLayout(this, null);
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

		/*ViewPager2 monthPager = new ViewPager2(this);
		monthPager.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		monthPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

		// Start from current month
		Calendar startCalendar = Calendar.getInstance();

		monthPager.setAdapter(new MonthAdapter(this, startCalendar));
		scrollView.addView(monthPager);*/
	}
}
