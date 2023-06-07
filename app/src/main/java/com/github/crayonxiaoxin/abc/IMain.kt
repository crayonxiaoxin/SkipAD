package com.github.crayonxiaoxin.abc

import androidx.fragment.app.Fragment

interface IMain {
    fun toPage(f: Fragment, tag: String? = null)
    fun back()
}

val Fragment.navigate get() = context as? IMain