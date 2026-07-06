package com.houvven.ktx_xposed.hook

fun Any.callMethod(methodName: String, vararg args: Any?): Any? {
    val method = javaClass.getDeclaredMethod(methodName)
    method.isAccessible = true
    return method.invoke(this, *args)
}

fun Any.callMethodIfExists(methodName: String, vararg args: Any?): Any? = try {
    callMethod(methodName, *args)
} catch (_: Throwable) {
    null
}

fun Class<*>.callStaticMethodIfExists(methodName: String, vararg args: Any?): Any? = try {
    val method = getDeclaredMethod(methodName)
    method.isAccessible = true
    method.invoke(null, *args)
} catch (_: Throwable) {
    null
}
