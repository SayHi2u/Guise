package com.houvven.guise.xposed

import android.util.Log
import com.houvven.guise.BuildConfig
import com.houvven.guise.xposed.hook.BatteryHook
import com.houvven.guise.xposed.hook.BuildConfigHook
import com.houvven.guise.xposed.hook.LocalHook
import com.houvven.guise.xposed.hook.OsBuildHook
import com.houvven.guise.xposed.hook.ScreenshotsHook
import com.houvven.guise.xposed.hook.UniquelyIdHook
import com.houvven.guise.xposed.hook.location.CellLocationHook
import com.houvven.guise.xposed.hook.location.LocationHook
import com.houvven.guise.xposed.hook.netowork.NetworkHook
import com.houvven.guise.xposed.other.BlankPass
import com.houvven.guise.xposed.other.HookSuccessHint
import com.houvven.ktx_xposed.hook.setCurrentClassLoader
import com.houvven.ktx_xposed.hook.setLpparam
import com.houvven.ktx_xposed.logger.XposedLogger
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.ModuleLoadedParam
import io.github.libxposed.api.XposedModuleInterface.PackageReadyParam

class HookInit : XposedModule() {

    override fun onModuleLoaded(param: ModuleLoadedParam) {
        log(Log.INFO, "Guise", "Module loaded in process: ${param.processName}")
    }

    override fun onPackageReady(param: PackageReadyParam) {
        setLpparam(this)
        setCurrentClassLoader(param.classLoader)

        if (param.packageName == BuildConfig.APPLICATION_ID) return

        XposedLogger.currentTargetPackage = param.packageName
        XposedLogger.doHookModuleLog()
        XposedLogger.i("start loadPackage: ${param.packageName}")
        PackageConfig.doRefresh(param.packageName)

        if (!PackageConfig.current.isEnable) {
            XposedLogger.i("loadPackage: ${param.packageName} is not enable, skip.")
            return
        }

        listOf(
            HookSuccessHint(),
            BatteryHook(),
            LocalHook(),
            LocationHook(),
            CellLocationHook(),
            NetworkHook(),
            OsBuildHook(),
            ScreenshotsHook(),
            UniquelyIdHook(),
            BlankPass(),
            BuildConfigHook()
        ).forEach { runCatching { it.onHook() } }
    }
}
