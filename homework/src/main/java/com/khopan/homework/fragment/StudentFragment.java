package com.khopan.homework.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khopan.core.fragment.ContextedFragment;
import com.khopan.core.fragment.FunctionalFragment;
import com.khopan.homework.R;
import com.khopan.homework.view.EventCalendarView;

public class StudentFragment extends ContextedFragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle bundle) {
		return inflater.inflate(R.layout.event_calendar_view, container, false);
	}
}
