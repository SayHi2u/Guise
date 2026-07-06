package com.houvven.guise

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.houvven.guise.lsposed.LsposedHelper
import com.houvven.ktx_xposed.HookStatus
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel
import io.github.libxposed.service.XposedService
import io.github.libxposed.service.XposedServiceHelper
import java.io.File

class ContextAmbient : Application(), XposedServiceHelper.OnServiceListener {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var current: Context

        @Volatile
        var service: XposedService? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        current = applicationContext
        MMKV.initialize(this, MMKVLogLevel.LevelNone)
        LsposedHelper.init(File(filesDir, "/bin/sqlite3").absolutePath)
        XposedServiceHelper.registerListener(this)
    }

    override fun onServiceBind(service: XposedService) {
        Log.i("Guise", "Xposed service connected: ${service.frameworkName} ${service.frameworkVersion}")
        Companion.service = service
        HookStatus.setActivated(true)
    }

    override fun onServiceDied(service: XposedService) {
        Log.w("Guise", "Xposed service died")
        Companion.service = null
        HookStatus.setActivated(false)
    }
}
