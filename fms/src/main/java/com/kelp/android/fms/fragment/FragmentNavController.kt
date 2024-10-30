package com.kelp.android.fms.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.kelp.android.fms.runtime2.core.NavController
import com.kelp.android.fms.runtime2.core.NavControllerContext
import com.kelp.android.fms.runtime2.destination.NavOptions

class FragmentNavController(
    context: NavControllerContext,
    val containerView: FragmentContainerView,
) : NavController(context) {
    val navigator = FragmentNavigator(context.ctx, context.fragmentManager, containerView.id)

    fun navigateTo(cls: Class<Fragment>, navOptions: NavOptions.() -> Unit = {}) {

    }

}