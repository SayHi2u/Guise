package com.houvven.ktx_xposed.hook

import io.github.libxposed.api.XposedInterface

lateinit var lppram: XposedInterface
    private set

lateinit var currentClassLoader: ClassLoader
    private set

fun setLpparam(module: XposedInterface) {
    lppram = module
}

fun setCurrentClassLoader(classLoader: ClassLoader) {
    currentClassLoader = classLoader
}

fun findClass(className: String): Class<*> =
    Class.forName(className, false, currentClassLoader)

fun findClassIfExists(className: String): Class<*>? = try {
    Class.forName(className, false, currentClassLoader)
} catch (_: ClassNotFoundException) {
    null
}
