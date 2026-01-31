package com.khopan.homework.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager2.widget.ViewPager2;

import com.khopan.core.CoreLayout;
import com.khopan.core.view.SimpleViewHolder;
import com.khopan.core.view.card.CardView;

import java.util.Objects;
import java.util.function.Consumer;

import dev.oneuiproject.oneui.widget.Separator;

public class EventView extends LinearLayout {
	private final Context context;
	private final RecyclerView recyclerView;

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

	public static ViewPager2 create(final Context context, final Consumer<RecyclerView> consumer) {
		final ViewPager2 pagerView = new ViewPager2(context);
		final RecyclerView.LayoutManager layoutManager = ((RecyclerView) pagerView.getChildAt(0)).getLayoutManager();
		assert(layoutManager != null);
		pagerView.setAdapter(new EventView.PagerAdapter(context, null));
		pagerView.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
			@Override
			public void onPageSelected(final int position) {
				Log.d("EventView", "onPageSelected(" + position + ")");
				consumer.accept(((EventView) Objects.requireNonNull(layoutManager.findViewByPosition(position))).recyclerView);
			}
		});

		return pagerView;
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

	private static class PagerAdapter extends RecyclerView.Adapter<SimpleViewHolder<EventView>> {
		//private final ViewPager2 pagerView;
		private final Context context;
		private final ViewPager2.OnPageChangeCallback callback;

		private PagerAdapter(/*final ViewPager2 pagerView, */final Context context, final ViewPager2.OnPageChangeCallback callback) {
			//this.pagerView = pagerView;
			this.context = /*this.pagerView.getContext();*/context;
			this.callback = callback;
		}

		@Override
		public int getItemCount() {
			return 32;
		}

		@Override
		public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
			if(this.callback != null) {
				//this.pagerView.registerOnPageChangeCallback(this.callback);
			}
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<EventView> holder, final int position) {

		}

		@NonNull
		@Override
		public SimpleViewHolder<EventView> onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			return new SimpleViewHolder<>(new EventView(this.context));
		}

		@Override
		public void onDetachedFromRecyclerView(@NonNull final RecyclerView recyclerView) {
			if(this.callback != null) {
				//this.pagerView.unregisterOnPageChangeCallback(this.callback);
			}
		}
	}
}
