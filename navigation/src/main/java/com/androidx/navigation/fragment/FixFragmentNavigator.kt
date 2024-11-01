package com.androidx.navigation.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.OnBackStackChangedListener
import androidx.fragment.app.FragmentManager.isLoggingEnabled
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.NavigatorState
import androidx.navigation.fragment.FragmentNavigator
import java.lang.reflect.Field
import java.lang.reflect.Method


@Navigator.Name("fragment")
public open class FixFragmentNavigator(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) : FragmentNavigator(context, fragmentManager, containerId) {
    private val TAG: String = "FixFragmentNavigator"
    private lateinit var savedIds: MutableSet<String>

    init {
        runCatching {
            val reflect_field: Field =
                FragmentNavigator::class.java.getDeclaredField("savedIds")
            reflect_field.isAccessible = true
            savedIds = reflect_field.get(this) as MutableSet<String>
        }.getOrElse {
            Log.e(TAG, "init: reflect_field savedIds failed")
        }
    }

    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        if (fragmentManager.isStateSaved) {
            Log.i(TAG, "Ignoring navigate() call: FragmentManager has already saved its state")
            return
        }
        for (entry in entries) {
            navigate(entry, navOptions, navigatorExtras)
        }
    }


    @SuppressLint("RestrictedApi")
    private fun navigate(
        entry: NavBackStackEntry,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        val initialNavigation = state.backStack.value.isEmpty()
        val restoreState =
            (navOptions != null &&
                    !initialNavigation &&
                    navOptions.shouldRestoreState() &&
                    savedIds.remove(entry.id))
        if (restoreState) {
            Log.d(TAG, "navigate: restoreState")
            // Restore back stack does all the work to restore the entry
            fragmentManager.restoreBackStack(entry.id)
            state.pushWithTransition(entry)
            return
        }
        val ft = createFragmentTransaction(entry, navOptions)

        if (!initialNavigation) {
            val outgoingEntry = state.backStack.value.lastOrNull()
            // if outgoing entry is initial entry, FragmentManager still triggers onBackStackChange
            // callback for it, so we don't filter out initial entry here
            if (outgoingEntry != null) {
                _addPendingOps_(outgoingEntry.id)
            }
            // add pending ops here before any animation (if present) starts
            _addPendingOps_(entry.id)
            ft.addToBackStack(entry.id)
        }

        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key, value)
            }
        }
        ft.commit()
        // The commit succeeded, update our view of the world
        if (isLoggingEnabled(Log.VERBOSE)) {
            Log.v(TAG, "Calling pushWithTransition via navigate() on entry $entry")
        }
        state.pushWithTransition(entry)
    }


    override fun onLaunchSingleTop(backStackEntry: NavBackStackEntry) {
        if (fragmentManager.isStateSaved) {
            Log.i(
                TAG,
                "Ignoring onLaunchSingleTop() call: FragmentManager has already saved its state"
            )
            return
        }
        val ft = createFragmentTransaction(backStackEntry, null)
        val backstack = state.backStack.value
        if (backstack.size > 1) {
            // If the Fragment to be replaced is on the FragmentManager's
            // back stack, a simple replace() isn't enough so we
            // remove it from the back stack and put our replacement
            // on the back stack in its place
            val incomingEntry = backstack.elementAtOrNull(backstack.lastIndex - 1)
            if (incomingEntry != null) {
                _addPendingOps_(incomingEntry.id)
            }
            _addPendingOps_(backStackEntry.id, isPop = true)
            fragmentManager.popBackStack(
                backStackEntry.id,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )

            _addPendingOps_(backStackEntry.id, deduplicate = false)
            ft.addToBackStack(backStackEntry.id)
        }
        ft.commit()
        // The commit succeeded, update our view of the world
        state.onLaunchSingleTop(backStackEntry)
    }

    private fun createFragmentTransaction(
        entry: NavBackStackEntry,
        navOptions: NavOptions?
    ): FragmentTransaction {
        val destination = entry.destination as Destination
        val args = entry.arguments
        var className = destination.className
        if (className[0] == '.') {
            className = context.packageName + className
        }
        //我们需要从现有的列表中查找，而不是新建实例
//        val frag = fragmentManager.fragmentFactory.instantiate(context.classLoader, className)
//        frag.arguments = args
        val ft = fragmentManager.beginTransaction()
        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }
//
//        ft.replace(containerId, frag, entry.id)
//        ft.setPrimaryNavigationFragment(frag)
//        ft.setReorderingAllowed(true)
//        return ft

        /**
         * show/hide有不少的问题：
         * 1. 比如当前已经显示了一个视频页面，接下来点击视频推荐，
         * 那么即将显示的fragment与当前的视频页面是同一个fragment类，
         * 却是不同的实例，这种情况下就不能show/hide，而是add。但很显然没有手段能区分这种情况。
         * 2. 使用了singleTop
         */
        //region 添加的代码

        var frag: Fragment? = fragmentManager.primaryNavigationFragment //查找当前导航栈顶的fragment

        //判断是否需要重新创建一个新的Fragment
        var needRecreate = false
        //提前判断。如果当栈顶Fragment 等于 目的地Fragment。在逻辑上属于自己打开自己。
        //所以当逻辑是这样的，就需要重新创建一个自己。
        if (frag?.javaClass?.name == className) {
            needRecreate = true
        }

        //如果栈顶存在Fragment，就hide。
        if (frag != null) {
            ft.setMaxLifecycle(frag, Lifecycle.State.STARTED)
            ft.hide(frag)
        }
        //查找目标导航fragment 如果查找到了就show这个fragment，如果没有查找到就创建一个新的fragment。
        frag = fragmentManager.findFragmentByTag(entry.id)
        //这里判断是否需要重建，如果需要重建就不show。而是重新创建一个。
        if (frag != null && !needRecreate) {
            //fragment 已经存在显示
            ft.setMaxLifecycle(frag, Lifecycle.State.RESUMED)
            ft.show(frag)
        } else {
            //fragment 不存在创建，添加
            frag = fragmentManager.fragmentFactory.instantiate(context.classLoader, className)
            frag.arguments = args//设置参数.
            ft.add(containerId, frag, entry.id)
        }
        //endregion
        
        ft.setPrimaryNavigationFragment(frag)
        ft.setReorderingAllowed(true)
        return ft
    }


    private fun _addPendingOps_(id: String, isPop: Boolean = false, deduplicate: Boolean = true) {
        val method: Method = FragmentNavigator::class.java.getDeclaredMethod(
            "addPendingOps",
            String::class.java,
            Boolean::class.java,
            Boolean::class.java
        )
        method.isAccessible = true
        method.invoke(this, id, isPop, deduplicate)
    }

}