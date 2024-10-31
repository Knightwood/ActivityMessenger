package com.androidx.navigation.fix

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

@Navigator.Name("fix-fragment")
class FixFragmentNavigator(
    context: Context,
    fragmentManager: FragmentManager, containerId: Int
) : FragmentNavigator(context, fragmentManager, containerId) {

}