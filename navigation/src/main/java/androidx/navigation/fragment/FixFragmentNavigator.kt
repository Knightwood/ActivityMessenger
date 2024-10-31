package androidx.navigation.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.OnBackStackChangedListener
import androidx.fragment.app.FragmentManager.isLoggingEnabled
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.NavigatorState
import java.lang.reflect.Field
import java.lang.reflect.Method


@Navigator.Name("fix-fragment")
public open class FixFragmentNavigator(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) : FragmentNavigator(context, fragmentManager, containerId) {
    private val TAG: String = "FixFragmentNavigator"

    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        super.navigate(entries, navOptions, navigatorExtras)
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
                    savedIds().remove(entry.id))
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
        Log.d(TAG, "navigate: ${state.backStack.value}")
    }


    override fun onLaunchSingleTop(backStackEntry: NavBackStackEntry) {
        super.onLaunchSingleTop(backStackEntry)
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
        val frag = fragmentManager.fragmentFactory.instantiate(context.classLoader, className)
        frag.arguments = args
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

        ft.replace(containerId, frag, entry.id)
        ft.setPrimaryNavigationFragment(frag)
        ft.setReorderingAllowed(true)
        Log.d(TAG, "createFragmentT: ${frag.tag} === ${entry.id}")
        return ft
/*
        val fragments: List<Fragment> = fragmentManager.fragments
        for (fragment in fragments) {
            ft.hide(fragment)
        }
        if (!frag.isAdded) {
            ft.add(containerId, frag, className)
        }
        ft.show(frag)
        ft.setPrimaryNavigationFragment(frag)

        @IdRes val destId = destination.id
        val mBackStack: ArrayDeque<Int>
        try {
            val field = FragmentNavigator::class.java.getDeclaredField("mBackStack")
            field.isAccessible = true
            mBackStack = field[this] as ArrayDeque<Int>
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException()
        }

        val initialNavigation = mBackStack.isEmpty()
        val isSingleTopReplacement = navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && mBackStack.last() == destId

        val isAdded: Boolean
        if (initialNavigation) {
            isAdded = true
        } else if (isSingleTopReplacement) {
            if (mBackStack.size > 1) {
                fragmentManager.popBackStack(
                    generateBackStackName(mBackStack.size, mBackStack.last()),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                ft.addToBackStack(generateBackStackName(mBackStack.size, destId))
            }
            isAdded = false
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size + 1, destId))
            isAdded = true
        }

        ft.setReorderingAllowed(true)
        if (isAdded) {
            mBackStack.add(destId)
        }
        return ft*/
    }

    //navigate需要的方法重复类直接复制过来就可以
    private fun generateBackStackName(backStackIndex: Int, destId: Int): String {
        return "$backStackIndex-$destId"
    }

    private fun savedIds(): MutableSet<String> {
        val reflect_field: Field =
            FragmentNavigator::class.java.getDeclaredField("savedIds")
        reflect_field.isAccessible = true
        return reflect_field.get(this) as MutableSet<String>
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