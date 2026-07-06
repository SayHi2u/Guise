package com.houvven.guise.xposed

import com.houvven.guise.ContextAmbient
import com.houvven.guise.xposed.config.ModuleConfig
import com.houvven.ktx_xposed.hook.lppram

object PackageConfig {

    const val PREF_FILE_NAME = "XposedDeployInfo"

    val safePrefs
        get() = ContextAmbient.current.getSharedPreferences(PREF_FILE_NAME, android.content.Context.MODE_PRIVATE)

    lateinit var current: ModuleConfig

    fun doRefresh(packageName: String) {
        val prefs = lppram.getRemotePreferences("guise_config")
        val json = prefs.getString(packageName, null)
        current = if (json != null) ModuleConfig.fromJson(json) else ModuleConfig()
        current.packageName = packageName
    }
}
