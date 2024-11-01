package com.kiylx.libx.activitymessenger.fragments

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
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

