package com.khopan.homework.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khopan.core.view.SimpleViewHolder;
import com.khopan.core.view.card.CardView;

import dev.oneuiproject.oneui.widget.Separator;

public class EventView extends LinearLayout {
	private final Context context;
	final RecyclerView recyclerView;

	@SuppressLint("PrivateResource")
	public EventView(final Context context) {
		super(context);
		this.context = context;
		this.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.setOrientation(LinearLayout.VERTICAL);
		final View dividerView = new View(context);
		final TypedValue value = new TypedValue();
		context.getTheme().resolveAttribute(androidx.appcompat.R.attr.switchDividerColor, value, true);
		dividerView.setBackgroundResource(value.resourceId);
		this.addView(dividerView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelSize(androidx.appcompat.R.dimen.sesl_switch_divider_height)));
		final Separator dayView = new Separator(context);
		dayView.setText("Sunday 25 Janury 2026");
		this.addView(dayView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		/*final RecyclerView eventListView = new RecyclerView(context);
		eventListView.setAdapter(new RecyclerAdapter());
		eventListView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));*/
		this.addView(/*eventListView*/this.recyclerView = new TestRecyclerView(context), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	}

	private class RecyclerAdapter extends RecyclerView.Adapter<SimpleViewHolder<CardView>> {
		@Override
		public int getItemCount() {
			return 100;
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<CardView> holder, final int position) {
			holder.itemView.setTitle("Hello, world!");
			holder.itemView.setSummary("Test summary");
		}

		@NonNull
		@Override
		public SimpleViewHolder<CardView> onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			return new SimpleViewHolder<>(new CardView(EventView.this.context));
		}
	}
}
