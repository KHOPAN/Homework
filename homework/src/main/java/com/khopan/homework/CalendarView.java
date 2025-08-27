package com.khopan.homework;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sec.sesl.khopan.homework.R;

public class CalendarView extends LinearLayout {
	public CalendarView(Context context) {
		super(context);
		this.setOrientation(LinearLayout.VERTICAL);
		ImageButton button = new ImageButton(context);
		button.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		button.setImageResource(R.drawable.sesl_date_picker_prev);
		this.addView(button);
		//RelativeLayout layout = new RelativeLayout(context);
		//layout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		//this.addView(layout);
	}
}
