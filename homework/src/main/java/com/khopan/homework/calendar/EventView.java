package com.khopan.homework.calendar;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

class EventView {
	static @NonNull View create(@NonNull final Context context) {
		final View view = new View(context);
		view.setBackgroundColor(0xFF00FF00);
		return view;
	}
}
