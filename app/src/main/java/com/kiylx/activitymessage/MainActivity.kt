package com.kiylx.activitymessage

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import com.kiylx.activitymessage.databinding.ActivityMainBinding
import com.kiylx.activitymessage.ui.SecondActivity
import com.kiylx.activitymessage.ui.home.HomeViewModel
import com.kiylx.libx.activity_ktx.activitymessenger.androidx.launchActivityForResult

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val a = arrayOf("Key" to "Value")
        launchActivityForResult(SecondActivity::class.java, *a) {

        }
//        launchActivityForResult<MainActivity>("Key" to "Value") {}

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(this) {
            textView.text = it
        }
    }
}