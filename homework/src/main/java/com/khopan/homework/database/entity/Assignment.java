package com.khopan.homework.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Assignment {
	@PrimaryKey(autoGenerate=true)
	public Integer identifier;

	@ColumnInfo(name="title")
	public String title;

	@ColumnInfo(name="subject")
	public int subject;

	@ColumnInfo(name="done")
	public boolean done;

	@ColumnInfo(name="deadline")
	public long deadline;
}
