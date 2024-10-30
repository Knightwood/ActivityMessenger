package com.kiylx.libx.activitymessenger.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.kiylx.libx.activitymessenger.R
import java.util.LinkedList


fun Fragment.applyOp(containerId: Int, block: FMHelper.() -> Unit) {
    val helper = FMHelper(childFragmentManager, containerId)
    helper.block()
    helper.execute()
}

fun FragmentActivity.applyOp(containerId: Int, block: FMHelper.() -> Unit) {
    val helper = FMHelper(supportFragmentManager, containerId)
    helper.block()
    helper.execute()
}

inline fun <reified TARGET : Fragment> Fragment.launchFragment(crossinline navOptions: (NavOptions.() -> Unit) = {}) {
    val options = NavOptions()
    options.navOptions()
    val container = requireActivity().getContainer(options.containerId)
    launchFragmentInternal(container, childFragmentManager, TARGET::class.java, options)
}

inline fun <reified TARGET : Fragment> FragmentActivity.launchFragment(crossinline navOptions: (NavOptions.() -> Unit) = {}) {
    val options = NavOptions()
    options.navOptions()
    val container = getContainer(options.containerId)
    launchFragmentInternal(container, supportFragmentManager, TARGET::class.java, options)
}


fun launchFragmentInternal(
    container: Int,
    fragmentManager: FragmentManager,
    cls: Class<out Fragment>,
    options: NavOptions
) {
    fragmentManager.commit {
        setReorderingAllowed(true)
        if (options.replace) {
            replace(container, cls, options.bundle, options.tag)
        } else {
            add(container, cls, options.bundle, options.tag)
        }
        if (options.addToBackStack) {
            addToBackStack(null)
        }
    }
}

/**
 * 如果id代表的容器存在，则返回id。
 *
 * 否则创建一个FragmentContainerView作为容器，并将其添加到最顶层，返回id。
 *
 * @param id
 * @return
 */
fun Activity.getContainer(id: Int? = null): Int {
    if (id != null) {
        return id
    } else {
        val containerView = FragmentContainerView(this)
        containerView.id = R.id.f_common_container
        containerView.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        val rootView = findViewById<FrameLayout>(android.R.id.content)
        rootView.addView(containerView)
        return containerView.id
    }
}

fun Fragment.finish() {
    val that = this
    parentFragmentManager.commit {
        remove(that)
    }
}

/**
 * 简化FragmentManager的操作 用法
 *
 * ```
 * 方式1 这个方式可用于Fragment和FragmentActivity,且不用自己构造FMHelper
 *   applyOp {
 *     hideAllFragment()
 *     addFragment<BlankFragment2>(R.id.fragment_container_view)
 *   }
 *
 *  方式2
 *   FMHelper(childFragmentManager).apply {
 *       hideAllFragment()
 *       addFragment<BlankFragment2>(R.id.fragment_container_view)
 *   }.execute()
 *
 *  方式3
 *   FMHelper(childFragmentManager).execute {
 *       hideAllFragment()
 *       addFragment<BlankFragment2>(R.id.fragment_container_view)
 *   }
 * ```
 *
 * @property fragmentManager
 */
@SuppressLint("CommitTransaction")
class FMHelper(
    val fragmentManager: FragmentManager,
    val containerId: Int,
) {
    private val queue: LinkedList<FragmentTransaction.() -> Unit> = LinkedList()

    /**
     * 使用block块添加多种操作，然后内部会调用execute将所有操作一并执行
     *
     * @param block
     */
    fun execute(block: FMHelper.() -> Unit) {
        block()
        execute()
    }

    /**
     * 执行添加到queue的所有操作
     */
    fun execute() {
        fragmentManager.commit {
            queue.forEach { it() }
        }
        clear()
    }

    fun clear() {
        queue.clear()
    }

    fun push(body: FragmentTransaction.() -> Unit) {
        queue.add(body)
    }

    inline fun <reified T : Fragment> addFragment(crossinline navOptions: (NavOptions.() -> Unit) = {}) {
        push {
            val options = NavOptions(containerId = this@FMHelper.containerId)
            options.navOptions()
            setReorderingAllowed(true)
            val container = options.containerId ?: this@FMHelper.containerId
            if (options.replace) {
                replace(container, T::class.java, options.bundle, options.tag)
            } else {
                add(container, T::class.java, options.bundle, options.tag)
            }
            if (options.addToBackStack) {
                addToBackStack(null)
            }
        }
    }

    inline fun <reified T : Fragment> showFragment(tag: String? = null) {
        push {
            findFragment<T>(tag)?.let {
                show(it)
            }
        }
    }

    /**
     * 隐藏其他所有fragment，并显示指定fragment。
     *
     * 操作等价于
     *
     * ```
     *
     * hideAllFragment()
     * showFragment<T>(tag)
     *
     * ```
     *
     * @param tag 要显示的fragment的tag
     * @param T 要显示的fragment
     */
    inline fun <reified T : Fragment> switchTo(tag: String? = null) {
        hideAllFragment()
        showFragment<T>(tag)
    }

    inline fun <reified T : Fragment> hideFragment(tag: String? = null) {
        push {
            findFragment<T>(tag)?.let {
                hide(it)
            }
        }
    }

    inline fun <reified T : Fragment> removeFragment(tag: String? = null) {
        push {
            findFragment<T>(tag)?.let {
                remove(it)
            }
        }
    }

    fun hideAllFragment() {
        push {
            fragmentManager.fragments.forEach {
                if (it.isVisible) {
                    hide(it)
                }
            }
        }
    }

    fun removeAllFragment() {
        push {
            fragmentManager.fragments.forEach {
                if (it.isVisible) {
                    remove(it)
                }
            }
        }
    }

    fun showAllFragment() {
        push {
            fragmentManager.fragments.forEach {
                if (!it.isVisible) {
                    show(it)
                }
            }
        }
    }

    inline fun <reified T : Fragment> findFragment(tag: String? = null): Fragment? {
        return fragmentManager.run {
            tag?.let { findFragmentByTag(it) } ?: fragments.find {
                it.javaClass == T::class.java
            }
        }
    }
}

