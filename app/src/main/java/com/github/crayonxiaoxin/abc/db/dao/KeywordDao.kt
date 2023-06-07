package com.github.crayonxiaoxin.abc.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.crayonxiaoxin.abc.model.Keyword

@Dao
interface KeywordDao {

    @Query("select * from keyword")
    fun getAllObx(): LiveData<List<Keyword>>

    @Query("select * from keyword")
    fun getAll(): List<Keyword>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg keyword: Keyword)

    @Delete
    suspend fun delete(vararg keyword: Keyword)

    @Query("delete from keyword")
    suspend fun deleteAll()
}