package com.khopan.homework.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.widget.NestedScrollView;
import androidx.picker.app.SeslDatePickerDialog;

import com.khopan.core.activity.ToolbarActivity;
import com.khopan.homework.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import dev.oneuiproject.oneui.layout.ToolbarLayout;
import dev.oneuiproject.oneui.widget.CardItemView;
import dev.oneuiproject.oneui.widget.RoundedLinearLayout;
import dev.oneuiproject.oneui.widget.Separator;

public class NewAssignmentActivity extends ToolbarActivity {
	private CardItemView deadlineView;
	private LocalDate deadline;

	@Override
	public void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.toolbarLayout.setShowNavigationButtonAsBack(true);
		this.toolbarLayout.setTitle(this.getString(R.string.newAssignment));
		final NestedScrollView scrollView = new NestedScrollView(this);
		scrollView.setLayoutParams(new ToolbarLayout.ToolbarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		scrollView.setFillViewport(true);
		scrollView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
		final LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		final Separator subjectTitle = new Separator(this);
		subjectTitle.setText(this.getString(R.string.subject));
		linearLayout.addView(subjectTitle);
		final RoundedLinearLayout subjectLayout = new RoundedLinearLayout(this);
		subjectLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		subjectLayout.setBackgroundColor(this.getColor(dev.oneuiproject.oneui.design.R.color.oui_des_background_color));
		subjectLayout.setOrientation(LinearLayout.VERTICAL);
		final DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		final int subjectPaddingHorizontal = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16.0f, metrics));
		final int subjectPaddingVertical = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f, metrics));
		subjectLayout.setPadding(subjectPaddingHorizontal, subjectPaddingVertical, subjectPaddingHorizontal, subjectPaddingVertical);
		final AppCompatSpinner subjectSpinner = new AppCompatSpinner(this);
		subjectSpinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		final ArrayAdapter<?> subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[] {"Math", "Physics", "Biology", "Add new subject"});
		subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		subjectSpinner.setAdapter(subjectAdapter);
		subjectLayout.addView(subjectSpinner);
		linearLayout.addView(subjectLayout);
		linearLayout.addView(new Separator(this));
		final RoundedLinearLayout deadlineLayout = new RoundedLinearLayout(this);
		deadlineLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		deadlineLayout.setBackgroundColor(this.getColor(dev.oneuiproject.oneui.design.R.color.oui_des_background_color));
		deadlineLayout.setOrientation(LinearLayout.VERTICAL);
		this.deadlineView = new CardItemView(this);
		this.deadlineView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		this.deadlineView.setTitle(this.getString(R.string.deadline));
		this.deadlineView.setClickable(true);
		this.deadline = LocalDate.now();
		this.deadlineView.setOnClickListener(view -> new SeslDatePickerDialog(this, (picker, year, month, day) -> {
			this.deadline = LocalDate.of(year, month + 1, day);
			this.updateDeadlineView();
		}, this.deadline.getYear(), this.deadline.getMonthValue() - 1, this.deadline.getDayOfMonth()).show());

		this.updateDeadlineView();
		deadlineLayout.addView(this.deadlineView);
		linearLayout.addView(deadlineLayout);

		scrollView.addView(linearLayout);
		this.toolbarLayout.addView(scrollView);
	}

	private void updateDeadlineView() {
		this.deadlineView.setSummary(this.deadline.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.getDefault())));
	}
}
