package com.khopan.homework.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.khopan.homework.database.dao.AssignmentDao;
import com.khopan.homework.database.entity.Assignment;

@Database(entities={Assignment.class}, version=1)
public abstract class HomeworkDatabase extends RoomDatabase {
	private static final Object INSTANCE_LOCK = new Object();

	private static HomeworkDatabase Instance;

	public abstract AssignmentDao getAssignment();

	public static HomeworkDatabase getInstance(final Context context) {
		if(HomeworkDatabase.Instance != null) {
			return HomeworkDatabase.Instance;
		}

		synchronized(HomeworkDatabase.INSTANCE_LOCK) {
			if(HomeworkDatabase.Instance == null) {
				HomeworkDatabase.Instance = Room.databaseBuilder(context, HomeworkDatabase.class, "homework").build();
			}

			return HomeworkDatabase.Instance;
		}
	}
}
