package com.kelp.android.fms.runtime.core

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
import com.kelp.android.fms.nav_back_stack.NavBackStackEntity
import com.kelp.android.fms.nav_options.NavOptions
import com.kelp.android.fms.nav_options.navOptions
import com.kelp.android.fms.runtime2.core.NavControllerContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 使用此类来导航、跳转、切换显示fragment
 */
class NavController(
    private val impl: NavControllerImpl,
) : INavController by impl, LifecycleEventObserver {
    val context = impl.context
    val backQueue = impl.backQueue

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
            val result = value && !backQueue.isEmpty()
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
        dispatcher.addCallback(impl.context.lifecycleOwner, onBackPressedCallback)
    }

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
    override var primaryNavHost: Fragment? = null
        set(value) {
            field = value
            context.fragmentManager.commit {
                setPrimaryNavigationFragment(value)
            }
        }


    internal val backQueue: ArrayDeque<NavBackStackEntity> = ArrayDeque()

    private val _visibleEntries: MutableStateFlow<List<NavBackStackEntity>> =
        MutableStateFlow(emptyList())
    public val visibleEntries: StateFlow<List<NavBackStackEntity>> =
        _visibleEntries.asStateFlow()

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

    override fun popBackStack() {

    }
    //</editor-fold>

    override fun destroy() {

    }

}