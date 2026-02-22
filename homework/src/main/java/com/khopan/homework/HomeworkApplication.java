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
import com.khopan.homework.database.HomeworkDatabase;
import com.khopan.homework.fragment.AssignmentFragment;
import com.khopan.homework.fragment.CalendarFragment;

public class HomeworkApplication extends NavigationDrawerActivity {
	@Override
	public void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.addMenuProvider(new Provider());
		this.toolbarLayout.setExpandable(false);
		this.drawerItems.clear();
		this.drawerItems.add(DrawerEntry.create(dev.oneuiproject.oneui.R.drawable.ic_oui_calendar, this.getString(R.string.calendar), new CalendarFragment()));
		this.drawerItems.add(DrawerEntry.create(dev.oneuiproject.oneui.R.drawable.ic_oui_document_outline, this.getString(R.string.assignment), new AssignmentFragment()));
		this.setSelectedItem(0);
		HomeworkDatabase.getInstance(this.getApplicationContext());
	}

	private class Provider implements MenuProvider {
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
	}
}
