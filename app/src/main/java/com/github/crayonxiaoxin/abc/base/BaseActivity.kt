package com.github.crayonxiaoxin.abc.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.crayonxiaoxin.abc.R


/**
 * Created by Lau on 2020-01-20.
 */
abstract class BaseActivity : AppCompatActivity() {

    open val TAG = logTag()

    private fun logTag(): String {
        var tag = this.javaClass.simpleName
        if (tag.length > 23) {
            tag = tag.substring(0, 23)
        }
        return tag
    }

    // 非EditText获取焦点时，隐藏键盘
//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        if (ev.action == MotionEvent.ACTION_DOWN) {
//            val v = currentFocus
//            // EditText失去焦点时隐藏键盘
//            if (v is EditText) {
//                val outRect = Rect()
//                v.getGlobalVisibleRect(outRect)
//                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
//                    v.clearFocus()
//                    v.hideSoftInput()
//                }
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//    }

    // 替换fragment
    @JvmOverloads
    protected fun replaceFragment(
        frameLayoutId: Int,
        f: Fragment,
        tag: String? = null,
        addToBackStack: Boolean = true,
        anim: Boolean = false
    ) {
        val tag = tag ?: f.javaClass.name
        val fm = supportFragmentManager
        val f1 = fm.findFragmentByTag(tag)
        with(fm.beginTransaction()) {
            if (anim) {
                setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
            }
            if (f1 == null) {
                replace(frameLayoutId, f, tag)
                if (addToBackStack) {
                    addToBackStack(tag)
                }
                commit()
            } else {
                fm.popBackStack(tag, 0)
            }
        }
    }

    // 添加fragment
    @JvmOverloads
    protected fun addFragment(
        frameLayoutId: Int,
        f: Fragment,
        tag: String? = null,
        anim: Boolean = true,
        addToBackStack: Boolean = true,
        allowingStateLoss: Boolean = false
    ) {
        val tag = tag ?: f.javaClass.name
        val fm = supportFragmentManager
        val f1 = fm.findFragmentByTag(tag)
        val currentFragment = fm.findFragmentById(frameLayoutId)
        with(fm.beginTransaction()) {
            if (anim) {
                setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
            }
            if (currentFragment != null) this.hide(currentFragment)
            if (f1 == null) {
                add(frameLayoutId, f, tag)
                if (addToBackStack) {
                    addToBackStack(tag)
                }
                if (allowingStateLoss) {
                    commitAllowingStateLoss()
                } else {
                    commit()
                }
            } else {
                fm.popBackStack(tag, 0)
                this.show(f1)
            }
        }
    }
}