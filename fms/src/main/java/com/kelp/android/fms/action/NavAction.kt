package com.kelp.android.fms.action

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentTransaction
import com.kelp.android.fms.core.NavControllerImpl
import com.kelp.android.fms.fragment_nodes.FragmentNode
import com.kelp.android.fms.nav_options.NavOptions

abstract class NavAction(
    val controller: NavControllerImpl,
    val navOptions: NavOptions,
    val node: FragmentNode
) : Runnable {

    val queue = controller.queue

    val fm = controller.context.fragmentManager

    val containerView: FragmentContainerView
        get() {
            if (navOptions.containerView != null) {
                return navOptions.containerView!!
            } else {
                return controller.containerView
            }
        }

    fun findFragment(tag: String): Fragment? {
        return fm.findFragmentByTag(tag)
    }


    /**
     * todo 隐藏时，还需要根据容器区分，因为可以在一个屏幕上并排摆放多个容器，同时显示多个fragment，
     * 且使用的是同一个fragmentManger，如果直接隐藏全部，这是不合理的，需要按照容器来隐藏。
     *
     * @param exclude
     */
    fun FragmentTransaction.hideAll(exclude: List<Fragment> = emptyList()) {
        controller.getVisibleFragment().forEach {
            if (!exclude.contains(it)) {
                hide(it)
            }
        }
    }

    fun FragmentTransaction.replaceFragment(fragment: Fragment, tag: String) {
        replace(containerView.id, fragment, tag)
    }
}

