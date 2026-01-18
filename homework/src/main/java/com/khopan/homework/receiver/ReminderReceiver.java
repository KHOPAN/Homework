package com.khopan.homework.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReminderReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, final Intent intent) {
		Log.d("Homework", "onReceive()");
	}
}
