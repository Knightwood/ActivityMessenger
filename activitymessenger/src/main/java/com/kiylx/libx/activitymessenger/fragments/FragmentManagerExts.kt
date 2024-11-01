package com.kiylx.libx.activitymessenger.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

inline fun <reified TARGET : Fragment> FragmentActivity.launchFragment(
    containerId: Int,
    crossinline navOptions: (BaseNavOptions.() -> Unit) = {}
) = supportFragmentManager.launchFragment<TARGET>(containerId, navOptions)


inline fun <reified TARGET : Fragment> FragmentManager.launchFragment(
    containerId: Int,
    crossinline navOptions: (BaseNavOptions.() -> Unit) = {}
) {
    val options = BaseNavOptions()
    options.navOptions()
    if (options.tag == null) {
        options.tag = TARGET::class.java.name
    }
    launchFragmentInternal(containerId, this, TARGET::class.java, options)
}

fun launchFragmentInternal(
    container: Int,
    fragmentManager: FragmentManager,
    cls: Class<out Fragment>,
    options: BaseNavOptions
) {
    fragmentManager.commit(options.allowLostState) {
        if (options.replace) {
            replace(container, cls, options.bundle, options.tag)
        } else {
            add(container, cls, options.bundle, options.tag)
        }
        if (options.addToBackStack) {
            addToBackStack(options.stackName)
        }
        setReorderingAllowed(true)
        //如果传入null，则不起作用
        setPrimaryNavigationFragment(options.primaryNavigationFragment)
    }
}

/**
 * 这会把当前fragment从parentFragmentManager移除。
 *
 * 请注意，在parentFragmentManager添加该fragment时，如果将其addToBackStack，则返回时会触发添加的事务
 */
fun Fragment.removeSelf() {
    val that = this
    parentFragmentManager.commit {
        remove(that)
    }
}

/**
 * 弹出parentFragmentManager回退栈最顶层的fragment
 */
fun Fragment.popUp() {
    parentFragmentManager.popUpTo(null, false)
}

/**
 * - 如果name不为null，includeSelf为false，将name之上的回退栈节点从回退栈中弹出
 * - 如果name不为null，includeSelf为false，将name及name之上的回退栈节点从回退栈中弹出
 * - 如果name为null，includeSelf为false，将弹出最上层的节点
 * - 如果name为null，includeSelf为true，将弹出所有节点
 *
 * @param name addToBackStack时传入的name
 * @param includeSelf 是否将[name]自己也弹出
 */
fun FragmentManager.popUpTo(
    name: String? = null,
    includeSelf: Boolean = false,
    async: Boolean = false
) {
    if (async) {
        popBackStack(name, if (includeSelf) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0)
    } else {
        popBackStackImmediate(
            name,
            if (includeSelf) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0
        )
    }
}

fun FragmentManager.popUpTo(cls: Class<*>, includeSelf: Boolean = false, async: Boolean = false) {
    popUpTo(cls.name, includeSelf, async)
}
