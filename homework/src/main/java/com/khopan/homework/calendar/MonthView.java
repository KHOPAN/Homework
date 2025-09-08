package com.khopan.homework.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class MonthView extends View {
	private final Context context;
	private final Supplier<Double> splitSupplier;
	private final Paint paint;

	public MonthView(@NonNull final Context context, final Supplier<Double> splitSupplier) {
		super(context);
		this.context = context;
		this.splitSupplier = splitSupplier;
		this.paint = new Paint();
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		final int width = this.getWidth();
		final int height = this.getHeight();
		final double cellWidth = ((double) width) / 7.0d;
		final double cellHeight = Math.max(height, this.splitSupplier.get()) / 6.0d;

		for(int y = 0; y < 6; y++) {
			for(int x = 0; x < 7; x++) {
				this.renderDay(new Random(y * 6 + x), canvas, (int) Math.round(cellWidth * ((double) x)), (int) Math.round(cellHeight * ((double) y)), (int) Math.round(cellWidth * ((double) (x + 1))), (int) Math.round(cellHeight * ((double) (y + 1))));
			}
		}
	}

	private void renderDay(Random random, final Canvas canvas, final double left, final double top, final double right, final double bottom) {
		this.paint.setColor(random.nextInt(0xFFFFFF + 1) | 0xFF000000);
		canvas.drawRect((float) left, (float) top, (float) right, (float) bottom, this.paint);
		this.paint.setColor(0xFF000000);
		this.paint.setTextSize(50.0f);
		canvas.drawText("Hello", (float) left, (float) top, this.paint);
	}
}
