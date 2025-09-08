package com.khopan.homework.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Random;

public class MonthView extends View {
	private final Context context;
	private final Paint paint;

	public MonthView(@NonNull final Context context) {
		super(context);
		this.context = context;
		this.paint = new Paint();
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		final int width = this.getWidth();
		final int height = this.getHeight();
		final double cellWidth = ((double) width) / 7.0d;
		final double cellHeight = ((double) height) / 6.0d;

		for(int y = 0; y < 6; y++) {
			for(int x = 0; x < 7; x++) {
				this.paint.setColor(new Random(y * 6 + x).nextInt(0xFFFFFF + 1) | 0xFF000000);
				canvas.drawRect((int) Math.round(cellWidth * ((double) x)), (int) Math.round(cellHeight * ((double) y)), (int) Math.round(cellWidth * ((double) (x + 1))), (int) Math.round(cellHeight * ((double) (y + 1))), this.paint);
			}
		}
	}
}
