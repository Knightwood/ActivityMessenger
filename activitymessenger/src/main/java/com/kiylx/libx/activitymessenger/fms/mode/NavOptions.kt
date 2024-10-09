package com.kiylx.libx.activitymessenger.fms.mode

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.kiylx.libx.activitymessenger.core.putExtras
import com.kiylx.libx.activitymessenger.fms.controller.AFragmentController

/**
 * 描述启动这个fragment时的配置参数。
 *
 * @property host 追加到的栈节点，默认是自身节点
 * @property bundle 要传递的数据
 * @property node 关联的FragmentNode
 */
class NavOptions(
    var tag:String,
    var host: AFragmentController,
) {
    val node: FragmentNode
        get() = host.nodeQueue.findInStack(tag)!!

    var launchMode: LaunchMode = LaunchMode.Standard

    var bundle: Bundle? = null

    /**
     * 标记此fragment是否是顶部正显示的。 当显示在顶部时，会被标记为true。
     */
    internal var isTopFragment: Boolean = false

    /**
     * 实际就是host变量，他两是一个意思。 如果设置的值为null，则不修改原有值
     */
    var attachTo: AFragmentController? = null
        set(value) {
            field = value
            if (value != null) {
                host = value
            }
        }
        get() = host

    /**
     * 可以修改添加到哪个容器视图。 默认是controller中的的containerView。
     */
    var containerView: FragmentContainerView = host.container

    /**
     * 如果为true，则此fragment和其平级的fragment为根层级，当触发返回键时，返回无效。 但可以手动对用[]将根层级弹出栈。
     */
    var root: Boolean = false

    /**
     * 当启动其他fragment时，是否将显示在目标容器最顶层fragment终止并移除
     */
    var dropSelf: Boolean = false

    /**
     * 当启动其他fragment时，是否清空目标容器最顶层fragment之前的所有fragment
     *
     * 被标记为root的不受影响
     */
    var clearHistory: Boolean = false

    /**
     * 是否加入回退栈
     */
    var addToBackStack: Boolean = true

    /**
     * 是否允许丢失状态
     */
    var allowLostState: Boolean = false

    /**
     * 对于此FragmentNode中的设置。将覆盖controller中的设置。 读取时使用[isUseReplace]变量
     * true：显示隐藏fragment时，使用replace，而不是hide/show。
     */
    var useReplace: Boolean? = null

    val isUseReplace: Boolean
        get() {
            return if (useReplace == null)
                host.useReplace
            else
                useReplace ?: false
        }

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

    companion object {
    }
}