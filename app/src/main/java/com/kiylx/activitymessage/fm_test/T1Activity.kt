package com.kiylx.activitymessage.fm_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.NavController
import androidx.navigation.fragment.FixFragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kiylx.activitymessage.R
import com.kiylx.activitymessage.databinding.BottomNavLayoutBinding

/**
 * 这个activity持有一个controller，
 */
class T1Activity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding0: BottomNavLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding0 = BottomNavLayoutBinding.inflate(layoutInflater)
        setContentView(binding0.root)
        setUpNavigation()
        testFms()
    }

    fun testFms() {

    }

    /**
     * Set up navigation 方式1：使用导航图设置整体界面
     */
    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        //手动自定义navigator也可以在布局中直接使用FixNavHostFragment
//        val customNavigator = FixFragmentNavigator(navHostFragment.requireContext(), navHostFragment.childFragmentManager, R.id.nav_host_fragment)
//        navController.navigatorProvider.addNavigator(customNavigator)
//        navHostFragment.navController.setGraph(R.navigation.total_nav_graph)
        findViewById<BottomNavigationView>(R.id.bottom_nav)?.run {
            setupWithNavController(navController)
        }
    }
}