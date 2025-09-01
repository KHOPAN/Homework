package com.khopan.homework.calendar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthViewHolder> {
	private Context context;
	private Calendar startCalendar;

	public MonthAdapter(Context context, Calendar startCalendar) {
		this.context = context;
		this.startCalendar = (Calendar) startCalendar.clone();
	}

	@NonNull
	@Override
	public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		RecyclerView rv = new RecyclerView(context);
		rv.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT
		));
		rv.setLayoutManager(new GridLayoutManager(context, 7) {
			@Override
			public boolean checkLayoutParams(RecyclerView.LayoutParams params) {
				params.width = this.getWidth() / (this.getSpanCount() - 1);
				params.height = this.getHeight() / (this.getSpanCount() - 1);
				return true;
			}
		}); // 7 days
		return new MonthViewHolder(rv);
	}

	@Override
	public void onBindViewHolder(@NonNull MonthViewHolder holder, int position) {
		Calendar month = (Calendar) startCalendar.clone();
		month.add(Calendar.MONTH, position - 500); // offset, center at ~500
		holder.bind(month);
	}

	@Override
	public int getItemCount() {
		return 1000; // 1000 months -> ~83 years
	}

	class MonthViewHolder extends RecyclerView.ViewHolder {
		RecyclerView recyclerView;

		MonthViewHolder(@NonNull View itemView) {
			super(itemView);
			recyclerView = (RecyclerView) itemView;
		}

		void bind(Calendar month) {
			recyclerView.setAdapter(new DayAdapter(context, month));
		}
	}
}
