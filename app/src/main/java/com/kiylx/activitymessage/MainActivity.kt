package com.kiylx.activitymessage

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import com.kiylx.activitymessage.databinding.ActivityMainBinding
import com.kiylx.activitymessage.fm_test.T1Activity
import com.kiylx.activitymessage.activity_message_test.MainViewModel
import com.kiylx.activitymessage.activity_message_test.ThirdActivity
import com.kiylx.libx.activitymessenger.androidx.launchActivity
import com.kiylx.libx.activitymessenger.androidx.launchActivityForResult
import com.kiylx.libx.activitymessenger.core.extraAct
import com.kiylx.libx.activitymessenger.patch.IntentActionDelegateHolder

/**
 * 有一点要注意，当app向intent放序列化后的数据启动另一个activity时，
 * 若被启动那个activity在读取intent时，若找不到序列化的类，是会报错的。 但在高版本或某些机型上，这个报错会被隐藏，并返回空的数据。
 *
 * https://blog.csdn.net/chzphoenix/article/details/79799289
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var str: String? by extraAct("argName")//委托机制，从intent中读取

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mainViewModel =
            ViewModelProvider(this).get(MainViewModel::class.java)

        val textView: TextView = binding.textHome
        mainViewModel.text.observe(this) {
            textView.text = it
        }
        val a = arrayOf("Key" to "Value")
        binding.btn1.setOnClickListener {
            launchActivityForResult<ThirdActivity>(*a) { code, intent ->
                mainViewModel.changeText(intent?.getStringExtra("key"))
            }
        }

        binding.btn2.setOnClickListener {
            IntentActionDelegateHolder.delegate(this) {
                it.launchActivityForResult<ThirdActivity>(*a) { code, intent ->
                    mainViewModel.changeText(intent?.getStringExtra("key"))
                }
            }
        }
        binding.btn3.setOnClickListener {
            launchActivity<T1Activity>()
        }

    }
}