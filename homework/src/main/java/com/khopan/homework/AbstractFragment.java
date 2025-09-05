package com.khopan.homework;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

public abstract class AbstractFragment extends Fragment {
	protected final @DrawableRes int icon;
	protected final @StringRes int name;

	public AbstractFragment(final @DrawableRes int icon, final @StringRes int name) {
		this.icon = icon;
		this.name = name;
	}
}
