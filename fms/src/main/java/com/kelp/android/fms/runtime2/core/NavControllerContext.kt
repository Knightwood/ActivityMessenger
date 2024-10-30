package com.kelp.android.fms.runtime2.core

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

class NavControllerContext(
    private val activity: FragmentActivity? = null,
    private val fragment: Fragment? = null,
) {
    /**
     * 当前生成控制器的是否为activity
     */
    val isActivity: Boolean get() = activity != null

    /**
     * 获取当前生成控制器的activity或fragment的fragmentManager
     */
    val fragmentManager: FragmentManager
        get() {
            return activity?.supportFragmentManager ?: fragment?.childFragmentManager!!
        }

    /**
     * 获取当前生成控制器的activity或fragment的 class name
     */
    val tag: String
        get() {
            if (activity != null) {
                return activity::class.java.name
            } else {
                return fragment!!::class.java.name
            }
        }

    val ctx: FragmentActivity
        get() {
            if (activity != null) {
                return activity
            } else {
                return fragment!!.requireActivity()
            }
        }

    val lifecycleOwner: LifecycleOwner
        get() {
            if (activity != null) {
                return activity
            } else {
                val lifecycle_ = runCatching {
                    //不是所有fragment都有视图，没有视图的，会抛出异常，所以要判断一下
                    fragment!!.viewLifecycleOwner
                }.getOrDefault(fragment!!)
                return lifecycle_
            }
        }

    val lifeCycle: Lifecycle
        get() {
            if (activity != null) {
                return activity.lifecycle
            } else {
                val lifecycle_ = runCatching {
                    //不是所有fragment都有视图，没有视图的，会抛出异常，所以要判断一下
                    fragment!!.viewLifecycleOwner.lifecycle
                }.getOrDefault(fragment!!.lifecycle)
                return lifecycle_
            }
        }
}