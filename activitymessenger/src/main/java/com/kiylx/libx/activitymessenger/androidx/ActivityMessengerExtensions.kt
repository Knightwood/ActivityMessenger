@file:Suppress("DEPRECATION", "unused", "UNCHECKED_CAST")

package com.kiylx.libx.activitymessenger.androidx

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.kiylx.libx.activitymessenger.androidx.ActivityMessenger.launchActivity
import com.kiylx.libx.activitymessenger.core.finallyLaunchActivityForResult
import com.kiylx.libx.activitymessenger.core.finallyLaunchActivityForResultCode
import com.kiylx.libx.activitymessenger.core.putExtras
import kotlin.reflect.KClass

/**
 * 比起ActivityMessenger.kt,这个是扩展方法版本
 * startActivity和startActivityResult的方法集合
 */

//====================================startActivity==================================================//
//传入实例版本
/**
 * 作用同[FragmentActivity.launchActivity] 示例：
 *
 * ```
 *      //不携带参数
 *      startActivity<TestActivity>()
 *
 *      //携带参数（可连续多个键值对）
 *      startActivity<TestActivity>("Key" to "Value")
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
//传入KClass版本
/**
 * Fragment跳转，同[FragmentActivity.launchActivity] 示例：
 *
 * ```
 *      //不携带参数
 *      startActivity(this, TestActivity::class)
 *
 *      //携带参数（可连续多个键值对）
 *     startActivity(
 *         TestActivity::class,
 *         "Key1" to "Value",
 *         "Key2" to 123
 *     )
 * ```
 *
 * @param target 要启动的Activity
 * @param params extras键值对
 */
fun FragmentActivity.launchActivity(
    target: KClass<out FragmentActivity>, vararg params: Pair<String, Any?>
) {
    this.startActivity(Intent(this, target.java).putExtras(*params))
}


fun Fragment.launchActivity(
    target: KClass<out FragmentActivity>, vararg params: Pair<String, Any?>
) {
    this.requireActivity().run {
        startActivity(Intent(this, target.java).putExtras(*params))
    }
}
//javaClass版本
/** 作用同上，以下三个方法为了兼容Java Class */
fun FragmentActivity.launchActivity(
    target: Class<out FragmentActivity>, vararg params: Pair<String, Any?>
) {this.startActivity(Intent(this, target).putExtras(*params))}

fun Fragment.launchActivity(
    target: Class<out FragmentActivity>, vararg params: Pair<String, Any?>
) {
    this.requireActivity().run {
        startActivity(Intent(this, target).putExtras(*params))
    }
}
//====================================startActivityForResult==================================================//

/**
 * 作用同[FragmentActivity.launchActivityForResult] 示例：
 *
 * ```
 *      //不携带参数
 *      startActivityForResult<TestActivity> {
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
    launchActivityForResult(TARGET::class, *params, callback = callback)
}


inline fun <reified TARGET : FragmentActivity> Fragment.launchActivityForResult(
    vararg params: Pair<String, Any?>, crossinline callback: ((result: Intent?) -> Unit)
) {
    requireActivity().launchActivityForResult(TARGET::class, *params, callback = callback)
}

//跟上面唯一区别是匿名函数参数多了一个ResultCode
/**
 * 作用同[FragmentActivity.launchActivityForResult] 示例：
 *
 * ```
 *      //不携带参数
 *      startActivityForResult<TestActivity> {resultCode, result->
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
    launchActivityForResultCode(TARGET::class, *params, callback = callback)
}


inline fun <reified TARGET : FragmentActivity> Fragment.launchActivityForResultCode(
    vararg params: Pair<String, Any?>,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) {
    requireActivity().launchActivityForResultCode(TARGET::class, *params, callback = callback)
}

//上面的几个launchActivityForResult方法都是调用这里的方法======================================//

//带ResultCode版本=======================
//KClass版本
/**
 * 作用同[FragmentActivity.launchActivityForResult] 示例：
 *
 * ```
 *      //不携带参数
 *      startActivityForResult(this, TestActivity::class) {
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
    target: KClass<out FragmentActivity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((result: Intent?) -> Unit)
) = ActivityMessenger.launchActivityForResult(this, target, *params, callback = callback)


inline fun Fragment.launchActivityForResult(
    target: KClass<out FragmentActivity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((result: Intent?) -> Unit)
) = activity?.run {
    ActivityMessenger.launchActivityForResult(this, target, *params, callback = callback)
}
//JavaClass版本
/** 作用同上，以下三个方法为了兼容Java Class */
inline fun FragmentActivity.launchActivityForResult(
    target: Class<out FragmentActivity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((result: Intent?) -> Unit)
) = ActivityMessenger.launchActivityForResult(this, target, *params, callback = callback)


inline fun Fragment.launchActivityForResult(
    target: Class<out FragmentActivity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((result: Intent?) -> Unit)
) = activity?.run {
    ActivityMessenger.launchActivityForResult(this, target, *params, callback = callback)
}


//带ResultCode版本=======================
//KClass版本
/**
 * 作用同[FragmentActivity.launchActivityForResult] 示例：
 *
 * ```
 *      //不携带参数
 *      startActivityForResult(this, TestActivity::class) {resultCode, result->
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
    target: KClass<out FragmentActivity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = ActivityMessenger.launchActivityForResultCode(this, target, *params, callback = callback)


inline fun Fragment.launchActivityForResultCode(
    target: KClass<out FragmentActivity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = activity?.run {
    ActivityMessenger.launchActivityForResultCode(this, target, *params, callback = callback)
}
//JavaClass版本
/** 作用同上，以下三个方法为了兼容Java Class */
inline fun FragmentActivity.launchActivityForResultCode(
    target: Class<out FragmentActivity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = ActivityMessenger.launchActivityForResultCode(this, target, *params, callback = callback)


inline fun Fragment.launchActivityForResultCode(
    target: Class<out FragmentActivity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = activity?.run {
    ActivityMessenger.launchActivityForResultCode(this, target, *params, callback = callback)
}
//=============================下面的是直接传intent版本,上面的传入的是多个params
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
) = activity?.run {
    finallyLaunchActivityForResult(this, intent, callback)
}

inline fun FragmentActivity?.launchActivityForResultCode(
    intent: Intent, crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = this?.run {
    finallyLaunchActivityForResultCode(this, intent, callback)
}


inline fun Fragment.launchActivityForResultCode(
    intent: Intent, crossinline callback: ((resultCode: Int, result: Intent?) -> Unit)
) = activity?.run {
    finallyLaunchActivityForResultCode(this, intent, callback)
}
//=============================================finish方法=======================
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