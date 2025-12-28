package com.khopan.homework;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;

import com.khopan.core.activity.NavigationDrawerActivity;

public class HomeworkApplication extends NavigationDrawerActivity {
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
			return false;
		}
	}
}
