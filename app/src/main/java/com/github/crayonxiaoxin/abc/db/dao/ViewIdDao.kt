package com.github.crayonxiaoxin.abc.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.crayonxiaoxin.abc.model.ViewId

@Dao
interface ViewIdDao {

    @Query("select * from viewid")
    fun getAllObx(): LiveData<List<ViewId>>

    @Query("select * from viewid")
    fun getAll(): List<ViewId>

    @Query("select * from viewid where type = :type")
    fun getAllByType(type:Int): List<ViewId>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg viewid: ViewId)

    @Delete
    suspend fun delete(vararg viewid: ViewId)

    @Query("delete from viewid")
    suspend fun deleteAll()
}