package com.khopan.homework;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;

import com.khopan.core.activity.NavigationDrawerActivity;
import com.khopan.homework.activity.NewAssignmentActivity;
import com.khopan.homework.activity.SettingsActivity;
import com.khopan.homework.fragment.CalendarFragment;
import com.khopan.homework.fragment.StudentFragment;
import com.khopan.homework.fragment.SubjectFragment;

public class HomeworkApplication extends NavigationDrawerActivity {
	public HomeworkApplication() {
		this.drawerItems.add(DrawerEntry.create(dev.oneuiproject.oneui.R.drawable.ic_oui_calendar, "Calendar", new CalendarFragment()));
		this.drawerItems.add(DrawerEntry.create(0, "Assignment", null));
		this.drawerItems.add(DrawerEntry.create(0, "Student", new StudentFragment()));
		this.drawerItems.add(DrawerEntry.create(0, "Teacher", null));
		this.drawerItems.add(DrawerEntry.create(0, "Subject", new SubjectFragment()));
		this.drawerItems.add(DrawerEntry.create(0, "Group", null));
	}

	@Override
	public void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.toolbarLayout.setExpandable(false);
		this.addMenuProvider(new MenuProvider() {
			@Override
			public void onCreateMenu(@NonNull final Menu menu, @NonNull final MenuInflater inflater) {
				inflater.inflate(R.menu.menu, menu);
			}

			@Override
			public boolean onMenuItemSelected(@NonNull final MenuItem item) {
				final int identifier = item.getItemId();
				final Class<?> activity;

				if(identifier == R.id.menu_new_assignment) {
					activity = NewAssignmentActivity.class;
				} else if(identifier == R.id.menu_settings) {
					activity = SettingsActivity.class;
				} else {
					return false;
				}

				HomeworkApplication.this.startActivity(new Intent(HomeworkApplication.this, activity));
				return true;
			}
		});

		this.setSelectedItem(0);
		/*final Calendar calendar = Calendar.getInstance();
		calendar.set(2026, Calendar.JANUARY, 19, 1, 1, 0);
		final Intent intent = new Intent(this, ReminderReceiver.class);
		final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		final AlarmManager manager = this.getSystemService(AlarmManager.class);
		manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);*/
	}
}
