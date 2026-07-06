package com.houvven.ktx_xposed.hook

inline fun <reified T> setStaticField(className: String, fieldName: String, value: T) {
    findClass(className).setStaticField(fieldName, value)
}

inline fun <reified T> Class<*>.setStaticField(fieldName: String, value: T) {
    val field = getDeclaredField(fieldName)
    field.isAccessible = true
    field.set(null, value)
}
