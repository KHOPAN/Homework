package com.khopan.homework.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class CalendarView extends View {
	private final Paint paint;

	private int width;
	private int height;

	public CalendarView(final Context context) {
		super(context);
		this.paint = new Paint();
		this.paint.setColor(Color.BLUE);
		this.paint.setStyle(Paint.Style.STROKE);
	}

	@Override
	protected void onDraw(@NonNull final Canvas canvas) {
		canvas.drawRect(0.0f, 0.0f, this.width, this.height, this.paint);
	}

	@Override
	protected void onSizeChanged(final int width, final int height, final int oldWidth, final int oldHeight) {
		this.width = width;
		this.height = height;
	}

	public static class Adapter extends RecyclerView.Adapter<ViewHolder> {
		private final ViewPager2 viewPager;
		private final Context context;
		private final ViewPager2.OnPageChangeCallback callback;

		public Adapter(final ViewPager2 viewPager) {
			this.viewPager = viewPager;
			this.context = this.viewPager.getContext();
			this.callback = new ViewPager2.OnPageChangeCallback() {
				@Override
				public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

				}
			};
		}

		@Override
		public int getItemCount() {
			return 100;
		}

		@Override
		public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
			this.viewPager.registerOnPageChangeCallback(this.callback);
		}

		@Override
		public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

		}

		@Override
		public void onDetachedFromRecyclerView(@NonNull final RecyclerView recyclerView) {
			this.viewPager.unregisterOnPageChangeCallback(this.callback);
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			final CalendarView view = new CalendarView(this.context);
			view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			return new ViewHolder(view);
		}
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(@NonNull final View itemView) {
			super(itemView);
		}
	}
}
