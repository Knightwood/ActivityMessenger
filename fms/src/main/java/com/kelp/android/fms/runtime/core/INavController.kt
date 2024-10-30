package com.kelp.android.fms.runtime.core

import androidx.fragment.app.Fragment
import com.kelp.android.fms.fragment_nodes.FragmentNode
import com.kelp.android.fms.nav_options.NavOptions

interface INavController {
    var primaryNavHost: Fragment?
    fun destroy()
    fun <T : Fragment> launchFragment(cls: Class<T>, navOptions: NavOptions)
    fun popBackStack()
}