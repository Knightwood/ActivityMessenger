package com.kelp.android.fms.core

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.kelp.android.fms.action.AddAction
import com.kelp.android.fms.fragment_nodes.FragmentNode
import com.kelp.android.fms.fragment_nodes.FragmentNodeQueue
import com.kelp.android.fms.nav_options.NavOptions
import com.kelp.android.fms.nav_options.navOptions

/**
 * 使用此类来导航、跳转、切换显示fragment
 */
class NavController(
    private val impl: NavControllerImpl,
) : INavController by impl, LifecycleEventObserver {

    fun setUpLifecycle(ctx: NavControllerContext = impl.context) {
        val lifecycle = ctx.lifeCycle
        lifecycle.addObserver(this)
    }

    inline fun <reified TARGET : Fragment> launchFragment(
        noinline navOptionsBuilder: NavOptions.() -> Unit = {},
    ) {
        val options = navOptions(this, navOptionsBuilder)
        launchFragment(TARGET::class.java, options)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                impl.destroy()
                NavControllerManager.remove(impl.context.tag)
            }

            Lifecycle.Event.ON_CREATE -> {
                NavControllerManager.put(impl.context.tag, this)
            }

            else -> {}
        }
    }

}


/**
 * 导航的实际实现，隐藏细节。
 *
 * @property context NavControllerContext
 * @property containerView FragmentContainerView0，默认用于显示fragment的容器，
 */
class NavControllerImpl(
    val context: NavControllerContext,
    val containerView: FragmentContainerView
) : INavController {
    private var onBackPressedDispatcher: OnBackPressedDispatcher? = null
    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                popBackStack()
            }
        }

    /**
     * 是否启用回退事件监听器，默认为true
     */
    var enableOnBackPressedCallback = true
        set(value) {
            val result = value && !queue.isEmpty()
            onBackPressedCallback.isEnabled = result
            field = result
        }

    /**
     * 设置回退事件监听器，这将添加返回按键监听器，
     *
     * @param dispatcher
     */
    fun setOnBackPressedDispatcher(dispatcher: OnBackPressedDispatcher) {
        if (dispatcher == onBackPressedDispatcher) {
            return
        }
        // Remove the callback from any previous dispatcher
        onBackPressedCallback.remove()
        // Then add it to the new dispatcher
        onBackPressedDispatcher = dispatcher
        dispatcher.addCallback(context.lifecycleOwner, onBackPressedCallback)
    }

    override var primaryNavHost: Fragment? = null
        set(value) {
            field = value
            context.fragmentManager.commit {
                setPrimaryNavigationFragment(value)
            }
        }

    /**
     * 管理当前的fragment栈列表
     */
    val queue = FragmentNodeQueue()

    private val backQueue: ArrayDeque<FragmentNode> = ArrayDeque()

    fun getVisibleFragment(): List<Fragment> {
        val result = mutableListOf<Fragment>()
        val fragments = context.fragmentManager.fragments
        for (fragment in fragments) {
            if (fragment != null && fragment.isVisible)
                result.add(fragment)
        }
        return result
    }

    //<editor-fold desc="所有的navigation方法">
    override fun <T : Fragment> launchFragment(cls: Class<T>, navOptions: NavOptions) {
        if (navOptions.attachTo != this) {
            navOptions.attachTo.launchFragment(cls, navOptions)
            return
        }
        val node = FragmentNode(cls)
        AddAction(this, navOptions, node).run()
    }

    private fun popBackStack() {

    }
    //</editor-fold>

    override fun destroy() {

    }

}