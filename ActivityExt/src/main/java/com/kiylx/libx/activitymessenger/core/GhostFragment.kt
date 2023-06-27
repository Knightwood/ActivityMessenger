package com.kiylx.libx.activitymessenger.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

//=================================================最终方法=======================================//
/**
 * @param starter
 * @param intent 传入intent
 * @param callback startActivityForResult之后执行block块
 */
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

/**
 * @param starter
 * @param intent 传入intent
 * @param useActivityFM true:使用supportFragmentManager,false:使用
 * @param callback startActivityForResult之后执行block块
 */
inline fun finallyLaunchActivityForResult(
    starter: Fragment,
    intent: Intent,
    useActivityFM: Boolean = true,
    crossinline callback: ((result: Intent?) -> Unit)
) {
    val fm: FragmentManager = if (useActivityFM) {
        starter.activity?.supportFragmentManager ?: throw Exception("no activity attached")
    } else {
        starter.childFragmentManager
    }
    val fragment = GhostFragment()
    fragment.init(intent) { result ->
        callback(result)
        fm.beginTransaction().remove(fragment)
            .commitAllowingStateLoss()
    }
    fm.beginTransaction()
        .add(fragment, GhostFragment::class.java.simpleName)
        .commitAllowingStateLoss()
}

/**
 * @param starter
 * @param intent 传入intent
 * @param useActivityFM true:使用supportFragmentManager,false:使用childFragmentManager
 * @param callback startActivityForResult之后执行block块
 */
inline fun finallyLaunchActivityForResultCode(
    starter: Fragment,
    intent: Intent,
    useActivityFM: Boolean = true,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) {
    val fm: FragmentManager = if (useActivityFM) {
        starter.activity?.supportFragmentManager ?: throw Exception("no activity attached")
    } else {
        starter.childFragmentManager
    }
    val fragment = GhostFragment()
    fragment.init(intent) { resultCode, result ->
        callback(resultCode, result)
        fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
    }
    fm.beginTransaction()
        .add(fragment, GhostFragment::class.java.simpleName)
        .commitAllowingStateLoss()
}

/**
 * @param starter
 * @param intent 传入intent
 * @param callback startActivityForResult之后执行block块
 */
inline fun finallyLaunchActivityForResultCode(
    starter: FragmentActivity,
    intent: Intent,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) {
    val fragment = GhostFragment()
    fragment.init(intent) { resultCode, result ->
        callback(resultCode, result)
        starter.supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
    }
    starter.supportFragmentManager.beginTransaction()
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
                it(activityResult.resultCode, activityResult.data)
            }
            val result: Intent? =
                if (activityResult.resultCode == Activity.RESULT_OK) activityResult.data else null
            callback?.let { it(result) }
        }

    private var intent: Intent? = null

    /**
     * the callback to be called on the main thread when activity result is available
     */
    private var callback: ((result: Intent?) -> Unit)? = null

    /**
     * the callback to be called on the main thread when activity result is available
     */
    private var callback2: ((resultCode: Int, result: Intent?) -> Unit)? = null

    /**
     * @param callback onActivityResult的回调
     */
    fun init(intent: Intent, callback: ((result: Intent?) -> Unit)) {
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