@file:Suppress("DEPRECATION", "unused", "UNCHECKED_CAST")

package com.kiylx.libx.activitymessenger.androidx

import android.app.Activity
import android.app.Application
import android.content.ComponentName
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.kiylx.libx.activitymessenger.androidx.ActivityMessenger.launchActivity
import com.kiylx.libx.activitymessenger.core.finallyLaunchActivityForResult
import com.kiylx.libx.activitymessenger.core.finallyLaunchActivityForResultCode
import com.kiylx.libx.activitymessenger.core.putExtras

/**
 * 比起ActivityMessenger.kt,这个是扩展方法版本
 * startActivity和startActivityResult的方法集合
 */
//<editor-fold desc="startActivity">
//<editor-fold desc="传入泛型">
//====================================startActivity==================================================//
/**
 * 作用同[FragmentActivity.launchActivity] 示例：
 *
 * ```
 *      //不携带参数
 *      launchActivity<TestActivity>()
 *
 *      //携带参数（可连续多个键值对）
 *      launchActivity<TestActivity>("Key" to "Value")
 * ```
 *
 * @param params extras键值对
 * @param TARGET 要启动的Activity
 */
inline fun <reified TARGET : FragmentActivity> FragmentActivity.launchActivity(
    vararg params: Pair<String, Any?>
) {
    this.startActivity(Intent(this, TARGET::class.java).putExtras(*params))
}

inline fun <reified TARGET : FragmentActivity> Fragment.launchActivity(
    vararg params: Pair<String, Any?>
) {
    this.requireActivity().run {
        startActivity(Intent(this, TARGET::class.java).putExtras(*params))
    }
}
//</editor-fold>

//<editor-fold desc="传参数">
/**
 * Fragment跳转，同[FragmentActivity.launchActivity] 示例：
 *
 * ```
 *      //不携带参数
 *      launchActivity(TestActivity::class.java)
 *
 *      //携带参数（可连续多个键值对）
 *     launchActivity(
 *         TestActivity::class.java,
 *         "Key1" to "Value",
 *         "Key2" to 123
 *     )
 * ```
 *
 * @param target 要启动的Activity
 * @param params extras键值对
 */
fun FragmentActivity.launchActivity(
    target: Class<out FragmentActivity>, vararg params: Pair<String, Any?>
) {
    this.startActivity(Intent(this, target).putExtras(*params))
}

fun Fragment.launchActivity(
    target: Class<out FragmentActivity>, vararg params: Pair<String, Any?>
) {
    this.requireActivity().run {
        startActivity(Intent(this, target).putExtras(*params))
    }
}
//</editor-fold>
//</editor-fold>
//====================================startActivityForResult==================================================//

//<editor-fold desc="startActivityForResult">

//<editor-fold desc="javaclass版本-传泛型">
//<editor-fold desc="javaclass版本-不带code">
/**
 * 作用同[FragmentActivity.launchActivityForResult] 示例：
 *
 * ```
 *      //不携带参数
 *      launchActivityForResult<TestActivity> {
 *          if (it == null) {
 *              //未成功处理，即（ResultCode != RESULT_OK）
 *          } else {
 *              //处理成功，这里可以操作返回的intent
 *          }
 *      }
 * ```
 *
 * 携带参数同[launchActivity]
 *
 * @param params extras键值对
 * @param callback onActivityResult的回调
 * @param TARGET 要启动的Activity
 */
inline fun <reified TARGET : FragmentActivity> FragmentActivity.launchActivityForResult(
    vararg params: Pair<String, Any?>, crossinline callback: ((result: Intent?) -> Unit)
) {
    launchActivityForResult(TARGET::class.java, *params, callback = callback)
}


inline fun <reified TARGET : FragmentActivity> Fragment.launchActivityForResult(
    vararg params: Pair<String, Any?>,
    crossinline callback: ((result: Intent?) -> Unit)
) {
    launchActivityForResult(
        target = TARGET::class.java,
        params = params,
        callback = callback
    )
}
//</editor-fold>

//<editor-fold desc="javaclass版本-带code">

//跟上面唯一区别是匿名函数参数多了一个ResultCode
/**
 * 作用同[FragmentActivity.launchActivityForResult] 示例：
 *
 * ```
 *      //不携带参数
 *      launchActivityForResult<TestActivity> {resultCode, result->
 *          if (resultCode == RESULT_OK) {
 *              //处理成功，这里可以操作返回的intent
 *          } else {
 *             //未成功处理
 *          }
 *      }
 * ```
 *
 * 携带参数同[launchActivity]
 *
 * @param params extras键值对
 * @param callback onActivityResult的回调
 * @param TARGET 要启动的Activity
 */
inline fun <reified TARGET : FragmentActivity> FragmentActivity.launchActivityForResultCode(
    vararg params: Pair<String, Any?>,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) {
    launchActivityForResultCode(TARGET::class.java, *params, callback = callback)
}


inline fun <reified TARGET : FragmentActivity> Fragment.launchActivityForResultCode(
    vararg params: Pair<String, Any?>,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) {
    launchActivityForResultCode(
        target = TARGET::class.java,
        params = params,
        callback = callback
    )
}
//</editor-fold>

//</editor-fold>

//上面的几个launchActivityForResult方法都是调用这里的方法======================================//
//<editor-fold desc="javaClass版本-传参数">
//<editor-fold desc="javaClass版本-不带code">
//带ResultCode版本=======================
/**
 * 作用同[FragmentActivity.launchActivityForResult] 示例：
 *
 * ```
 *      //不携带参数
 *      launchActivityForResult(TestActivity::class.java) {
 *          if (it == null) {
 *              //未成功处理，即（ResultCode != RESULT_OK）
 *          } else {
 *              //处理成功，这里可以操作返回的intent
 *          }
 *      }
 * ```
 *
 * 携带参数同[launchActivity]
 *
 * @param target 要启动的Activity
 * @param params extras键值对
 * @param callback onActivityResult的回调
 */
inline fun FragmentActivity.launchActivityForResult(
    target: Class<out FragmentActivity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((result: Intent?) -> Unit)
) {
    finallyLaunchActivityForResult(
        this,
        Intent(this, target).putExtras(*params),
        callback = callback
    )
}


inline fun Fragment.launchActivityForResult(
    target: Class<out FragmentActivity>,
    vararg params: Pair<String, Any?>,
    crossinline callback: ((result: Intent?) -> Unit)
) {
    finallyLaunchActivityForResult(
        this,
        Intent(this.activity, target).putExtras(*params),
        callback
    )
}
//</editor-fold>

//<editor-fold desc="javaClass版本-带code">
//带ResultCode版本=======================
/**
 * 作用同[FragmentActivity.launchActivityForResult] 示例：
 *
 * ```
 *      //不携带参数
 *      launchActivityForResult(TestActivity::class.java) {resultCode, result->
 *          if (resultCode == RESULT_OK) {
 *              //处理成功，这里可以操作返回的intent
 *          } else {
 *             //未成功处理
 *          }
 *      }
 * ```
 *
 * 携带参数同[launchActivity]
 *
 * @param target 要启动的Activity
 * @param params extras键值对
 * @param callback onActivityResult的回调
 */
inline fun FragmentActivity.launchActivityForResultCode(
    target: Class<out FragmentActivity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) {
    finallyLaunchActivityForResultCode(this, Intent(this, target).putExtras(*params), callback)
}


inline fun Fragment.launchActivityForResultCode(
    target: Class<out FragmentActivity>,
    vararg params: Pair<String, Any?>,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) {
    finallyLaunchActivityForResultCode(
        this,
        Intent(this.activity, target).putExtras(*params),
        callback = callback
    )
}
//</editor-fold>

//</editor-fold>

//=============================下面的是直接传intent版本,上面的传入的是多个params

//<editor-fold desc="直接传intent版本">

/**
 * 作用同[FragmentActivity.launchActivityForResult]
 *
 * @param intent intent
 * @param callback onActivityResult的回调
 */
inline fun FragmentActivity?.launchActivityForResult(
    intent: Intent, crossinline callback: ((result: Intent?) -> Unit)
) = this?.run {
    finallyLaunchActivityForResult(this, intent, callback)
}


inline fun Fragment.launchActivityForResult(
    intent: Intent, crossinline callback: ((result: Intent?) -> Unit)
) {
    finallyLaunchActivityForResult(this, intent ,callback)
}

inline fun FragmentActivity?.launchActivityForResultCode(
    intent: Intent, crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = this?.run {
    finallyLaunchActivityForResultCode(this, intent, callback)
}


inline fun Fragment.launchActivityForResultCode(
    intent: Intent, crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = finallyLaunchActivityForResultCode(this, intent ,callback)
//</editor-fold>
//</editor-fold>
//=============================================finish方法=======================
//<editor-fold desc="finish方法">

/**
 * 作用同[FragmentActivity.finish] 示例：
 *
 * ```
 *      finish(this, "Key" to "Value")
 * ```
 *
 * @param params extras键值对
 */
fun FragmentActivity.finish(vararg params: Pair<String, Any?>) = run {
    setResult(FragmentActivity.RESULT_OK, Intent().putExtras(*params))
    finish()
}

fun FragmentActivity.finish(intent: Intent) = run {
    setResult(FragmentActivity.RESULT_OK, intent)
    finish()
}
//</editor-fold>
//<editor-fold desc="toIntent方法">

/**
 * String转Intent对象
 *
 * 示例：
 *
 * ```
 *      val action = "android.media.action.IMAGE_CAPTURE"
 *      val intent = action.toIntent()
 * ```
 *
 * @param flags [Intent.setFlags]
 */
fun String.toIntent(flags: Int = 0): Intent = Intent(this).setFlags(flags)
//</editor-fold>

//<editor-fold desc="application启动activity方法">
/**
 * 启动任意activity
 *
 * ```
 * 用法：
 *     在application环境下启动settingsActivity
 *
 *     application.startActivity<SettingsActivity>()
 * ```
 *
 */
inline fun <reified TARGET : Activity> Application.launchActivity(vararg params: Pair<String, Any?>) {
    val componentName =
        ComponentName(this.packageName, TARGET::class.java.canonicalName)
    val intent = Intent()
    intent.putExtras(*params)
    intent.component = componentName
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

//</editor-fold>
