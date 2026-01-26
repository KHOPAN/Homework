package com.khopan.homework.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.khopan.core.view.SimpleViewHolder;

public class EventView extends LinearLayout {
	@SuppressLint("PrivateResource")
	public EventView(final Context context) {
		super(context);
		this.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.setOrientation(LinearLayout.VERTICAL);
		final View dividerView = new View(context);
		final TypedValue value = new TypedValue();
		context.getTheme().resolveAttribute(androidx.appcompat.R.attr.switchDividerColor, value, true);
		dividerView.setBackgroundResource(value.resourceId);
		this.addView(dividerView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelSize(androidx.appcompat.R.dimen.sesl_switch_divider_height)));
		final TextView dayView = new TextView(context);
		dayView.setText("Sunday 25 Janury 2026");
		this.addView(dayView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		this.addView(new TestRecyclerView(context), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	}

	public static class Adapter extends RecyclerView.Adapter<SimpleViewHolder<EventView>> {
		private final ViewPager2 pagerView;
		private final Context context;
		private final ViewPager2.OnPageChangeCallback callback;

		public Adapter(final ViewPager2 pagerView, final ViewPager2.OnPageChangeCallback callback) {
			this.pagerView = pagerView;
			this.context = this.pagerView.getContext();
			this.callback = callback;
		}

		@Override
		public int getItemCount() {
			return 32;
		}

		@Override
		public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
			if(this.callback != null) {
				this.pagerView.registerOnPageChangeCallback(this.callback);
			}
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<EventView> holder, final int position) {

		}

		@Override
		public void onDetachedFromRecyclerView(@NonNull final RecyclerView recyclerView) {
			if(this.callback != null) {
				this.pagerView.unregisterOnPageChangeCallback(this.callback);
			}
		}

		@NonNull
		@Override
		public SimpleViewHolder<EventView> onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			return new SimpleViewHolder<>(new EventView(this.context));
		}
	}
}
