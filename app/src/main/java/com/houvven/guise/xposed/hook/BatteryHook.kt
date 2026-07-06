package com.houvven.guise.xposed.hook

import android.content.Intent
import android.os.BatteryManager
import com.houvven.guise.xposed.LoadPackageHandler
import com.houvven.ktx_xposed.hook.afterHookedMethod

class BatteryHook : LoadPackageHandler {

    @Throws(Throwable::class)
    override fun onHook() {
        val level = config.batteryLevel
        if (level == -1) return

        BatteryManager::class.java.afterHookedMethod(
            methodName = "getIntProperty", Int::class.java
        ) { chain ->
            if (chain.args[0] == BatteryManager.BATTERY_PROPERTY_CAPACITY) {
                level
            } else {
                chain.proceed()
            }
        }

        Intent::class.java.afterHookedMethod(
            methodName = "getIntExtra", String::class.java, Int::class.java
        ) { chain ->
            if (chain.args[0] == BatteryManager.EXTRA_LEVEL) {
                level
            } else {
                chain.proceed()
            }
        }
    }
}
