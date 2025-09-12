package com.khopan.homework.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.khopan.homework.AbstractFragment;
import com.khopan.homework.calendar.EventCalendarView;
import com.sec.sesl.khopan.homework.R;

public class HomeworkFragment extends AbstractFragment {
	public HomeworkFragment() {
		super(R.drawable.ic_oui_alarm, R.string.applicationName);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle bundle) {
		return new EventCalendarView(this.getContext());
	}

	/*private static class CalendarAdapter extends RecyclerView.Adapter<CalendarHolder> {
		private final CalendarLayout layout;

		private CalendarAdapter(final CalendarLayout layout) {
			this.layout = layout;
		}

		@Override
		public int getItemCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public void onBindViewHolder(@NonNull final CalendarHolder holder, final int position) {

		}

		@NonNull
		@Override
		public CalendarHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
			return new CalendarHolder(this.layout);
		}
	}

	private static class CalendarView extends View {
		private final CalendarLayout layout;
		private final Paint paint;

		public CalendarView(final CalendarLayout layout) {
			super(layout.context);
			this.layout = layout;
			this.paint = new Paint();
			this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		}

		@Override
		protected void onDraw(final Canvas canvas) {
			final int width = this.getWidth();
			final int height = this.getHeight();
			final double cellWidth = ((double) width) / 7.0d;
			final double cellHeight = Math.max(height, this.layout.separatorY) / 6.0d;

			for(int y = 0; y < 6; y++) {
				for(int x = 0; x < 7; x++) {
					this.renderDay(new Random(y * 6 + x), canvas, (int) Math.round(cellWidth * ((double) x)), (int) Math.round(cellHeight * ((double) y)), (int) Math.round(cellWidth * ((double) (x + 1))), (int) Math.round(cellHeight * ((double) (y + 1))));
				}
			}
		}

		private void renderDay(Random random, final Canvas canvas, final double left, final double top, final double right, final double bottom) {
			this.paint.setColor(random.nextInt(0xFFFFFF + 1) | 0xFF000000);
			this.paint.setStyle(Paint.Style.FILL);
			canvas.drawRect((float) left, (float) top, (float) right, (float) bottom, this.paint);
			this.paint.setColor(0xFF000000);
			this.paint.setTextSize(50.0f);
			canvas.drawText("Hello", (float) left, (float) top, this.paint);
			this.paint.setColor(0xFFFF0000);
			this.paint.setStyle(Paint.Style.STROKE);
			this.paint.setStrokeWidth(4.0f);
			canvas.drawRoundRect((float) left, (float) top, (float) right, (float) bottom, 20.0f, 20.0f, this.paint);
		}
	}

	private static class CalendarHolder extends RecyclerView.ViewHolder {
		private CalendarHolder(final CalendarLayout layout) {
			super(new CalendarView(layout));
		}
	}

	private static class CalendarLayout extends ViewGroup {

	}*/
}
