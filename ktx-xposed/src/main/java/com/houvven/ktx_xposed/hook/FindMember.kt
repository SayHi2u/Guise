package com.houvven.ktx_xposed.hook

import java.lang.reflect.Method

fun Class<*>.findMethodExactIfExists(name: String, vararg parameterTypes: Class<*>): Method? = try {
    getDeclaredMethod(name, *parameterTypes)
} catch (_: NoSuchMethodException) {
    null
}

fun Method.setMethodResult(value: Any?) {
    this.declaringClass.setMethodResult(this.name, value)
}
