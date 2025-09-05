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
	public TestFragment() {
		super(R.drawable.ic_oui_add_pdf, R.string.cant_check_for_updates_tablet);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
		return super.onCreateView(inflater, container, bundle);
	}
}
