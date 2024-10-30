package com.kelp.android.fms.runtime2.core

import com.kelp.android.fms.runtime2.destination.NavBackStackEntry
import com.kelp.android.fms.runtime2.destination.NavDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 实际的导航，一方面是回退栈节点的管理，另一方面是真正的导航行为实现。
 * 因此，回退栈节点的管理由NavController实现了，而另一部分则交给Navigator实现。
 * 同时，每一个回退节点还需要持有Navigator引用。
 *
 * @property context
 */
open class NavController(
    val context: NavControllerContext,
) {
    val navigator: Navigator<*>

    private val backQueue: ArrayDeque<NavBackStackEntry> = ArrayDeque()

    val _visibleEntries = MutableStateFlow(emptyList<NavBackStackEntry>())
    val visibleEntries: StateFlow<List<NavBackStackEntry>> =
        _visibleEntries.asStateFlow()

    open fun navigateInternal(destination: NavDestination) {
        val backStackEntry = NavBackStackEntry(destination)
        backQueue.addLast(backStackEntry)
    }

    open fun back() {
        if (backQueue.size > 0) {
            backQueue.removeLast()
        }
    }
}