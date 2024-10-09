package com.kiylx.libx.activitymessenger.fms.impl

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import com.kiylx.libx.activitymessenger.fms.impl.action.Action
import com.kiylx.libx.activitymessenger.fms.controller.AFragmentController
import com.kiylx.libx.activitymessenger.fms.controller.buildNavOptions
import com.kiylx.libx.activitymessenger.fms.mode.FragmentNode
import com.kiylx.libx.activitymessenger.fms.mode.FragmentNodeQueue
import com.kiylx.libx.activitymessenger.fms.mode.NavOptions

/**
 * 管理fragment
 */
class SealFragmentController(
    fm: FragmentManager,
    container: FragmentContainerView,
    tag: String,//标识唯一性
    //是否在activity中创建的controller
    var inActivity: Boolean = false,
) : AFragmentController(fm, container, tag) {
    /**
     * 主导航fragment
     */
    var primaryNavHost:Fragment?=null

    fun buildNavGraph(builder: FragmentNodeQueue.(SealFragmentController) -> Unit): SealFragmentController {
        nodeQueue.builder(this)
        return this
    }

    override fun launchFragment(navOptions: NavOptions) {
        val attachNode = navOptions.host
        if (attachNode != this) {
            //如果此fragment不是添加到此节点，则用对应的节点添加fragment
            attachNode.launchFragment(navOptions)
        } else {
            val node =nodeQueue.findInStack(navOptions.tag)
            if (node==null){
                nodeQueue.putIn(
                    FragmentNode(navOptions.clazz)
                )
            }
            Action.add(this, navOptions).run()
        }
    }

    fun back() {
        fm.popBackStack()
    }

    fun backTo(target: FragmentNode) {

    }

    fun switch(target: FragmentNode) {

    }

}

//<editor-fold desc="扩展方法">

/**
 * 启动新的fragment
 */
inline fun <reified T : Fragment> AFragmentController.launchFragment(
    builder: NavOptions.() -> Unit = {},
) = launchFragment(buildNavOptions<T>(builder))

/**
 * 将此controller追加到指定的父节点controller
 *
 * @param targetParentTag 父节点tag
 * @return
 */
fun SealFragmentController.setParentNode(targetParentTag: String): SealFragmentController {
    findController(targetParentTag)?.let {
        return setParentNode(it)
    } ?: throw IllegalStateException("未找到目标节点")
}

/**
 * 将此controller追加到指定的父节点controller
 *
 * 适用于在activity或fragment中生成controller时，tag未指定的情况（未指定时，tag是activity或fragment的类名）
 *
 * 泛型 T : 父节点controller所在的activity或fragment
 *
 * @receiver SealFragmentController 当前controller
 */
inline fun <reified T> SealFragmentController.setParentNode(): SealFragmentController {
    findController(T::class.java.name)?.let {
        return setParentNode(it)
    } ?: throw IllegalStateException("未找到目标节点")
}

//</editor-fold>
