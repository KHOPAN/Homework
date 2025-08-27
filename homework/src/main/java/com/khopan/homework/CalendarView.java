package com.khopan.homework;

import android.content.Context;
import android.content.res.Resources;
import android.media.Image;
import android.transition.Transition;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.sec.sesl.khopan.homework.R;

public class CalendarView extends LinearLayout {
	int index = -1;

	public CalendarView(final Context context) {
		super(context);
		this.setOrientation(LinearLayout.VERTICAL);
		final RelativeLayout buttonHolder = new RelativeLayout(context);
		buttonHolder.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		final Resources resources = context.getResources();

		final ImageButton previousButton = new ImageButton(context);
		final RelativeLayout.LayoutParams previousButtonParameters = new RelativeLayout.LayoutParams(resources.getDimensionPixelSize(R.dimen.sesl_date_picker_calendar_header_button_width), resources.getDimensionPixelSize(R.dimen.sesl_date_picker_calendar_header_button_height));
		previousButtonParameters.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		previousButtonParameters.addRule(RelativeLayout.CENTER_VERTICAL);
		previousButton.setLayoutParams(previousButtonParameters);
		previousButton.setImageResource(R.drawable.sesl_date_picker_prev);
		buttonHolder.addView(previousButton);

		TextSwitcher switcher = new TextSwitcher(context);
		final RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
		parameters.addRule(RelativeLayout.CENTER_IN_PARENT);
		parameters.addRule(RelativeLayout.CENTER_VERTICAL);
		switcher.setLayoutParams(parameters);
		switcher.addView(new TextView(context));
		switcher.addView(new TextView(context));
		switcher.setInAnimation(context, android.R.anim.slide_in_left);
		switcher.setOutAnimation(context, android.R.anim.slide_out_right);
		switcher.setText("Hello");
		buttonHolder.addView(switcher);

		final ImageButton nextButton = new ImageButton(context);
		final RelativeLayout.LayoutParams nextButtonParameters = new RelativeLayout.LayoutParams(resources.getDimensionPixelSize(R.dimen.sesl_date_picker_calendar_header_button_width), resources.getDimensionPixelSize(R.dimen.sesl_date_picker_calendar_header_button_height));
		nextButtonParameters.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		nextButtonParameters.addRule(RelativeLayout.CENTER_VERTICAL);
		nextButton.setLayoutParams(nextButtonParameters);
		nextButton.setImageResource(R.drawable.sesl_date_picker_next);
		nextButton.setOnClickListener(view -> {
			switcher.setText(new String[] {"First", "Second", "Third", "Forth", "Fifth"}[index = (index + 1) % 5]);
			switcher.animate().translationY(50.0f * index).start();
		});

		buttonHolder.addView(nextButton);

		this.addView(buttonHolder);
		/*ImageButton button = new ImageButton(context);
		button.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		button.setImageResource(R.drawable.sesl_date_picker_prev);
		this.addView(button);
		RelativeLayout layout = new RelativeLayout(context);
		layout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.addView(layout);*/
	}
}
