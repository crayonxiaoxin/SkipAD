package com.github.crayonxiaoxin.abc.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class AbcService : AccessibilityService() {

    private val TAG = this.javaClass.name

    // 可捕获的事件类型
    private val filterEvents = arrayListOf(
        AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED,
        AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
        AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
    )

    // 匹配的关键词
    private val skipKeywords = arrayListOf(
        "跳过",
        "跳過",
    )

    override fun onServiceConnected() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            if (filterEvents.contains(it.eventType)) {
                it.source?.let { source ->
                    skipAD(source)
                }
            }
        }
    }

    private fun printEventType(it: AccessibilityEvent) {
        val eventTypeToString = AccessibilityEvent.eventTypeToString(it.eventType)
        Log.e(TAG, "onAccessibilityEvent: $eventTypeToString")
    }

    /**
     * 跳过广告
     */
    private fun skipAD(source: AccessibilityNodeInfo) {
        for (i in 0 until source.childCount) {
            val child = source.getChild(i) ?: continue
            val text = child.text
            Log.e(TAG, "skipAD: $text，${child.packageName}, ${child.className}")
            if (text != null && matchSkipKeywords(text.toString())) {
                // 如果当前节点就是
                findSkipClickableNode(child)
                return
            } else if (child.childCount > 0) {
                // 如果当前节点不是，并且有子节点，则继续遍历
                skipAD(child)
            }
        }
    }


    /**
     * 是否匹配关键词
     */
    private fun matchSkipKeywords(text: String?): Boolean {
        if (text.isNullOrEmpty()) return false
        val replaceText = text.replace(" ", "")
        // 如果至少有一个匹配关键词，则表示匹配
        return skipKeywords.any {
            "(.*)$it(.*)".toRegex().matches(replaceText)
        }
    }

    /**
     * 查找匹配文本所在的可点击节点，执行点击操作
     */
    private fun findSkipClickableNode(child: AccessibilityNodeInfo) {
        if (child.isClickable) {
            // 如果当前节点可点击，则直接点击
            child.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            Log.e(TAG, "findSkipClickableNode: 已点击跳过")
            return
        } else {
            // 否则，继续向上查找可点击的父节点
            val parent = child.parent
            if (parent != null) {
                findSkipClickableNode(parent)
            }
        }
    }

    override fun onInterrupt() {

    }
}