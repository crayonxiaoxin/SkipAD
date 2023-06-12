package com.github.crayonxiaoxin.abc.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableSharedFlow

object FlowBus {
    private val flowEvents: HashMap<String, MutableSharedFlow<Any>> = HashMap()

    suspend fun post(eventName: String, value: Any, isSticky: Boolean = false) {
        val event = getEventFlow(eventName, isSticky)
        event.emit(value)
    }

    suspend fun <T : Any> observe(
        eventName: String,
        isSticky: Boolean = false,
        onChange: suspend (T) -> Unit
    ) {
        getEventFlow(eventName, isSticky).collect {
            onChange(it as T)
        }
    }

    suspend fun <T : Any> observe(
        lifecycleOwner: LifecycleOwner,
        eventName: String,
        isSticky: Boolean = false,
        onChange: suspend (T) -> Unit
    ) {
        observe(eventName, isSticky, onChange)
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    flowEvents.remove(eventName)
                }
            }
        })
    }

    private fun getEventFlow(eventName: String, isSticky: Boolean = false): MutableSharedFlow<Any> {
        var event = flowEvents[eventName]
        if (event == null) {
            val replay = if (isSticky) 1 else 0 // 是否接收以前的数据
            event = MutableSharedFlow(replay, 1)
            flowEvents[eventName] = event
        }
        return event
    }
}