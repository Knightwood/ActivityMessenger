package com.kiylx.activitymessage.activity_message_test

import android.app.Activity
import com.kiylx.libx.activitymessenger.androidx.finish
import java.util.UUID

class ThirdActivity : Activity() {

    override fun onStart() {
        super.onStart()
        finish("key" to "finish" + UUID.randomUUID())
    }
}