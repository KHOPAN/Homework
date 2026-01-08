package com.khopan.homework;

import android.app.Activity;
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
import com.khopan.homework.fragment.StudentFragment;

public class HomeworkApplication extends NavigationDrawerActivity {
	public HomeworkApplication() {
		this.drawerItems.add(DrawerEntry.create(0, "Assignment", null));
		this.drawerItems.add(DrawerEntry.create(0, "Student", new StudentFragment()));
		this.drawerItems.add(DrawerEntry.create(0, "Teacher", null));
		this.drawerItems.add(DrawerEntry.create(0, "Subject", null));
		this.drawerItems.add(DrawerEntry.create(0, "Group", null));
	}

	@Override
	public void onCreate(@Nullable final Bundle bundle) {
		super.onCreate(bundle);
		this.addMenuProvider(new Provider());
	}

	private class Provider implements MenuProvider {
		@Override
		public void onCreateMenu(@NonNull final Menu menu, @NonNull final MenuInflater inflater) {
			inflater.inflate(R.menu.menu, menu);
		}

		@Override
		public boolean onMenuItemSelected(@NonNull final MenuItem item) {
			final Class<? extends Activity> activity;

			if(item.getItemId() == R.id.menu_new_assignment) {
				activity = NewAssignmentActivity.class;
			} else if(item.getItemId() == R.id.menu_settings) {
				activity = SettingsActivity.class;
			} else {
				return false;
			}

			HomeworkApplication.this.startActivity(new Intent(HomeworkApplication.this, activity));
			return true;
		}
	}
}
