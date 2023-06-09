package com.github.crayonxiaoxin.abc

import android.app.Application
import android.content.Context
import com.github.crayonxiaoxin.abc.db.MyRoomDatabase
import com.github.crayonxiaoxin.abc.db.Repository
import com.github.crayonxiaoxin.abc.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {
    private val database by lazy { MyRoomDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        Logger.provide(BuildConfig.DEBUG)
//        Logger.provide(false)
        appContext = this.applicationContext
        db = database
        CoroutineScope(Dispatchers.IO).launch {
            Repository.init()
        }
    }

    companion object {
        lateinit var appContext: Context
        lateinit var db: MyRoomDatabase
    }
}