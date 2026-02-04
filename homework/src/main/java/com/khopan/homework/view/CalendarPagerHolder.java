package com.khopan.homework.view;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.khopan.homework.R;

public class CalendarPagerHolder {
	final ViewPager2 viewPager;

	private final EventCalendarView view;

	public CalendarPagerHolder(final EventCalendarView view) {
		this.view = view;
		this.viewPager = new ViewPager2(this.view.context);
		this.viewPager.setAdapter(new Adapter());
		this.viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
	}

	private class Adapter extends RecyclerView.Adapter<ViewHolder> {
		@Override
		public int getItemCount() {
			return 13;
		}

		@Override
		public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			final LinearLayout linearLayout = new LinearLayout(CalendarPagerHolder.this.view.context);
			linearLayout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			linearLayout.setOrientation(LinearLayout.VERTICAL);

			final Button button = new Button(CalendarPagerHolder.this.view.context);
			button.setBackgroundResource(dev.oneuiproject.oneui.design.R.drawable.oui_des_btn_transparent_bg);
			button.setText("January");
			button.setClickable(true);
			button.setFocusable(true);
			button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26.0f);
			var x = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			x.gravity = Gravity.CENTER_HORIZONTAL;
			x.bottomMargin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26.0f, CalendarPagerHolder.this.view.context.getResources().getDisplayMetrics()));
			linearLayout.addView(button, x);
			linearLayout.addView(new CalendarView(CalendarPagerHolder.this.view, 8), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

			// 2.05625
			return new ViewHolder(linearLayout);
		}
	}

	private static class ViewHolder extends RecyclerView.ViewHolder {
		private final LinearLayout linearLayout;

		private ViewHolder(final LinearLayout linearLayout) {
			super(linearLayout);
			this.linearLayout = linearLayout;
		}
	}
}
