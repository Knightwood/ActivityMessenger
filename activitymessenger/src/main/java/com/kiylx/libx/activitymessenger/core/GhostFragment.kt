package com.kiylx.libx.activitymessenger.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

//=================================================最终方法=======================================//
inline fun finallyLaunchActivityForResult(
    starter: FragmentActivity?,
    intent: Intent,
    crossinline callback: ((result: Intent?) -> Unit)
) {
    starter.runIfNonNull {
        val fragment = GhostFragment()
        fragment.init(intent) { result ->
            callback(result)
            it.supportFragmentManager.beginTransaction().remove(fragment)
                .commitAllowingStateLoss()
        }
        it.supportFragmentManager.beginTransaction()
            .add(fragment, GhostFragment::class.java.simpleName)
            .commitAllowingStateLoss()
    }
}

inline fun finallyLaunchActivityForResultCode(
    starter: FragmentActivity?,
    intent: Intent,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = starter.runIfNonNull {
    val fragment = GhostFragment()
    fragment.init(intent) { resultCode, result ->
        callback(resultCode, result)
        it.supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
    }
    it.supportFragmentManager.beginTransaction()
        .add(fragment, GhostFragment::class.java.simpleName)
        .commitAllowingStateLoss()
}


/**
 * 真正执行startActivityForResult的地方
 *
 * 初始化此实例，调用[init]赋予callback,在attach到activity时自动执行startActivityForResult，
 * 获取结果后，通过callback传出去。
 */
class GhostFragment : Fragment() {
    private var register =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            callback2?.let {
                it(activityResult.resultCode,activityResult.data)
            }
            val result: Intent? =
                if (activityResult.resultCode == Activity.RESULT_OK) activityResult.data else null
            callback?.let { it(result) }
        }

    private var intent: Intent? = null
    private var callback: ((result: Intent?) -> Unit)? = null
    private var callback2: ((resultCode: Int, result: Intent?) -> Unit)? = null

    fun init( intent: Intent, callback: ((result: Intent?) -> Unit)) {
        this.intent = intent
        this.callback = callback
    }

    fun init(
        intent: Intent,
        callback: ((resultCode: Int, result: Intent?) -> Unit)
    ) {
        this.intent = intent
        this.callback2 = callback
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        register.launch(intent)
    }


    override fun onDetach() {
        super.onDetach()
        intent = null
        callback = null
        callback2 = null
        register.unregister()
    }

}