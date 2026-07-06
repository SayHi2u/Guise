package com.houvven.guise.xposed.hook

import android.content.ContentResolver
import android.provider.Settings
import android.provider.Settings.Secure
import android.telephony.TelephonyManager
import com.houvven.guise.xposed.LoadPackageHandler
import com.houvven.guise.xposed.PackageConfig
import com.houvven.guise.xposed.config.ModuleConfig
import com.houvven.ktx_xposed.hook.afterHookedMethod
import com.houvven.ktx_xposed.hook.beforeHookedMethod
import com.houvven.ktx_xposed.hook.findClassIfExists
import com.houvven.ktx_xposed.hook.lppram
import com.houvven.ktx_xposed.hook.setAllMethodResult
import com.houvven.ktx_xposed.hook.setMethodResult
import com.houvven.ktx_xposed.hook.setSomeSameNameMethodResult
import com.houvven.ktx_xposed.logger.XposedLogger

class UniquelyIdHook : LoadPackageHandler {

    override fun onHook() {
        if (config.androidId.isNotBlank()) this.hookAndroidId()
        if (config.imei.isNotBlank()) this.hookImei()
        if (config.phoneNum.isNotBlank()) this.hookPhoneNum()
    }

    private fun hookAndroidId() {
        Secure::class.java.beforeHookedMethod(
            methodName = "getStringForUser",
            ContentResolver::class.java, String::class.java, Int::class.java
        ) { chain ->
            if (chain.args[1] == Secure.ANDROID_ID) {
                if (config.androidId.isBlank()) {
                    XposedLogger.i("androidId is blank")
                    val prefs = lppram.getRemotePreferences("guise_config")
                    val json = prefs.getString(lppram.moduleApplicationInfo.packageName, null)
                    if (json != null) {
                        val moduleConfig = ModuleConfig.fromJson(json)
                        moduleConfig.androidId
                    } else {
                        chain.proceed()
                    }
                } else {
                    config.androidId
                }
            } else {
                chain.proceed()
            }
        }

        Settings.System::class.java.afterHookedMethod(
            methodName = "getStringForUser",
            ContentResolver::class.java, String::class.java, Int::class.java
        ) { chain ->
            if (chain.args[1] == Settings.System.ANDROID_ID) {
                if (config.androidId.isBlank()) {
                    val prefs = lppram.getRemotePreferences("guise_config")
                    val json = prefs.getString(lppram.moduleApplicationInfo.packageName, null)
                    if (json != null) {
                        val moduleConfig = ModuleConfig.fromJson(json)
                        moduleConfig.androidId
                    } else {
                        chain.proceed()
                    }
                } else {
                    config.androidId
                }
            } else {
                chain.proceed()
            }
        }
    }

    private fun hookImei() {
        TelephonyManager::class.java.setMethodResult(
            "getImei", config.imei, parameterTypes = arrayOf(Int::class.java)
        )
    }

    private fun hookPhoneNum() {
        TelephonyManager::class.java.setAllMethodResult("getLine1Number", config.phoneNum)
    }
}
