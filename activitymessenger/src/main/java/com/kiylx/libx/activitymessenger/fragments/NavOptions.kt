package com.kiylx.libx.activitymessenger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.kiylx.libx.activitymessenger.core.putExtras

/**
 * 描述一个fragment启动时，需要的参数
 */
data class NavOptions(
    var bundle: Bundle? = null,
    var addToBackStack: Boolean = true,
    var allowLostState: Boolean = false,
    var tag: String? = null,
    var containerId: Int?=null,
    var replace: Boolean = false,
) {

    /**
     * 设置参数，可以是任意类型，只要Bundle支持的即可。
     *
     * 如果你重写了[newInstanceFactory]，且调用此方法，
     * 那么此方法设置的参数将覆盖你在[newInstanceFactory]调用[Fragment.setArguments]设置的参数。
     *
     * 调用示例：
     *
     * ```
     * params("param1" to 1, "param2" to "2")
     *
     * ```
     *
     * @param params
     */
    fun params(
        vararg params: Pair<String, Any?>,
    ) {
        bundle = Bundle().putExtras(*params)
    }
}