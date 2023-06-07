package com.github.crayonxiaoxin.abc.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.crayonxiaoxin.abc.model.Options

@Dao
interface OptionsDao {

    @Query("select * from options")
    fun getAllObx(): LiveData<List<Options>>

    @Query("select * from options")
    fun getAll(): List<Options>

    @Query("select * from options where `key` = :key")
    fun get(key: String): Options?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg options: Options)

    @Delete
    suspend fun delete(vararg options: Options)

    @Query("delete from options")
    suspend fun deleteAll()
}