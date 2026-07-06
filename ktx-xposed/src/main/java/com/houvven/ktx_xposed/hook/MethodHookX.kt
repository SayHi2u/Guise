package com.houvven.ktx_xposed.hook

import io.github.libxposed.api.XposedInterface

inline fun Class<*>.afterHookedMethod(
    methodName: String,
    vararg parameterTypes: Class<*>,
    crossinline callback: (XposedInterface.Chain) -> Any?,
) {
    val method = getMethod(methodName, *parameterTypes)
    lppram.hook(method).intercept { chain -> callback(chain) }
}

inline fun afterHookedMethod(
    className: String,
    methodName: String,
    vararg parameterTypes: Class<*>,
    crossinline callback: (XposedInterface.Chain) -> Any?,
) {
    findClass(className).afterHookedMethod(methodName, *parameterTypes, callback = callback)
}

inline fun Class<*>.beforeHookedMethod(
    methodName: String,
    vararg parameterTypes: Class<*>,
    crossinline callback: (XposedInterface.Chain) -> Unit,
) {
    val method = getMethod(methodName, *parameterTypes)
    lppram.hook(method).intercept { chain ->
        callback(chain)
        chain.proceed()
    }
}

inline fun beforeHookedMethod(
    className: String,
    methodName: String,
    vararg parameterTypes: Class<*>,
    crossinline callback: (XposedInterface.Chain) -> Unit,
) {
    findClass(className).beforeHookedMethod(methodName, *parameterTypes, callback = callback)
}

fun Class<*>.setMethodResult(
    methodName: String,
    value: Any?,
    vararg parameterTypes: Class<*>,
) {
    val method = getMethod(methodName, *parameterTypes)
    lppram.hook(method).intercept { _ -> value }
}

fun setMethodResult(
    className: String,
    methodName: String,
    value: Any?,
    vararg parameterTypes: Class<*>,
) {
    findClass(className).setMethodResult(methodName, value, *parameterTypes)
}

fun Class<*>.setAllMethodResult(methodName: String, value: Any?) {
    declaredMethods
        .filter { it.name == methodName }
        .forEach { method ->
            lppram.hook(method).intercept { _ -> value }
        }
}

fun setAllMethodResult(className: String, methodName: String, value: Any?) {
    findClass(className).setAllMethodResult(methodName, value)
}

inline fun Class<*>.afterHookAllMethods(
    methodName: String,
    crossinline callback: (XposedInterface.Chain) -> Any?,
) {
    declaredMethods
        .filter { it.name == methodName }
        .forEach { method ->
            lppram.hook(method).intercept { chain -> callback(chain) }
        }
}

inline fun Class<*>.beforeHookAllMethods(
    methodName: String,
    crossinline callback: (XposedInterface.Chain) -> Unit,
) {
    declaredMethods
        .filter { it.name == methodName }
        .forEach { method ->
            lppram.hook(method).intercept { chain ->
                callback(chain)
                chain.proceed()
            }
        }
}

fun Class<*>.setSomeSameNameMethodResult(
    vararg methodName: String,
    value: Any?,
) {
    methodName.forEach { name ->
        setAllMethodResult(name, value)
    }
}

fun setSomeSameNameMethodResultForAnyClass(
    classAndMethodName: List<Pair<Class<*>, String>>,
    value: Any?,
) {
    classAndMethodName.forEach { (clazz, methodName) ->
        clazz.setAllMethodResult(methodName, value)
    }
}

inline fun Class<*>.afterHookSomeSameNameMethod(
    vararg methodName: String,
    crossinline callback: (XposedInterface.Chain) -> Any?,
) {
    methodName.forEach { name ->
        afterHookAllMethods(name, callback = callback)
    }
}

inline fun Class<*>.beforeHookSomeSameNameMethod(
    vararg methodName: String,
    crossinline callback: (XposedInterface.Chain) -> Unit,
) {
    methodName.forEach { name ->
        beforeHookAllMethods(name, callback = callback)
    }
}

inline fun Class<*>.beforeHookConstructor(
    vararg parameterTypes: Class<*>,
    crossinline callback: (XposedInterface.Chain) -> Unit,
) {
    val constructor = getConstructor(*parameterTypes)
    lppram.hook(constructor).intercept { chain ->
        callback(chain)
        chain.proceed()
    }
}
