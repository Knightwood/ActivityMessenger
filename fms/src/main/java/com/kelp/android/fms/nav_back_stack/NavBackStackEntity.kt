package com.kelp.android.fms.nav_back_stack

import android.content.Context
import androidx.annotation.RestrictTo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class NavBackStackEntity(
    private val context: Context?,
    public var destination: Class<Fragment>,
    private var hostLifecycleState: Lifecycle.State = Lifecycle.State.CREATED,
) : LifecycleOwner {
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