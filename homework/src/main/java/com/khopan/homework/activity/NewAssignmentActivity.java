package com.khopan.homework.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.picker.app.SeslDatePickerDialog;
import androidx.picker.app.SeslTimePickerDialog;

import com.khopan.core.activity.ToolbarActivity;
import com.khopan.core.view.card.CardView;
import com.khopan.homework.R;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class NewAssignmentActivity extends ToolbarActivity {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.UK);

	private Button addButton;
	private CardView deadlineDateView;
	private CardView deadlineTimeView;
	private CardView titleView;
	private DateTimeFormatter formatter;
	private LocalDate deadlineDate;
	private LocalTime deadlineTime;

	@Override
	public void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.getDefault());
		this.toolbarLayout.setShowNavigationButtonAsBack(true);
		this.toolbarLayout.setTitle(this.getString(R.string.new_assignment));
		this.getLayoutInflater().inflate(R.layout.activity_new_assignment, this.toolbarLayout, true);
		this.titleView = this.toolbarLayout.findViewById(R.id.title_view);
		this.titleView.setOnClickListener(null);

		this.deadlineDate = LocalDate.now();
		final SeslDatePickerDialog datePicker = new SeslDatePickerDialog(this, (view, year, month, day) -> {
			this.deadlineDate = LocalDate.of(year, month + 1, day);
			this.updateDeadlineDateView();
		}, this.deadlineDate.getYear(), this.deadlineDate.getMonthValue() - 1, this.deadlineDate.getDayOfMonth());

		this.deadlineDateView = this.toolbarLayout.findViewById(R.id.deadline_date_view);
		this.deadlineDateView.setOnClickListener(view -> datePicker.show());
		this.updateDeadlineDateView();
		this.deadlineTime = LocalTime.now();
		final SeslTimePickerDialog timePicker = new SeslTimePickerDialog(this, (view, hour, minute) -> {
			this.deadlineTime = LocalTime.of(hour, minute);
			this.updateDeadlineTimeView();
		}, this.deadlineTime.getHour(), this.deadlineTime.getMinute(), true);

		timePicker.setTitle(R.string.new_assignment_deadline_time);
		this.deadlineTimeView = this.toolbarLayout.findViewById(R.id.deadline_time_view);
		this.deadlineTimeView.setOnClickListener(view -> timePicker.show());
		this.updateDeadlineTimeView();
		this.toolbarLayout.<Button>findViewById(R.id.cancel_button).setOnClickListener(view -> this.getOnBackPressedDispatcher().onBackPressed());
		this.addButton = this.toolbarLayout.findViewById(R.id.add_button);
	}

	private void updateDeadlineDateView() {
		this.deadlineDateView.setSummary(this.deadlineDate.format(this.formatter));
	}

	private void updateDeadlineTimeView() {
		this.deadlineTimeView.setSummary(this.deadlineTime.format(NewAssignmentActivity.FORMATTER));
	}
}
