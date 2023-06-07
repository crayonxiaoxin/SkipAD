package com.github.crayonxiaoxin.abc.widgets

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.github.crayonxiaoxin.abc.R

/**
 * Created by Lau on 2019/4/19.
 */
class CustomSettingView : LinearLayout {
    private lateinit var view: View
    private var rightIcon = 0
    private var title: String? = null
    private var subTitle: String? = null
    private lateinit var rightIconView: ImageView
    private lateinit var titleView: TextView
    private lateinit var subTitleView: TextView
    private lateinit var sepView: View
    private var hasSep = false
    private var showRightIcon = false

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        // get attrs
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomSettingView)
        rightIcon = typedArray.getResourceId(
            R.styleable.CustomSettingView_cs_right_icon,
            R.drawable.ic_right
        )
        title = typedArray.getString(R.styleable.CustomSettingView_cs_title)
        subTitle = typedArray.getString(R.styleable.CustomSettingView_cs_sub_title)
        hasSep = typedArray.getBoolean(R.styleable.CustomSettingView_cs_has_sep, false)
        showRightIcon =
            typedArray.getBoolean(R.styleable.CustomSettingView_cs_show_right_icon, true)
        typedArray.recycle()

        // get view
        view = LayoutInflater.from(context).inflate(R.layout.custom_setting, this, false)
        rightIconView = view.findViewById(R.id.right_icon)
        titleView = view.findViewById(R.id.title)
        subTitleView = view.findViewById(R.id.sub_title)
        sepView = view.findViewById(R.id.sep)

        // bind data
        rightIconView.setImageResource(rightIcon)
        titleView.text = title
        subTitleView.text = subTitle
        sepView.visibility = if (hasSep) VISIBLE else GONE
        rightIconView.visibility = if (showRightIcon) VISIBLE else GONE

        addView(view)
    }

    fun setRightIcon(resId: Int, visible: Boolean = true): CustomSettingView {
        rightIconView.setImageResource(resId)
        rightIconView.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun setTitle(title: String?): CustomSettingView {
        titleView.text = title ?: ""
        return this
    }

    fun setContent(content: String?): CustomSettingView {
        subTitleView.text = content ?: ""
        return this
    }

    fun addSep(isAdd: Boolean): CustomSettingView {
        sepView.visibility = if (isAdd) VISIBLE else GONE
        return this
    }

    fun hideRightIcon(): CustomSettingView {
        rightIconView.visibility = GONE
        return this
    }
}