package com.khopan.homework.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.khopan.homework.database.dao.AssignmentDao;
import com.khopan.homework.database.entity.Assignment;

@Database(entities={Assignment.class}, version=1)
public abstract class HomeworkDatabase extends RoomDatabase {
	public abstract AssignmentDao getAssignment();
}
