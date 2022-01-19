package com.gtera.data.local.db.dao

import androidx.room.*

@Dao
interface BaseDao<T> {
    @Delete
    fun delete(item: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<T>?)

    @Update
    fun update(item: T)
}