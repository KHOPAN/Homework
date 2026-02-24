package com.khopan.homework.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.khopan.homework.R;
import com.khopan.homework.database.HomeworkDatabase;
import com.khopan.homework.database.entity.Assignment;

public class ReminderReceiver extends BroadcastReceiver {
	public static final String NOTIFICATION_CHANNEL_IDENTIFIER = "assignment";

	@Override
	public void onReceive(final Context context, final Intent intent) {
		new Thread(() -> {
			final Assignment assignment = HomeworkDatabase.getInstance(context.getApplicationContext()).getAssignment().next(System.currentTimeMillis() / 1000);

			if(assignment == null) {
				return;
			}

			if(!assignment.notified && assignment.deadline - System.currentTimeMillis() < 86400000) {
				final Notification notification = new NotificationCompat.Builder(context, ReminderReceiver.NOTIFICATION_CHANNEL_IDENTIFIER)
						.setSmallIcon(R.drawable.launcher_foreground)
						.setContentTitle(assignment.title)
						.setContentText(context.getString(R.string.notification_content))
						.setPriority(NotificationCompat.PRIORITY_DEFAULT)
						.build();

				NotificationManagerCompat.from(context).notify(0, notification);
				assignment.notified = true;
				new Thread(() -> HomeworkDatabase.getInstance(context.getApplicationContext()).getAssignment().update(assignment)).start();
			}

			ReminderReceiver.schedule(context.getApplicationContext());
		}).start();
	}

	public static void schedule(final Context context) {
		final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, ReminderReceiver.class), PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
		final AlarmManager manager = context.getSystemService(AlarmManager.class);
		new Thread(() -> {
			final Assignment assignment = HomeworkDatabase.getInstance(context.getApplicationContext()).getAssignment().next(System.currentTimeMillis() / 1000);

			if(assignment == null) {
				manager.cancel(pendingIntent);
			} else {
				manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, (assignment.deadline - 86400) * 1000, pendingIntent);
			}
		}).start();
	}
}
