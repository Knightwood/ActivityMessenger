package com.kiylx.activitymessage.fm_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kiylx.activitymessage.R

/**
 * 这个activity持有一个controller，
 */
class T1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        testFms()
    }

    fun testFms() {}
}