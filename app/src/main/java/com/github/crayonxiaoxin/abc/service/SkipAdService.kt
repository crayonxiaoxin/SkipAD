package com.github.crayonxiaoxin.abc.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.ComponentName
import android.graphics.Path
import android.graphics.Rect
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.webkit.WebView
import android.widget.GridView
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.github.crayonxiaoxin.abc.db.Repository
import com.github.crayonxiaoxin.abc.model.Keyword
import com.github.crayonxiaoxin.abc.model.Log
import com.github.crayonxiaoxin.abc.model.ViewId
import com.github.crayonxiaoxin.abc.utils._e
import com.github.crayonxiaoxin.abc.utils.getAppInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SkipAdService : AccessibilityService() {

    private val TAG = this.javaClass.name

    // 可捕获的事件类型
    private val filterEvents = arrayListOf(
        AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED,
        AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
        AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
    )

    // 匹配 - 关键词
    private var _matchKeywords = listOf<Keyword>()

    // 匹配 - viewId
    private var _matchViewIds = listOf<ViewId>()

    // 忽略 - viewId
    private var _ignoreViewIds = listOf<ViewId>()

    private var _mode = Repository.MODE_VIEW

    private val serviceScope = CoroutineScope(Job() + Dispatchers.IO)

    override fun onServiceConnected() {
        serviceScope.launch(Dispatchers.Main) {
            Repository.keywordDao.getAllObx().observeForever {
                _matchKeywords = it
            }
        }
        serviceScope.launch(Dispatchers.Main) {
            Repository.viewIdDao.getAllObx().observeForever {
                _matchViewIds = it.filter { it.isTypeMatch() }
                _ignoreViewIds = it.filter { it.isTypeIgnore() }
            }
        }
        serviceScope.launch(Dispatchers.Main) {
            Repository.optionsDao.getObx(Repository.OPTIONS_MODE).observeForever {
                _mode = it?.value ?: Repository.MODE_VIEW
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            // 匹配事件类型 并且 不是自己
            if (it.packageName != packageName && filterEvents.contains(it.eventType)) {
                it.source?.let { source ->
                    skipAD(source)
                }
            }
        }
    }

    private fun getActivityInfo(it: AccessibilityEvent) {
        val componentName = ComponentName(it.packageName.toString(), it.className.toString())
        try {
            val activityInfo = packageManager.getActivityInfo(componentName, 0)
            _e(TAG, "onAccessibilityEvent: ${activityInfo.name}, $activityInfo")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun printEventType(it: AccessibilityEvent) {
        val eventTypeToString = AccessibilityEvent.eventTypeToString(it.eventType)
        _e(TAG, "onAccessibilityEvent: [事件] $eventTypeToString")
    }

    /**
     * 跳过广告
     */
    private fun skipAD(source: AccessibilityNodeInfo) {
        if (source.childCount == 0) return
        val pkg = source.packageName.toString()
        val appName = getAppInfo(pkg)?.name ?: ""
        for (i in 0 until source.childCount) {
            val child = source.getChild(i) ?: continue
            val text = child.text
            val viewId = child.viewIdResourceName
            val isTextView = TextView::class.java.name == child.className
            _e(
                TAG,
                "skipAD: $text，${pkg}, ${child.className}, ${child.viewIdResourceName}"
            )
            if (isIgnoreViewId(viewId)) {
                _e(TAG, "skipAD: [忽略ID] --- $viewId")
                serviceScope.launch {
                    Repository.logDao.insertAll(
                        Log(
                            skip = false,
                            pkg = pkg,
                            app_name = appName,
                            viewId = viewId
                        )
                    )
                    cancel()
                }
                return
            } else if (viewId != null && matchSkipViewIds(viewId)) {
                // 匹配 viewId
                val isSkip = handleSkip(child)
                _e(TAG, "skipAD: [匹配ID] --- $viewId, ${if (isSkip) "已跳过" else "未能跳过"}")
                serviceScope.launch {
                    Repository.logDao.insertAll(
                        Log(
                            skip = isSkip,
                            pkg = pkg,
                            app_name = appName,
                            viewId = viewId
                        )
                    )
                }
                return
            } else if (isIgnoreItem(child)) {
                // 匹配文字：忽略列表项，输入框以及 webView 内容
                return
            } else if (text != null && isTextView && matchSkipKeywords(text.toString())) {
                // 匹配文字（可能误触）：不能是列表项，输入框以及 webView 内容
                val isSkip = handleSkip(child)
                _e(
                    TAG, "skipAD: [匹配文字] --- $text, ${if (isSkip) "已跳过" else "未能跳过"}"
                )
                serviceScope.launch {
                    Repository.logDao.insertAll(
                        Log(
                            skip = isSkip,
                            pkg = pkg,
                            app_name = appName,
                            keyword = text.toString()
                        )
                    )
                }
                return
            } else {
                skipAD(child)
            }
        }
    }

    private fun isIgnoreViewId(viewId: String?): Boolean {
        if (viewId.isNullOrEmpty()) return false
        val replaceText = viewId.replace(" ", "")
        // 如果至少有一个匹配关键词，则表示匹配
        return _ignoreViewIds.any {
            !it.viewId.isNullOrEmpty() && it.viewId == replaceText
        }
    }

    /**
     * 忽略的父级容器
     */
    private fun isIgnoreItem(child: AccessibilityNodeInfo): Boolean {
        val parent = child.parent ?: return false
        val isRecyclerView = RecyclerView::class.java.name == parent.className
        val isListView = ListView::class.java.name == parent.className
        val isGridView = GridView::class.java.name == parent.className
        val isScrollView = ScrollView::class.java.name == parent.className
        val isWebView = WebView::class.java.name == parent.className
        return isRecyclerView || isListView || isGridView || isScrollView || isWebView
    }

    /**
     * 执行跳过操作
     */
    private fun handleSkip(child: AccessibilityNodeInfo): Boolean {
        // 如果当前节点就是
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (Repository.isModeRect(_mode)) {
                clickRect2Skip(child)
            } else {
                findSkipClickableNode(child)
            }
            true
        } else {
            findSkipClickableNode(child)
        }
    }


    /**
     * 是否匹配关键词
     */
    private fun matchSkipKeywords(text: String?): Boolean {
        if (text.isNullOrEmpty()) return false
        val replaceText = text.replace(" ", "")
        // 如果至少有一个匹配关键词，则表示匹配
        return _matchKeywords.any {
            !it.keyword.isNullOrEmpty() && "(.*)${it.keyword}(.*)".toRegex()
                .matches(replaceText)
        }
    }

    /**
     * 是否匹配 viewId
     */
    private fun matchSkipViewIds(viewId: String?): Boolean {
        if (viewId.isNullOrEmpty()) return false
        val replaceText = viewId.replace(" ", "")
        // 如果至少有一个匹配关键词，则表示匹配
        return _matchViewIds.any {
            !it.viewId.isNullOrEmpty() && it.viewId == replaceText
        }
    }

    /**
     * 查找匹配文本所在的可点击节点，执行点击操作
     */
    private fun findSkipClickableNode(child: AccessibilityNodeInfo): Boolean {
        if (child.isClickable && child.isVisibleToUser) {
            // 如果当前节点可点击，则直接点击
            child.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            return true
        } else {
            // 否则，继续向上查找可点击的父节点
            val parent = child.parent
            if (parent != null) {
                return findSkipClickableNode(parent)
            }
            return false
        }
    }

    /**
     * 找到对应节点的 rect 并模拟点击
     *
     * 优点：如果文本所在节点不是可点击的（可能点击监听是父节点），此时可以通过点击当前节点所在区域实现
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun clickRect2Skip(child: AccessibilityNodeInfo) {
        val rect = Rect()
        child.getBoundsInScreen(rect)
        val path = Path()
        path.moveTo(rect.centerX().toFloat(), rect.centerY().toFloat())
        val builder = GestureDescription.Builder()
        builder.addStroke(GestureDescription.StrokeDescription(path, 0, 10))
        dispatchGesture(builder.build(), null, null)
        _e(TAG, "clickRect2Skip: 已点击跳过")
    }

    override fun onInterrupt() {

    }
}