package com.github.crayonxiaoxin.abc.db

import com.github.crayonxiaoxin.abc.App
import com.github.crayonxiaoxin.abc.model.Keyword
import com.github.crayonxiaoxin.abc.model.Options
import com.github.crayonxiaoxin.abc.model.ViewId
import com.github.crayonxiaoxin.abc.utils._e
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Repository {
    private val db get() = App.db
    val keywordDao = db.keywordDao()
    val logDao = db.logDao()
    val viewIdDao = db.viewIdDao()
    val optionsDao = db.optionsDao()

    // 匹配的关键词
    private val matchKeywords = arrayListOf(
        "跳过",
        "跳過",
    )

    // 匹配的id
    private val matchViewIds = arrayListOf(
        "com.byted.pangle:id/tt_splash_skip_btn", // 字节穿山甲sdk
        "com.miui.systemAdSolution:id/view_skip_button_nearby_top_right", // 小米系统广告sdk
    )

    // 忽略的id
    private val ignoreViewIds = arrayListOf(
        "tv.danmaku.bili:id/search_fake_text", // bilibili 搜索框
    )

    suspend fun init() {
        val option = initOption()
        if (option != null && option.value == "1") {
            // 已经初始化过
            mode() ?: setMode(MODE_VIEW)
        } else {
            // 标记为已初始化
            setInitOption("1")
            // 设置默认匹配模式
            setMode(MODE_VIEW)
            // 设置默认规则
            setDefaultRules()
        }
    }

    private const val OPTIONS_INIT = "init" // 是否第一次打开
    const val OPTIONS_MODE = "mode" // 点击模式：view、rect

    const val MODE_VIEW = "view" // 点击模式：view
    const val MODE_RECT = "rect" // 点击模式：rect
    val modes = listOf(MODE_VIEW, MODE_RECT)
    private fun initOption() = optionsDao.get(OPTIONS_INIT)
    private suspend fun setInitOption(value: String = "1") {
        optionsDao.insertAll(Options(key = OPTIONS_INIT, value = value))
    }

    fun isModeView(mode: String): Boolean = mode == MODE_VIEW
    fun isModeRect(mode: String): Boolean = mode == MODE_RECT

    private suspend fun mode() = withContext(Dispatchers.IO) {
        optionsDao.get(OPTIONS_MODE)
    }

    suspend fun modeValue() = mode()?.value ?: MODE_VIEW
    suspend fun setMode(mode: String) {
        withContext(Dispatchers.IO) {
            val currentMode = mode()
            val newMode = if (modes.contains(mode)) mode else MODE_VIEW
            _e(msg = "mode = $mode, $newMode")
            if (currentMode != null) {
                optionsDao.insertAll(currentMode.copy(value = newMode))
            } else {
                optionsDao.insertAll(Options(key = OPTIONS_MODE, value = newMode))
            }
        }
    }

    private suspend fun setDefaultRules() {
        // 初始化 - 默认关键词
        val keywords = matchKeywords.map { Keyword(keyword = it) }
        keywordDao.insertAll(*keywords.toTypedArray())
        // 初始化 - 默认匹配viewId 及 忽略viewId
        val matchIds = matchViewIds.map { ViewId(viewId = it, type = ViewId.TYPE_MATCH) }
        val ignoreIds = ignoreViewIds.map { ViewId(viewId = it, type = ViewId.TYPE_IGNORE) }
        val ids = matchIds + ignoreIds
        viewIdDao.insertAll(*ids.toTypedArray())
    }


}