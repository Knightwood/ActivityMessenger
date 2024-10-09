package com.kiylx.libx.activitymessenger.fms

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import com.kiylx.libx.activitymessenger.fms.controller.FragmentControllerManager
import com.kiylx.libx.activitymessenger.fms.impl.SealFragmentController


/**
 * 生成一个新的控制器，或者获取一个已存在的控制器
 *
 * @param containerView fragment容器
 * @param tag 标签，用于标识一个控制器，默认为当前activity的className
 * @return
 */
fun FragmentActivity.fragmentController(
    containerView: FragmentContainerView,
): SealFragmentController {
    val tag: String = this::class.java.name
    //如果存在，则直接返回，忽略attachTo
    return (FragmentControllerManager.get(tag)?.also { it.fm = supportFragmentManager }
        ?: SealFragmentController(
            supportFragmentManager, containerView, tag,true,
        ).also { this.lifecycle.addObserver(it) }) as SealFragmentController
}

fun Fragment.fragmentController(
    containerView: FragmentContainerView,
): SealFragmentController {
    val tag: String = this::class.java.name
    //如果存在，则直接返回，忽略attachTo
    return (FragmentControllerManager.get(tag)?.also { it.fm = childFragmentManager }
        ?: SealFragmentController(
            childFragmentManager, containerView, tag,false,
        ).also {
            val owner = runCatching {
                //不是所有fragment都有视图，没有视图的，会抛出异常，所以要判断一下
                this.viewLifecycleOwner.lifecycle
            }.getOrDefault(this.lifecycle)
            owner.addObserver(it)

        }) as SealFragmentController
}