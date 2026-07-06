package com.houvven.ktx_xposed.utils

import io.github.libxposed.api.XposedInterface

fun XposedInterface.Chain.getTypeArgIndexOfFirst(type: Class<*>): Int {
    return args.indexOfFirst { it.javaClass == type }
}

fun XposedInterface.Chain.setNullResult(): Nothing? = null
