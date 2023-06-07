package com.github.crayonxiaoxin.abc.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.crayonxiaoxin.abc.db.dao.KeywordDao
import com.github.crayonxiaoxin.abc.db.dao.LogDao
import com.github.crayonxiaoxin.abc.db.dao.OptionsDao
import com.github.crayonxiaoxin.abc.db.dao.ViewIdDao
import com.github.crayonxiaoxin.abc.model.Keyword
import com.github.crayonxiaoxin.abc.model.Log
import com.github.crayonxiaoxin.abc.model.Options
import com.github.crayonxiaoxin.abc.model.ViewId

@Database(
    entities = [Options::class, Log::class, Keyword::class, ViewId::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
    ]
)
public abstract class MyRoomDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao
    abstract fun keywordDao(): KeywordDao
    abstract fun viewIdDao(): ViewIdDao
    abstract fun optionsDao(): OptionsDao

    companion object {
        @Volatile
        private var INSTANCE: MyRoomDatabase? = null

        fun getDatabase(context: Context): MyRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyRoomDatabase::class.java,
                    "abc_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}