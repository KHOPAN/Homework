package com.khopan.homework.view;

import android.annotation.SuppressLint;
import android.content.Context;
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

import dev.oneuiproject.oneui.widget.Separator;

public class EventView extends LinearLayout {
	final Separator dayView;
	final RecyclerView recyclerView;

	private final Context context;

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
		this.recyclerView.setAdapter(new Adapter());
		this.recyclerView.setLayoutManager(new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false));
		CoreLayout.forceEnableScrollbars(this.recyclerView, false, true);
		this.addView(this.recyclerView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	}

	private class Adapter extends RecyclerView.Adapter<SimpleViewHolder<CardView>> {
		@Override
		public int getItemCount() {
			return 100;
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<CardView> holder, final int position) {
			holder.itemView.resetForegroundState();
			holder.itemView.setCardLocation(position, this.getItemCount());
			holder.itemView.setSummary("Test summary");
			holder.itemView.setTitle("Hello, world!");
		}

		@NonNull
		@Override
		public SimpleViewHolder<CardView> onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			return new SimpleViewHolder<>(new CardView(EventView.this.context));
		}
	}
}
