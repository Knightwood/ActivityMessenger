package com.kelp.android.fms.runtime.core

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * 管理所有controller
 */
object NavControllerManager {
    private val controllers = mutableMapOf<String, INavController>()

    fun get(tag: String): INavController? {
        return controllers[tag]
    }

    fun put(tag: String, controller: INavController) {
        controllers[tag] = controller
    }

    fun remove(tag: String) {
        controllers.remove(tag)
    }
}

/**
 * 查找控制器
 *
 * @param T
 * @return
 */
inline fun <reified T : FragmentActivity> T.findController(): INavController? {
    return NavControllerManager.get(this::class.java.name)
}
/**
 * 查找控制器
 *
 * @param T
 * @return
 */
inline fun <reified T : Fragment> T.findController(): INavController? {
    return NavControllerManager.get(this::class.java.name)
}