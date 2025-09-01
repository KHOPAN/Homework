package com.khopan.homework.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
	private Context context;
	private List<Calendar> days;
	private Map<String, List<String>> events; // key = yyyy-MM-dd

	DayAdapter(Context context, Calendar month) {
		this.context = context;
		this.days = buildDays(month);
		this.events = new HashMap<>();
	}

	private List<Calendar> buildDays(Calendar month) {
		List<Calendar> list = new ArrayList<>();
		Calendar cal = (Calendar) month.clone();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1; // 0=Sun
		cal.add(Calendar.DAY_OF_MONTH, -firstDayOfWeek);

		for (int i = 0; i < 42; i++) { // 6 weeks
			list.add((Calendar) cal.clone());
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		return list;
	}

	@NonNull
	@Override
	public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER);
		/*layout.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				0 // fixed height
		));*/

		TextView dayText = new TextView(context);
		dayText.setGravity(Gravity.CENTER);

		View eventBar = new View(context);
		eventBar.setBackgroundColor(Color.BLUE);
		LinearLayout.LayoutParams barParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 10);
		barParams.setMargins(10, 4, 10, 4);
		eventBar.setLayoutParams(barParams);

		layout.addView(dayText);
		//layout.addView(eventBar);

		return new DayViewHolder(layout, dayText, eventBar);
	}

	@Override
	public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
		Calendar day = days.get(position);
		holder.dayText.setText(String.valueOf(day.get(Calendar.DAY_OF_MONTH)));

		String key = String.format(Locale.US, "%04d-%02d-%02d",
				day.get(Calendar.YEAR),
				day.get(Calendar.MONTH) + 1,
				day.get(Calendar.DAY_OF_MONTH));

		if (events.containsKey(key)) {
			holder.eventBar.setVisibility(View.VISIBLE);
		} else {
			holder.eventBar.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public int getItemCount() {
		return days.size();
	}

	public void setEvents(Map<String, List<String>> events) {
		this.events = events;
		notifyDataSetChanged();
	}

	static class DayViewHolder extends RecyclerView.ViewHolder {
		TextView dayText;
		View eventBar;

		DayViewHolder(@NonNull View itemView, TextView dayText, View eventBar) {
			super(itemView);
			this.dayText = dayText;
			this.eventBar = eventBar;
		}
	}
}
