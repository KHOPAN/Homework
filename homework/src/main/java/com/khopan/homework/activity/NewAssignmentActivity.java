package com.khopan.homework.activity;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.picker.app.SeslDatePickerDialog;

import com.khopan.core.activity.ToolbarActivity;
import com.khopan.homework.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import dev.oneuiproject.oneui.widget.CardItemView;

public class NewAssignmentActivity extends ToolbarActivity {
	private CardItemView deadlineView;
	private LocalDate deadline;

	@Override
	public void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.toolbarLayout.setShowNavigationButtonAsBack(true);
		this.toolbarLayout.setTitle(this.getString(R.string.newAssignment));
		LayoutInflater.from(this).inflate(R.layout.new_assignment_activity, this.toolbarLayout);
		this.deadlineView = this.findViewById(R.id.deadlineView);
		this.deadline = LocalDate.now();
		this.deadlineView.setOnClickListener(view -> new SeslDatePickerDialog(this, (picker, year, month, day) -> {
			this.deadline = LocalDate.of(year, month + 1, day);
			this.updateDeadlineView();
		}, this.deadline.getYear(), this.deadline.getMonthValue() - 1, this.deadline.getDayOfMonth()).show());

		this.updateDeadlineView();
	}

	private void updateDeadlineView() {
		this.deadlineView.setTitle(this.deadline.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.getDefault())));
	}
}
