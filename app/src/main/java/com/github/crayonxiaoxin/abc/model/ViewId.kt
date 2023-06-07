package com.github.crayonxiaoxin.abc.model

import androidx.annotation.IntRange
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * type=0, ignore
 *
 * type=1, matches
 */
@Entity
data class ViewId(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var viewId: String?,
    @IntRange(from = 0, to = 1) var type: Int = TYPE_MATCH,
) : Serializable {
    fun isTypeIgnore(): Boolean = type == TYPE_IGNORE
    fun isTypeMatch(): Boolean = type == TYPE_MATCH

    companion object {
        const val TYPE_IGNORE = 0
        const val TYPE_MATCH = 1
    }
}
