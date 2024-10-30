package com.kelp.android.fms.runtime2.deeplink


 interface DeeplinkSupport {
    fun isSupport(uri: String): Boolean
    fun register(uri: String)
    fun unregister(uri: String)
}