package com.khopan.homework.database.dao;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.khopan.homework.database.entity.Assignment;

import java.util.List;

@Dao
public interface AssignmentDao {
	@Query("SELECT * FROM assignment")
	List<Assignment> getAll();

	@Query("SELECT * FROM assignment ORDER BY deadline ASC")
	PagingSource<Integer, Assignment> getAllPaged();

	@Query("SELECT * FROM assignment WHERE deadline >= :start AND deadline < :end ORDER BY deadline ASC")
	List<Assignment> getRange(final long start, final long end);

	@Query("SELECT * FROM assignment WHERE deadline >= :start ORDER BY deadline ASC LIMIT 1")
	Assignment getNext(final long start);

	@Insert
	void insert(final Assignment... assignments);
}
