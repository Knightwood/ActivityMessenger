package com.kelp.android.fms.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.kelp.android.fms.runtime2.core.NavController
import com.kelp.android.fms.runtime2.core.NavControllerContext

class FragmentNavHelper {}

fun Fragment.fragmentController(
    containerView: FragmentContainerView,
): NavController {
    val ctx = NavControllerContext(activity = null, fragment = this)
    val controller = FragmentNavController(ctx, containerView)
    return controller
}