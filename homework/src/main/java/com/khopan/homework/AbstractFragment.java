package com.khopan.homework;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;

public abstract class AbstractFragment extends Fragment {
	public abstract @DrawableRes int getIcon();
	public abstract String getName();
}
