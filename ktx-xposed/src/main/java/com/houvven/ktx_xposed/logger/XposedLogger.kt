package com.houvven.ktx_xposed.logger

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.core.content.contentValuesOf
import com.houvven.ktx_xposed.hook.lppram

object XposedLogger {

    object Level {
        const val DEBUG = 'D'
        const val INFO = 'I'
        const val ERROR = 'E'
    }

    private val logList = mutableListOf<Pair<Char, String>>()
    private val uri = Uri.parse("content://com.houvven.xposed.runtime.log/module_log")

    fun d(msg: String) { logList.add('D' to msg) }
    fun i(msg: String) { logList.add('I' to msg) }
    fun e(msg: String) { logList.add('E' to msg) }
    fun e(throwable: Throwable) { logList.add('E' to throwable.toString()) }

    fun doHookModuleLog() {
        val onPauseMethod = Activity::class.java.getDeclaredMethod("onPause")
        lppram.hook(onPauseMethod).intercept { chain ->
            val activity = chain.thisObject as? Activity
            if (activity != null && logList.isNotEmpty()) {
                runCatching {
                    logList.forEach { log ->
                        contentValuesOf(
                            "type" to log.first.toString(),
                            "source" to "unknown",
                            "message" to log.second
                        ).let { activity.contentResolver.insert(uri, it) }
                    }
                    logList.clear()
                }
            }
            chain.proceed()
        }
    }
}
