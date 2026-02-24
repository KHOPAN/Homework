package com.khopan.homework.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.picker.app.SeslDatePickerDialog;
import androidx.picker.app.SeslTimePickerDialog;

import com.khopan.core.activity.ToolbarActivity;
import com.khopan.core.view.card.CardView;
import com.khopan.homework.R;
import com.khopan.homework.database.HomeworkDatabase;
import com.khopan.homework.database.dao.AssignmentDao;
import com.khopan.homework.database.entity.Assignment;
import com.khopan.homework.receiver.ReminderReceiver;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Objects;

public class NewAssignmentActivity extends ToolbarActivity {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.UK);

	private Button addButton;
	private CardView deadlineDateView;
	private CardView deadlineTimeView;
	private CardView titleView;
	private DateTimeFormatter formatter;
	private LocalDate deadlineDate;
	private LocalTime deadlineTime;
	private String title;

	@Override
	public void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		final AssignmentDao accessor = HomeworkDatabase.getInstance(this.getApplicationContext()).getAssignment();
		this.formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.getDefault());
		this.toolbarLayout.setShowNavigationButtonAsBack(true);
		this.toolbarLayout.setTitle(this.getString(R.string.new_assignment));
		this.getLayoutInflater().inflate(R.layout.activity_new_assignment, this.toolbarLayout, true);
		final FrameLayout frameLayout = new FrameLayout(this);
		final int padding = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12.0f, this.getResources().getDisplayMetrics()));
		frameLayout.setPadding(padding, 0, padding, 0);
		final EditText editText = new EditText(this);
		editText.setInputType(InputType.TYPE_CLASS_TEXT);
		editText.setSingleLine(true);
		frameLayout.addView(editText);
		final DialogInterface.OnClickListener listener = (dialog, which) -> {
			final String title = editText.getText().toString();
			this.title = title.isEmpty() ? null : title;
			this.updateTitleView();
		};

		final AlertDialog alertDialog = new AlertDialog.Builder(this)
				.setNegativeButton(R.string.new_assignment_title_cancel, (dialog, which) -> dialog.cancel())
				.setPositiveButton(R.string.new_assignment_title_done, listener)
				.setTitle(R.string.new_assignment_title)
				.setView(frameLayout)
				.create();

		editText.setOnEditorActionListener((view, identifier, event) -> {
			if(identifier == EditorInfo.IME_ACTION_DONE) {
				listener.onClick(null, 0);
				alertDialog.cancel();
				return true;
			}

			return false;
		});

		this.titleView = this.toolbarLayout.findViewById(R.id.title_view);
		this.titleView.setOnClickListener(view -> {
			editText.requestFocus();
			editText.setText("");

			if(this.title != null) {
				editText.append(this.title);
			}

			Objects.requireNonNull(alertDialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);;
			alertDialog.show();
		});

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
		this.addButton.setOnClickListener(view -> {
			final Assignment assignment = new Assignment();
			assignment.title = this.title;
			assignment.deadline = this.deadlineDate.toEpochSecond(this.deadlineTime, ZoneOffset.UTC);
			new Thread(() -> {
				accessor.insert(assignment);
				this.runOnUiThread(() -> {
					ReminderReceiver.schedule(this.getApplicationContext());
					this.finish();
				});
			}).start();
		});

		this.updateTitleView();
	}

	private void updateDeadlineDateView() {
		this.deadlineDateView.setSummary(this.deadlineDate.format(this.formatter));
	}

	private void updateDeadlineTimeView() {
		this.deadlineTimeView.setSummary(this.deadlineTime.format(NewAssignmentActivity.FORMATTER));
	}

	private void updateTitleView() {
		this.titleView.setSummary(this.title == null ? this.getString(R.string.new_assignment_title_not_set) : this.title);
		this.addButton.setAlpha(this.title == null ? 0.4f : 1.0f);
		this.addButton.setEnabled(this.title != null);
	}
}
