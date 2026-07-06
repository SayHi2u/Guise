package com.houvven.guise.xposed.hook

import com.houvven.guise.xposed.LoadPackageHandler
import com.houvven.ktx_xposed.hook.setMethodResult
import java.util.Locale

class LocalHook : LoadPackageHandler {

    override fun onHook() {
        var language = config.language
        var country: String

        if (language.isBlank()) return

        language.split("_").let {
            language = it[0]
            country = if (it.size < 2) "" else it[1]
        }

        runCatching {
            if (country.isBlank()) Locale(language)
            else Locale(language, country)
        }.onSuccess { locale ->
            country = locale.country
            Locale::class.java.run {
                setMethodResult("getDefault", locale)
                setMethodResult("getLanguage", locale.language)
                setMethodResult("getCountry", locale.country)
                setMethodResult("getVariant", locale.variant)
                setMethodResult("getScript", locale.script)
                setMethodResult("getDisplayLanguage", locale.displayLanguage)
                setMethodResult("getDisplayCountry", locale.displayCountry)
                setMethodResult("getDisplayName", locale.displayName)
                setMethodResult("getDisplayVariant", locale.displayVariant)
                setMethodResult("getDisplayScript", locale.displayScript)
                setMethodResult("toLanguageTag", locale.toLanguageTag())
                setMethodResult("toString", locale.toString())
            }
        }
    }
}
