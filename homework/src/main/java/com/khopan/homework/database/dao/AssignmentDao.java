package com.khopan.homework.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.khopan.homework.database.entity.Assignment;

import java.util.List;

@Dao
public interface AssignmentDao {
	@Query("SELECT * FROM assignment")
	List<Assignment> getAll();

	@Insert
	void insert(final Assignment... assignments);
}
