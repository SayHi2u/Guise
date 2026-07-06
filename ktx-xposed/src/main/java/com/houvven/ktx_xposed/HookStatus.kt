package com.houvven.ktx_xposed

class HookStatus {
    companion object {
        @Volatile
        private var activated = false

        fun setActivated(value: Boolean) {
            activated = value
        }

        fun isActivated(): Boolean = activated
    }
}
