package com.kiylx.activitymessage.fm_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kiylx.activitymessage.R
import com.kiylx.activitymessage.fm_test.home.BlankFragment1
import com.kiylx.libx.activitymessenger.fms.fragmentController
import com.kiylx.libx.activitymessenger.fms.impl.launchFragment
import com.kiylx.libx.activitymessenger.fms.mode.LaunchMode

/**
 * 这个activity持有一个controller，
 */
class T1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        testFms()
    }

    fun testFms() {
        val controller = fragmentController(findViewById(R.id.fragment_container_view))
        controller.launchFragment<BlankFragment1>() {
            root = true
            launchMode = LaunchMode.SingleInTask
            params("param1" to "1", "param2" to "2")
            newInstanceFactory = {
                BlankFragment1()
            }
        }
    }
}