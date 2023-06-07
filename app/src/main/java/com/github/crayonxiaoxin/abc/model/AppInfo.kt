package com.github.crayonxiaoxin.abc.model

import android.graphics.drawable.Drawable
import java.io.Serializable

data class AppInfo(
    var pkg: String,
    var name: String,
    var logo: Drawable?,
) : Serializable
