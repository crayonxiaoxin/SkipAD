package com.github.crayonxiaoxin.abc.base

import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    open val TAG = logTag()

    private fun logTag(): String {
        var tag = this.javaClass.simpleName
        if (tag.length > 23) {
            tag = tag.substring(0, 23)
        }
        return tag
    }

    open fun onBackPressed(): Boolean {
        return true
    }

}