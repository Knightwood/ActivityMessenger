package com.kelp.android.fms.runtime2.destination

import android.content.Context
import androidx.annotation.RestrictTo
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.kelp.android.fms.runtime2.core.NavHost

/**
 * 在新的设计中，首先是没有导航图的，没有节点定义。 导航时，配置导航参数，生成回退栈节点，并添加到回退栈中。
 * 导航方法的实现，由NavController 来完成。
 *
 * @property destination 导航目的地
 * @property hostLifecycleState
 */
class NavBackStackEntry(
    var destination: NavDestination,
    private var hostLifecycleState: Lifecycle.State = Lifecycle.State.CREATED,
) : LifecycleOwner {

    /**
     * 这个回退节点属于哪个 NavHost
     */
    var parent: NavHost? = null

    private var _lifecycle = LifecycleRegistry(this)

    override val lifecycle: Lifecycle
        get() = _lifecycle

    @get:RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    @set:RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public var maxLifecycle: Lifecycle.State = Lifecycle.State.INITIALIZED
        set(maxState) {
            field = maxState
            updateState()
        }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public fun handleLifecycleEvent(event: Lifecycle.Event) {
        hostLifecycleState = event.targetState
        updateState()
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public fun updateState() {
        if (hostLifecycleState.ordinal < maxLifecycle.ordinal) {
            _lifecycle.currentState = hostLifecycleState
        } else {
            _lifecycle.currentState = maxLifecycle
        }
    }

}