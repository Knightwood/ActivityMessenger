@file:Suppress("DEPRECATION", "unused", "UNCHECKED_CAST")

package com.kiylx.libx.activitymessenger.androidx

import android.app.Activity
import android.app.ActivityOptions
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.kiylx.libx.activitymessenger.androidx.ActivityMessenger.launchActivity
import com.kiylx.libx.activitymessenger.core.finallyLaunchActivityForResult
import com.kiylx.libx.activitymessenger.core.putExtras

/**
 * 比起ActivityMessenger.kt,这个是扩展方法版本 startActivity和startActivityResult的方法集合
 */
//<editor-fold desc="startActivity">
//====================================startActivity==================================================//
/**
 * TargetActivity放在泛型
 *
 * 示例：
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
inline fun <reified TARGET : Activity> Activity.launchActivity(
    vararg params: Pair<String, Any?>
) = launchActivity<TARGET>(Bundle().putExtras(*params), null)

/**
 * TargetActivity放在泛型
 *
 * 示例：
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
inline fun <reified TARGET : Activity> Fragment.launchActivity(
    vararg params: Pair<String, Any?>
) = requireActivity().launchActivity<TARGET>(*params)

/**
 * TargetActivity放在泛型
 *
 * 示例：
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
inline fun <reified TARGET : Activity> Context.launchActivity(
    vararg params: Pair<String, Any?>,
) {
    val intent = Intent(this, TARGET::class.java).putExtras(*params)
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

/**
 * 用法：
 *
 * ```
 *
 * val options = ActivityOptions.makeSceneTransitionAnimation(this)
 *
 * launchActivity<SomeActivity>(
 *     Bundle().putExtras("ee" to 1, "dd" to 2),
 *     options
 * )
 * ```
 *
 * @param bundle 传递的数据
 * @param options 可选参数，比如转场动画
 * @param TARGET 要启动的Activity
 */
inline fun <reified TARGET : Activity> Fragment.launchActivity(
    bundle: Bundle? = null,
    options: ActivityOptions? = null,
) = requireActivity().launchActivity<TARGET>(bundle, options)

/**
 * 用法：
 *
 * ```
 *
 * val options = ActivityOptions.makeSceneTransitionAnimation(this)
 *
 * launchActivity<SomeActivity>(
 *     Bundle().putExtras("ee" to 1, "dd" to 2),
 *     options
 * )
 * ```
 *
 * @param bundle 传递的数据
 * @param options 可选参数，比如转场动画
 * @param TARGET 要启动的Activity
 */
inline fun <reified TARGET : Activity> Activity.launchActivity(
    bundle: Bundle? = null,
    options: ActivityOptions? = null,
) {
    startActivity(
        Intent(this, TARGET::class.java).also {
            if (bundle != null) it.putExtras(bundle)
        },
        options?.toBundle()
    )
}

//</editor-fold>

//<editor-fold desc="startActivity -传class版本">
/**
 * TargetActivity放在泛型
 *
 * 示例：
 *
 * ```
 *      //不携带参数
 *      launchActivity(TestActivity::class.java,)
 *
 *      //携带参数（可连续多个键值对）
 *      launchActivity(TestActivity::class.java,"Key" to "Value")
 * ```
 *
 * @param params extras键值对
 * @param TARGET 要启动的Activity
 */
fun Activity.launchActivity(
    cls: Class<out Activity>,
    vararg params: Pair<String, Any?>
) = launchActivity(cls, Bundle().putExtras(*params), null)

/**
 * TargetActivity放在泛型
 *
 * 示例：
 *
 * ```
 *      //不携带参数
 *      launchActivity(TestActivity::class.java,)
 *
 *      //携带参数（可连续多个键值对）
 *      launchActivity(TestActivity::class.java,"Key" to "Value")
 * ```
 *
 * @param params extras键值对
 * @param TARGET 要启动的Activity
 */
fun Fragment.launchActivity(
    cls: Class<out Activity>,
    vararg params: Pair<String, Any?>
) = requireActivity().launchActivity(cls, *params)

/**
 * TargetActivity放在泛型
 *
 * 示例：
 *
 * ```
 *      //不携带参数
 *      launchActivity(TestActivity::class.java,)
 *
 *      //携带参数（可连续多个键值对）
 *      launchActivity(TestActivity::class.java,"Key" to "Value")
 * ```
 *
 * @param params extras键值对
 * @param TARGET 要启动的Activity
 */
fun Context.launchActivity(
    cls: Class<out Activity>,
    vararg params: Pair<String, Any?>,
) {
    val intent = Intent(this, cls).putExtras(*params)
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

/**
 * 用法：
 *
 * ```
 *
 * val options = ActivityOptions.makeSceneTransitionAnimation(this)
 *
 * launchActivity(
 *     SomeActivity::class.java,
 *     Bundle().putExtras("ee" to 1, "dd" to 2),
 *     options
 * )
 * ```
 *
 * @param cls 要启动的Activity
 * @param bundle 传递的数据
 * @param options 可选参数，比如转场动画
 */
fun Fragment.launchActivity(
    cls: Class<out Activity>,
    bundle: Bundle? = null,
    options: ActivityOptions? = null,
) = requireActivity().launchActivity(cls, bundle, options)

/**
 * 用法：
 *
 * ```
 *
 * val options = ActivityOptions.makeSceneTransitionAnimation(this)
 *
 * launchActivity(
 *     SomeActivity::class.java,
 *     Bundle().putExtras("ee" to 1, "dd" to 2),
 *     options
 * )
 * ```
 *
 * @param cls 要启动的Activity
 * @param bundle 传递的数据
 * @param options 可选参数，比如转场动画
 */
fun Activity.launchActivity(
    cls: Class<out Activity>,
    bundle: Bundle? = null,
    options: ActivityOptions? = null,
) {
    startActivity(
        Intent(this, cls).also {
            if (bundle != null) it.putExtras(bundle)
        },
        options?.toBundle()
    )
}
//</editor-fold>

//====================================startActivityForResult==================================================//

//<editor-fold desc="startActivityForResult">

//<editor-fold desc="javaclass版本-传泛型">

//<editor-fold desc="javaclass版本-带code">

//跟上面唯一区别是匿名函数参数多了一个ResultCode
/**
 * 作用同[FragmentActivity.launchActivityForResult] 示例：
 *
 * ```
 *      //不携带参数
 *      launchActivityForResult<TestActivity> {code, result->
 *          if (code == RESULT_OK) {
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
inline fun <reified TARGET : Activity> FragmentActivity.launchActivityForResult(
    vararg params: Pair<String, Any?>,
    noinline callback: ((code: Int, result: Intent?) -> Unit)
) = launchActivityForResult(
    Intent(this, TARGET::class.java).putExtras(*params),
    callback
)


inline fun <reified TARGET : Activity> Fragment.launchActivityForResult(
    vararg params: Pair<String, Any?>,
    noinline callback: ((code: Int, result: Intent?) -> Unit)
) = launchActivityForResult(
    Intent(this.requireActivity(), TARGET::class.java).putExtras(*params),
    callback
)
//</editor-fold>

//</editor-fold>

//<editor-fold desc="传clas，不使用泛型">
fun FragmentActivity.launchActivityForResult(
    cls: Class<out Activity>,
    vararg params: Pair<String, Any?>,
    callback: ((code: Int, result: Intent?) -> Unit)
) = launchActivityForResult(
    Intent(this, cls).putExtras(*params),
    callback
)


fun Fragment.launchActivityForResult(
    cls: Class<out Activity>,
    vararg params: Pair<String, Any?>,
    callback: ((code: Int, result: Intent?) -> Unit)
) = launchActivityForResult(
    Intent(this.requireActivity(), cls).putExtras(*params),
    callback
)
//</editor-fold>


//=============================下面的是直接传intent版本,上面的传入的是多个params

//<editor-fold desc="直接传intent版本">

fun FragmentActivity.launchActivityForResult(
    intent: Intent, callback: ((code: Int, result: Intent?) -> Unit)
) = finallyLaunchActivityForResult(this, intent, callback)

fun Fragment.launchActivityForResult(
    intent: Intent, callback: ((code: Int, result: Intent?) -> Unit)
) = finallyLaunchActivityForResult(this, intent, callback)

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
fun Activity.finish(vararg params: Pair<String, Any?>) = run {
    setResult(Activity.RESULT_OK, Intent().putExtras(*params))
    finish()
}

fun Activity.finish(intent: Intent) = run {
    setResult(Activity.RESULT_OK, intent)
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
