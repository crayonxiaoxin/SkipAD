package com.github.crayonxiaoxin.abc.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.crayonxiaoxin.abc.model.Log

@Dao
interface LogDao {

    @Query("select distinct * from log")
    fun getAllObx(): LiveData<List<Log>>

    @Query("select distinct * from log")
    fun getAll(): List<Log>

    @Query("select count(id) from (select distinct * from log)")
    fun getSize(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg log: Log)

    @Delete
    suspend fun delete(vararg log: Log)

    @Query("delete from log")
    suspend fun deleteAll()
}