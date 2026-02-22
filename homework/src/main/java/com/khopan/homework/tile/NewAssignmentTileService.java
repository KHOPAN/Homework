package com.khopan.homework.tile;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.TileService;

import com.khopan.homework.activity.NewAssignmentActivity;

public class NewAssignmentTileService extends TileService {
	@SuppressLint("StartActivityAndCollapseDeprecated")
	@Override
	public void onClick() {
		final Intent intent = new Intent(this, NewAssignmentActivity.class);

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
			this.startActivityAndCollapse(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE));
		} else {
			this.startActivityAndCollapse(intent);
		}
	}
}
