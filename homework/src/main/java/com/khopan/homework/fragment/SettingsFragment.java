package com.khopan.homework.fragment;

import com.khopan.homework.AbstractFragment;
import com.sec.sesl.khopan.homework.R;

public class SettingsFragment extends AbstractFragment {
	@Override
	public int getIcon() {
		return R.drawable.ic_oui_settings_outline;
	}

	@Override
	public String getName() {
		return this.getString(R.string.fragment_settings);
	}
}
