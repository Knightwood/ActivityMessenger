package com.kelp.android.fms.runtime2.destination

import com.kelp.android.fms.runtime2.deeplink.DeeplinkSupport

/**
 * 表示一个导航目的地
 *
 * @property navigatorName 用于查找关联的navigator
 * @property navOptions 导航参数
 */
open class NavDestination(
    val navigatorName: String,
    val navOptions: NavOptions
) {
    val deeplinkSupport = object : DeeplinkSupport {
        override fun isSupport(uri: String): Boolean {
            return false
        }

        override fun register(uri: String) {
            TODO("Not yet implemented")
        }

        override fun unregister(uri: String) {
            TODO("Not yet implemented")
        }
    }

}

