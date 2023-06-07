package com.github.crayonxiaoxin.abc.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Options(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var key: String = "",
    var value: String = "",
) : Serializable
