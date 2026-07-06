package com.houvven.guise.xposed.hook

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import com.houvven.guise.xposed.LoadPackageHandler
import com.houvven.guise.xposed.config.HooksValue
import com.houvven.ktx_xposed.hook.afterHookedMethod
import com.houvven.ktx_xposed.hook.afterHookSomeSameNameMethod
import com.houvven.ktx_xposed.hook.callMethod

class ScreenshotsHook : LoadPackageHandler {

    override fun onHook() {
        if (config.screenshotsFlag == HooksValue.SCREENSHOTS_UNHOOK) return
        if (config.screenshotsFlag == HooksValue.SCREENSHOTS_DISABLE) disableScreenshots()
        else if (config.screenshotsFlag == HooksValue.SCREENSHOTS_ENABLE) enableScreenshots()
    }

    private fun disableScreenshots() {
        Activity::class.java.afterHookedMethod(
            methodName = "onCreate", Bundle::class.java
        ) { chain ->
            val activity = chain.thisObject as Activity
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
            chain.proceed()
        }
    }

    private fun enableScreenshots() {
        android.view.Window::class.java.afterHookSomeSameNameMethod(
            "setFlags",
            "setPrivateFlags",
            "addFlags",
            "addPrivateFlags",
            "addSystemFlags"
        ) { chain ->
            if (chain.args[0] == WindowManager.LayoutParams.FLAG_SECURE) {
                (chain.thisObject as android.view.Window).clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
            chain.proceed()
        }
    }
}
