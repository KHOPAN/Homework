package com.khopan.homework.database.dao;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.khopan.homework.database.entity.Assignment;

import java.util.List;

@Dao
public interface AssignmentDao {
	@Query("SELECT * FROM assignment ORDER BY deadline ASC")
	PagingSource<Integer, Assignment> allPaged();

	@Delete
	void delete(final Assignment assignment);

	@Insert
	void insert(final Assignment assignment);

	@Query("SELECT * FROM assignment WHERE deadline >= :start ORDER BY deadline ASC LIMIT 1")
	Assignment next(final long start);

	@Query("SELECT * FROM assignment WHERE deadline >= :start AND deadline < :end ORDER BY deadline ASC")
	List<Assignment> range(final long start, final long end);

	@Update
	void update(final Assignment assignment);
}
