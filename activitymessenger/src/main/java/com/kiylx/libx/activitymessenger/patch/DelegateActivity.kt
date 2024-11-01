package com.kiylx.libx.activitymessenger.patch

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import java.util.UUID

/**
 * Delegate activity
 * 用来处理一些只能在FragmentActivity中执行的代码。
 * 比如在普通的或者ComponentActivity中使用launchActivityForResult。会使用到透明的fragment执行请求，
 * 而fragment需要有一个FragmentActivity宿主，所以需要在DelegateActivity中处理。
 *
 * author: knightwood
 */
class DelegateActivity : FragmentActivity() {
    private lateinit var uuid: UUID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT


        uuid = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("uuid", UUID::class.java)!!
        } else {
            intent.getSerializableExtra("uuid") as UUID
        }

        supportFragmentManager.setFragmentResultListener(
            IntentActionDelegateHolder.REQUEST_KEY,
            this,
        ) { _, bundle ->
            val result = bundle.getBoolean(IntentActionDelegateHolder.RESULT_KEY)
            if (result) // Hahaha, in fact it will never be false here
                release()
        }

        //execute startActivityForResult
        IntentActionDelegateHolder.holder[uuid]!!.invoke(this)
    }

    private fun release() {
        IntentActionDelegateHolder.holder.remove(uuid)
        finish()
    }

}