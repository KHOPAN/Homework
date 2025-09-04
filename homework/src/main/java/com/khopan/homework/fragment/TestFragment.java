package com.khopan.homework.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.khopan.homework.AbstractFragment;
import com.sec.sesl.khopan.homework.R;

public class TestFragment extends AbstractFragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
		return super.onCreateView(inflater, container, bundle);
	}

	@Override
	public int getIcon() {
		return R.drawable.ic_oui_ratio_portrait_on;
	}

	@Override
	public String getName() {
		return "Test Fragment";
	}
}
