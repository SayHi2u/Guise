package com.houvven.ktx_xposed.utils

import android.util.Log

inline fun <R> runXposedCatching(block: () -> R): R? {
    return try {
        block()
    } catch (e: Throwable) {
        Log.e("XposedCatch", e.toString(), e)
        null
    }
}
