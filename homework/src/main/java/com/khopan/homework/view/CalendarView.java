package com.khopan.homework.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.khopan.core.view.SimpleViewHolder;

public class CalendarView extends View {
	private final float strokeWidth;
	private final float arcSize;
	private final RectF outerBounds;
	private final RectF innerBounds;
	private final Paint paint;

	private int width;
	private int height;
	private float cellWidth;

	public CalendarView(final Context context) {
		super(context);
		final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		this.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.0f, metrics);
		this.arcSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, metrics);
		this.outerBounds = new RectF();
		this.innerBounds = new RectF();
		this.paint = new Paint();
		this.paint.setColor(Color.RED);
		Log.d("CalendarView", "Stroke width: " + this.strokeWidth + " Arc size: " + this.arcSize + " Density: " + metrics.density);
	}

	@Override
	protected void onDraw(@NonNull final Canvas canvas) {
		for(int x = 4; x < 5; x++) {
			final float left = this.cellWidth * x;
			final float right = left + this.cellWidth + this.strokeWidth;
			this.drawCell(canvas, left, 100.0f, right, 200.0f);
		}
	}

	@Override
	protected void onSizeChanged(final int width, final int height, final int oldWidth, final int oldHeight) {
		this.width = width;
		this.height = height;
		this.cellWidth = (this.width - this.strokeWidth * 8.0f) / 7.0f + this.strokeWidth;
	}

	private void drawCell(final Canvas canvas, final float left, final float top, final float right, final float bottom) {
		this.outerBounds.left = left;
		this.outerBounds.top = top;
		this.outerBounds.right = right;
		this.outerBounds.bottom = bottom;
		this.innerBounds.left = left + this.strokeWidth;
		this.innerBounds.top = top + this.strokeWidth;
		this.innerBounds.right = right - this.strokeWidth;
		this.innerBounds.bottom = bottom - this.strokeWidth;

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			final float innerArcSize = this.arcSize - this.strokeWidth * 2.0f;
			canvas.drawDoubleRoundRect(this.outerBounds, this.arcSize, this.arcSize, this.innerBounds, innerArcSize, innerArcSize, this.paint);
		}
	}

	/*public static class Adapter extends RecyclerView.Adapter<ViewHolder> {
		private final ViewPager2.OnPageChangeCallback callback;

		public Adapter(final ViewPager2 viewPager) {
			this.callback = new ViewPager2.OnPageChangeCallback() {
				@Override
				public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

				}
			};
		}

		@Override
		public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
			this.viewPager.registerOnPageChangeCallback(this.callback);
		}

		@Override
		public void onDetachedFromRecyclerView(@NonNull final RecyclerView recyclerView) {
			this.viewPager.unregisterOnPageChangeCallback(this.callback);
		}
	}*/

	public static ViewPager2 create(final Context context) {
		final ViewPager2 pagerView = new ViewPager2(context);
		pagerView.setAdapter(new CalendarViewPagerAdapter(context));
		pagerView.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
		return pagerView;
	}

	private static class CalendarViewPagerAdapter extends RecyclerView.Adapter<SimpleViewHolder<CalendarView>> {
		private final Context context;

		private CalendarViewPagerAdapter(final Context context) {
			this.context = context;
		}

		@Override
		public int getItemCount() {
			return 13;
		}

		@Override
		public void onBindViewHolder(@NonNull final SimpleViewHolder<CalendarView> holder, final int position) {

		}

		@NonNull
		@Override
		public SimpleViewHolder<CalendarView> onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			final CalendarView calendarView = new CalendarView(this.context);
			calendarView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			return new SimpleViewHolder<>(calendarView);
		}
	}
}
