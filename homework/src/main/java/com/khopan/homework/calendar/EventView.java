package com.khopan.homework.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khopan.core.CoreLayout;
import com.khopan.core.view.SimpleViewHolder;
import com.khopan.core.view.card.CardView;
import com.khopan.homework.HomeworkApplication;
import com.khopan.homework.database.entity.Assignment;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import dev.oneuiproject.oneui.widget.Separator;

public class EventView extends LinearLayout {
	final Separator dayView;
	final RecyclerView recyclerView;

	private final Context context;
	private final Adapter adapter;

	private List<Assignment> list;

	@SuppressLint("PrivateResource")
	public EventView(final Context context) {
		super(context);
		this.context = context;
		this.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.setOrientation(LinearLayout.VERTICAL);
		final View dividerView = new View(this.context);
		final TypedValue value = new TypedValue();
		this.context.getTheme().resolveAttribute(androidx.appcompat.R.attr.switchDividerColor, value, true);
		dividerView.setBackgroundResource(value.resourceId);
		this.addView(dividerView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, this.context.getResources().getDimensionPixelSize(androidx.appcompat.R.dimen.sesl_switch_divider_height)));
		this.addView(this.dayView = new Separator(this.context), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		this.recyclerView = new RecyclerView(this.context);
		this.recyclerView.setAdapter(this.adapter = new Adapter());
		this.recyclerView.setLayoutManager(new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false));
		CoreLayout.forceEnableScrollbars(this.recyclerView, false, true);
		this.addView(this.recyclerView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	}

	void setDate(final LocalDate date) {
		final long start = (date.toEpochDay() - EventCalendarView.EPOCH_DAY.toEpochDay()) * 1440;
		new Thread(() -> {
			final List<Assignment> list = HomeworkApplication.Database.getAssignment().getRange(start, start + 1440);
			Log.d("EventView", "Query done: " + list.size());
			this.post(() -> {
				this.list = list;
				this.adapter.notifyDataSetChanged();
			});
		}).start();
	}

	private class Adapter extends RecyclerView.Adapter<SimpleViewHolder<CardView>> {
		@Override
		public int getItemCount() {
			return EventView.this.list == null ? 0 : EventView.this.list.size();
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<CardView> holder, final int position) {
			final Assignment assignment = EventView.this.list.get(position);
			holder.itemView.resetForegroundState();
			holder.itemView.setSummary(String.valueOf(EventCalendarView.EPOCH_TIME.plusMinutes(assignment.deadline).getHour()));
			holder.itemView.setTitle(assignment.title);
			holder.itemView.setTopDividerVisible(position != 0);
		}

		@NonNull
		@Override
		public SimpleViewHolder<CardView> onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			final CardView view = new CardView(EventView.this.context);
			view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			return new SimpleViewHolder<>(view);
		}
	}
}
