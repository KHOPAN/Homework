package com.khopan.homework.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.khopan.homework.AbstractFragment;
import com.sec.sesl.khopan.homework.R;

public class HomeworkFragment extends AbstractFragment {
	public HomeworkFragment() {
		super(R.drawable.ic_oui_alarm, R.string.applicationName);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle bundle) {
		final Context context = this.getContext();

		if(context == null) {
			return null;
		}

		final ViewPager2 pager = new ViewPager2(context);
		return pager;
	}
}
