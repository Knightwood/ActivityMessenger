package com.kelp.android.fms.runtime

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import com.kelp.android.fms.runtime.core.NavController
import com.kelp.android.fms.runtime2.core.NavControllerContext
import com.kelp.android.fms.runtime.core.NavControllerImpl

class NavControllerHelper {
}

fun Fragment.fragmentController2(
    containerView: FragmentContainerView,
): NavController {
    val ctx = NavControllerContext(activity = null, fragment = this)
    val impl = NavControllerImpl(ctx, containerView)
    val controller = NavController(impl)
    controller.setUpLifecycle()
    controller.primaryNavHost = this
    return controller
}

fun FragmentActivity.fragmentController2(
    containerView: FragmentContainerView,
): NavController {
    val ctx = NavControllerContext(activity = this, fragment = null)
    val impl = NavControllerImpl(ctx, containerView)
    val controller = NavController(impl)
    controller.setUpLifecycle()
    return controller
}

