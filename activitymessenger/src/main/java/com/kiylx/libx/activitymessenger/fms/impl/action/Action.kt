package com.kiylx.libx.activitymessenger.fms.impl.action

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.kiylx.libx.activitymessenger.fms.impl.SealFragmentController
import com.kiylx.libx.activitymessenger.fms.mode.NavOptions


/**
 * 执行操作
 */
object Action {

    fun add(
        controller: SealFragmentController,
        navOptions: NavOptions,
    ): Runnable {
        return AddAction(controller, navOptions)
    }

}

/**
 * 基础action
 *
 * @property controller 与节点关联的controller
 * @property navOptions 要执行操作的节点
 *
 * 机制描述： fragment manager具有事务管理能力，所以在一个事务中进行的操作会统一在commit时提交。
 * 当然，在按下返回键时，事务会回滚。 因此，如果在一个事务中，我隐藏了所有的fragment，并添加一个新的fragment。
 * 那么，在按下返回键时，这个事务将回滚，因此，在返回键被按下时，会显示那些被隐藏的fragment。
 *
 * 其实，fragment manager中的回退栈，是以回滚事务为基础的，而不是单个的fragment。
 *
 * launchFragment是添加一个fragment到容器，那么，底部的fragment如何处理？
 * 1，隐藏，但这种方式，如果底部的fragment不想隐藏，需要启动新的fragment时依旧显示呢？
 * 2，不处理，直接覆盖在上面，但这种方式需要fragment设置背景色，否则会漏出底部的fragment。
 * 3，平级的fragment显示和隐藏时，如何处理？
 */
abstract class BaseAction(
    val controller: SealFragmentController,
    val navOptions: NavOptions,
) : Runnable {

    fun findFragment(tag: String): Fragment? {
        return controller.fm.findFragmentByTag(tag)
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
        replace(controller.container.id, fragment, tag)
    }

}