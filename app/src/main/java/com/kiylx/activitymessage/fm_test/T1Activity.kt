package com.kiylx.activitymessage.fm_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kiylx.activitymessage.R
import com.kiylx.activitymessage.fm_test.home.BlankFragment1
import com.kiylx.libx.activitymessenger.fragments.applyOp
import com.kiylx.libx.activitymessenger.fragments.launchFragment

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
        launchFragment<BlankFragment1>(R.id.fragment_container_view) {
            params("param1" to "a", "param2" to "b")
            replace = true
            addToBackStack = false
        }
        /*applyOp(R.id.fragment_container_view) {
            addFragment<BlankFragment1> {
                params("param1" to "a", "param2" to "b")
                replace = true
                addToBackStack = false
            }
        }*/
    }
}