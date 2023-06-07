package com.github.crayonxiaoxin.abc.db

import com.github.crayonxiaoxin.abc.App
import com.github.crayonxiaoxin.abc.model.Keyword
import com.github.crayonxiaoxin.abc.model.Options
import com.github.crayonxiaoxin.abc.model.ViewId

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
        val keyInit = "init"
        val option = optionsDao.get(keyInit)
        if (option != null && option.value == "1") {
            // 已经初始化过，无需理会
        } else {
            // 标记为已初始化
            optionsDao.insertAll(Options(key = keyInit, value = "1"))
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
}