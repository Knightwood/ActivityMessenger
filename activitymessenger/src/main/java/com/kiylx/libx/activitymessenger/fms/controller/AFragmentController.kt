package com.kiylx.libx.activitymessenger.fms.controller

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.kiylx.libx.activitymessenger.fms.mode.FragmentNode
import com.kiylx.libx.activitymessenger.fms.mode.FragmentNodeQueue
import com.kiylx.libx.activitymessenger.fms.mode.LaunchMode
import com.kiylx.libx.activitymessenger.fms.mode.NavOptions

abstract class AFragmentController(
    var fm: FragmentManager,
    val container: FragmentContainerView,
    val tag: String,//标识唯一性
) : IFragmentController, LifecycleEventObserver {
    private val TAG = "FragmentController"

    /**
     * 对于此controller的全局设置。FragmentNode中的设置将覆盖此controller的设置。
     * true：显示隐藏fragment时，使用replace，而不是hide/show。
     */
    var useReplace: Boolean = false

    /**
     * controller可以组成树结构。 父节点为null，则表示为根节点
     */
    var parent: AFragmentController? = null
        private set

    /**
     * 首先，在activity 1中，生成一个controller 1。
     * 然后使用controller.launchFragment方法，启动新的fragment A。 在A中，生成新的controller
     * A，追加controller 1为parent。 在A中使用launchFragment方法，启动新的fragment
     * B，将fragment添加到controller 1或者controller A。 如果是1：此时的controller
     * 1有两个fragment，为A和B。并有一个children，为controller A。 如果是A：此时的controller
     * 1有1个fragment，为A。并有一个children，为controller A。 controller A 有一个fragment，为B。
     *
     * controller管理的其他controller
     */
    var childs: MutableMap<String, AFragmentController> = mutableMapOf()
        private set

    /**
     * controller存储所有fragment节点
     */
    val nodeQueue = FragmentNodeQueue()

    /**
     * 持有fragment实例 会在fragment销毁时从这里移除。 Map<fragment_tag,fragment>
     */
    @Deprecated(message = "已弃用")
    val fragments: MutableMap<String, Fragment> = mutableMapOf()

    init {
        init()
    }


    private fun init() {
        FragmentControllerManager + this
        fm.registerFragmentLifecycleCallbacks(FragmentLifecycleObserver(this), false)
    }

    fun getVisibleFragment(): List<Fragment> {
        val result = mutableListOf<Fragment>()
        val fragments = fm.fragments
        for (fragment in fragments) {
            if (fragment != null && fragment.isVisible)
                result.add(fragment)
        }
        return result
    }

    internal fun getFragmentInstance(
        fragmentManager: FragmentManager,
        actionType: LaunchMode,
        tag: String,
        newInstanceFactory: () -> Fragment,
    ): Fragment? {
        return when (actionType) {
            LaunchMode.SingleInTask -> {
                val wrapper = nodeQueue.findInStack(tag)
                if (wrapper == null) {
                    return newInstanceFactory.invoke()
                } else {
                    return fragmentManager.findFragmentByTag(tag)
                }
            }

            LaunchMode.SingleTop -> {
                val wrapper = nodeQueue.findTop(tag)
                if (wrapper == null) {
                    return newInstanceFactory.invoke()
                } else {
                    return fragmentManager.findFragmentByTag(tag)
                }
            }

            LaunchMode.Standard -> newInstanceFactory.invoke()
        }
    }

    /**
     * 获取任意的controller
     *
     * @param tag 唯一标识
     * @return FragmentController，如果tag对应的controller不存在，则返回null
     */
    fun findController(tag: String): AFragmentController? {
        return FragmentControllerManager.get(tag)
    }

    /**
     * 将此controller追加到指定的父节点controller
     *
     * @param targetParentController 父节点
     * @return
     */
    fun <T : AFragmentController> setParentNode(targetParentController: AFragmentController): T {
        if (parent == null) {
            parent = targetParentController//设置父节点
            targetParentController.childs[tag] = this //将自己挂载到父节点的childs中
            return this as T
        }
        if (this == targetParentController) {
            throw IllegalArgumentException("不能将自身设置为父节点")
        }
        //父节点存在，则不用再次添加
        Log.d(TAG, "setParentNode: 父节点已存在，无需再次添加")
        return this as T
    }

    /**
     * 清空[nodeQueue]和[childs]
     */
    fun release() {
        nodeQueue.release()
        childs.forEach {
            it.value.release()
        }
    }

    /**
     * 当关联的lifecycleOwner销毁时，释放所有资源
     */
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                release()
                FragmentControllerManager - this
            }

            else -> {}
        }
    }
}

/**
 * 构建一个FragmentNode
 */
inline fun <reified T : Fragment> AFragmentController.buildNavOptions(
    builder: NavOptions.() -> Unit = {},
): NavOptions {
    val config = NavOptions(T::class.java.name, this)
    config.builder()
    return config
}
