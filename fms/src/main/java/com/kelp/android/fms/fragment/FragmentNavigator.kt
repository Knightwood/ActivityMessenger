package com.kelp.android.fms.fragment

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.kelp.android.fms.runtime2.core.Navigator
import com.kelp.android.fms.runtime2.destination.NavDestination

class FragmentNavigator(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) : Navigator<NavDestination>() {

}