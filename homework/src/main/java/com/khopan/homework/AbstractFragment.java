package com.khopan.homework;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

public abstract class AbstractFragment extends Fragment {
	protected @DrawableRes final int icon;
	protected @StringRes final int name;

	public AbstractFragment(@DrawableRes final int icon, @StringRes final int name) {
		this.icon = icon;
		this.name = name;
	}
}
