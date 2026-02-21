package com.khopan.homework.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Assignment {
	@PrimaryKey
	public int identifier;

	@ColumnInfo(name="title")
	public String title;

	@ColumnInfo(name="deadline")
	public long deadline;
}
