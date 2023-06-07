package com.github.crayonxiaoxin.abc.model

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.crayonxiaoxin.abc.utils.fmt
import com.github.crayonxiaoxin.abc.utils.getAppInfo
import java.io.Serializable
import java.util.Date

@Entity
data class Log(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var skip: Boolean = true,
    var pkg: String = "",
    @ColumnInfo(defaultValue = "") var app_name: String = "",
    var keyword: String = "",
    var viewId: String = "",
    var dt: String = Date().fmt()
) : Serializable {
    fun isTypeViewId(): Boolean = viewId.isNotEmpty()
    fun isTypeKeyword(): Boolean = keyword.isNotEmpty()

    fun typeName(): String {
        if (isTypeKeyword()) {
            return if (skip) "匹配关键词" else "未能跳过"
        } else if (isTypeViewId()) {
            return if (skip) "匹配ViewId" else "忽略ViewId"
        }
        return ""
    }

    fun desc(): String {
        if (isTypeKeyword()) {
            return keyword
        } else if (isTypeViewId()) {
            return viewId
        }
        return ""
    }

    fun logo(context: Context):Drawable?{
        return context.getAppInfo(pkg)?.logo
    }
}
