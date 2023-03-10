@file:Suppress(
    "unused",
    "SpellCheckingInspection",
)

package com.kiylx.libx.activitymessenger.androidx

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.kiylx.libx.activitymessenger.core.finallyLaunchActivityForResult
import com.kiylx.libx.activitymessenger.core.finallyLaunchActivityForResultCode
import com.kiylx.libx.activitymessenger.core.putExtras
import com.kiylx.libx.activitymessenger.core.runIfNonNull
import kotlin.reflect.KClass

/**
 * 比起扩展方法需要fragment或者fragmentActivity接收者才可以调用 这里是非扩展方法版本。
 * startActivity和startActivityResult的方法集合
 */
object ActivityMessenger {
    /**
     * 启动任意activity
     * @param pkgName 应用的包名(manifest中的package)
     *
     * ```
     * 用法：
     *     在context环境下启动settingsActivity
     *
     *     mContext.startActivity<SettingsActivity>("pkgName")
     * ```
     *
     */
    inline fun <reified TARGET : Activity> Context.startActivity(pkgName: String) {
        val componentName =
            ComponentName(pkgName, TARGET::class.java.canonicalName)
        val intent = Intent()
        intent.component = componentName
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    /**
     * 作用同[FragmentActivity.startActivity] 示例：
     *
     * ```
     *      //不携带参数
     *      ActivityMessenger.launchActivity<TestActivity>(this)
     *
     *      //携带参数（可连续多个键值对）
     *      ActivityMessenger.launchActivity<TestActivity>(this, "Key" to "Value")
     * ```
     *
     * @param starter 发起的Activity
     * @param params extras键值对
     * @param TARGET 要启动的Activity
     */
    inline fun <reified TARGET : FragmentActivity> launchActivity(
        starter: FragmentActivity, vararg params: Pair<String, Any?>,
    ) {
        starter.startActivity(Intent(starter, TARGET::class.java).putExtras(*params))
    }

    /**
     * Fragment跳转，同[FragmentActivity.startActivity] 示例：
     *
     * ```
     *      //不携带参数
     *      ActivityMessenger.launchActivity<TestActivity>(this)
     *
     *      //携带参数（可连续多个键值对）
     *      ActivityMessenger.launchActivity<TestActivity>(this, "Key" to "Value")
     * ```
     *
     * @param starter 发起的Fragment
     * @param params extras键值对
     * @param TARGET 要启动的Activity
     */
    inline fun <reified TARGET : FragmentActivity> launchActivity(
        starter: Fragment, vararg params: Pair<String, Any?>,
    ) {
        starter.startActivity(Intent(starter.activity, TARGET::class.java).putExtras(*params))
    }

    /**
     * Adapter跳转，同[Context.startActivity] 示例：
     *
     * ```
     *      //不携带参数
     *      ActivityMessenger.launchActivity<TestActivity>(context)
     *
     *      //携带参数（可连续多个键值对）
     *      ActivityMessenger.launchActivity<TestActivity>(context, "Key" to "Value")
     * ```
     *
     * @param starter 发起的Fragment
     * @param params extras键值对
     * @param TARGET 要启动的Context
     */
    inline fun <reified TARGET : FragmentActivity> launchActivity(
        starter: Context, vararg params: Pair<String, Any?>,
    ) {
        starter.startActivity(Intent(starter, TARGET::class.java).putExtras(*params))
    }

    /**
     * 作用同[FragmentActivity.startActivity] 示例：
     *
     * ```
     *      //不携带参数
     *      ActivityMessenger.launchActivity(this, TestActivity::class)
     *
     *      //携带参数（可连续多个键值对）
     *     ActivityMessenger.launchActivity(
     *         this, TestActivity::class,
     *         "Key1" to "Value",
     *         "Key2" to 123
     *     )
     * ```
     *
     * @param starter 发起的Activity
     * @param target 要启动的Activity
     * @param params extras键值对
     */
    fun launchActivity(
        starter: FragmentActivity,
        target: KClass<out FragmentActivity>,
        vararg params: Pair<String, Any?>,
    ) {
        starter.startActivity(Intent(starter, target.java).putExtras(*params))
    }

    /** 作用同上，此方法为了兼容Java Class */
    fun launchActivity(
        starter: FragmentActivity,
        target: Class<out FragmentActivity>,
        vararg params: Pair<String, Any?>,
    ) {
        starter.startActivity(Intent(starter, target).putExtras(*params))
    }

    /**
     * Fragment跳转，同[FragmentActivity.startActivity] 示例：
     *
     * ```
     *      //不携带参数
     *      ActivityMessenger.launchActivity(this, TestActivity::class)
     *
     *      //携带参数（可连续多个键值对）
     *     ActivityMessenger.launchActivity(
     *         this, TestActivity::class,
     *         "Key1" to "Value",
     *         "Key2" to 123
     *     )
     * ```
     *
     * @param starter 发起的Fragment
     * @param target 要启动的Activity
     * @param params extras键值对
     */
    fun launchActivity(
        starter: Fragment, target: KClass<out FragmentActivity>, vararg params: Pair<String, Any?>,
    ) {
        starter.startActivity(Intent(starter.activity, target.java).putExtras(*params))
    }

    /** 作用同上，此方法为了兼容Java Class */
    fun launchActivity(
        starter: Fragment, target: Class<out FragmentActivity>, vararg params: Pair<String, Any?>,
    ) {
        starter.startActivity(Intent(starter.activity, target).putExtras(*params))
    }

    /**
     * Adapter里面跳转，同[Context.startActivity] 示例：
     *
     * ```
     *      //不携带参数
     *      ActivityMessenger.launchActivity(context, TestActivity::class)
     *
     *      //携带参数（可连续多个键值对）
     *     ActivityMessenger.launchActivity(
     *         context, TestActivity::class,
     *         "Key1" to "Value",
     *         "Key2" to 123
     *     )
     * ```
     *
     * @param starter 发起的Context
     * @param target 要启动的Activity
     * @param params extras键值对
     */
    fun launchActivity(
        starter: Context, target: KClass<out FragmentActivity>, vararg params: Pair<String, Any?>,
    ) {
        starter.startActivity(Intent(starter, target.java).putExtras(*params))
    }

    /** 作用同上，此方法为了兼容Java Class */
    fun launchActivity(
        starter: Context, target: Class<out FragmentActivity>, vararg params: Pair<String, Any?>,
    ) {
        starter.startActivity(Intent(starter, target).putExtras(*params))
    }


//====================================startActivityForResult=====================================//
    /**
     * 作用同[FragmentActivity.startActivityForResult] 示例：
     *
     * ```
     *      //不携带参数
     *      ActivityMessenger.launchActivityForResult<TestActivity> {
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
     * @param starter 发起的Activity
     * @param params extras键值对
     * @param callback onActivityResult的回调
     * @param TARGET 要启动的Activity
     */
    inline fun <reified TARGET : FragmentActivity> launchActivityForResult(
        starter: FragmentActivity, vararg params: Pair<String, Any?>,
        crossinline callback: ((result: Intent?) -> Unit),
    ) {
        launchActivityForResult(starter, TARGET::class, *params, callback = callback)
    }

    /**
     * Fragment跳转，同[FragmentActivity.startActivityForResult] 示例：
     *
     * ```
     *      //不携带参数
     *      ActivityMessenger.launchActivityForResult<TestActivity> {
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
     * @param starter 发起的Activity
     * @param params extras键值对
     * @param callback onActivityResult的回调
     * @param TARGET 要启动的Activity
     */
    inline fun <reified TARGET : FragmentActivity> launchActivityForResult(
        starter: Fragment, vararg params: Pair<String, Any?>,
        crossinline callback: ((result: Intent?) -> Unit),
    ) = launchActivityForResult(starter.activity, TARGET::class, *params, callback = callback)

    /**
     * 作用同[FragmentActivity.startActivityForResult] 示例：
     *
     * ```
     *      //不携带参数
     *      ActivityMessenger.launchActivityForResult<TestActivity> {resultCode, result->
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
     * @param starter 发起的Activity
     * @param params extras键值对
     * @param callback onActivityResult的回调
     * @param TARGET 要启动的Activity
     */
    inline fun <reified TARGET : FragmentActivity> launchActivityForResultCode(
        starter: FragmentActivity, vararg params: Pair<String, Any?>,
        crossinline callback: ((resultCode: Int, result: Intent?) -> Unit),
    ) = launchActivityForResultCode(starter, TARGET::class, *params, callback = callback)

    /**
     * Fragment跳转，同[FragmentActivity.startActivityForResult] 示例：
     *
     * ```
     *      //不携带参数
     *      ActivityMessenger.launchActivityForResult<TestActivity> {resultCode, result->
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
     * @param starter 发起的Activity
     * @param params extras键值对
     * @param callback onActivityResult的回调
     * @param TARGET 要启动的Activity
     */
    inline fun <reified TARGET : FragmentActivity> launchActivityForResultCode(
        starter: Fragment, vararg params: Pair<String, Any?>,
        crossinline callback: ((resultCode: Int, result: Intent?) -> Unit),
    ) = launchActivityForResultCode(starter.activity, TARGET::class, *params, callback = callback)

    /**
     * 作用同[FragmentActivity.startActivityForResult] 示例：
     *
     * ```
     *      //不携带参数
     *      ActivityMessenger.launchActivityForResult(this, TestActivity::class) {
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
     * @param starter 发起的Activity
     * @param target 要启动的Activity
     * @param params extras键值对
     * @param callback onActivityResult的回调
     */
    inline fun launchActivityForResult(
        starter: FragmentActivity?, target: KClass<out FragmentActivity>,
        vararg params: Pair<String, Any?>, crossinline callback: ((result: Intent?) -> Unit),
    ) = starter.runIfNonNull {
        finallyLaunchActivityForResult(it, Intent(it, target.java).putExtras(*params), callback)
    }

    /** 作用同上，此方法为了兼容Java Class */
    inline fun launchActivityForResult(
        starter: FragmentActivity?, target: Class<out FragmentActivity>,
        vararg params: Pair<String, Any?>, crossinline callback: ((result: Intent?) -> Unit),
    ) = starter.runIfNonNull {
        finallyLaunchActivityForResult(it, Intent(it, target).putExtras(*params), callback)
    }

    /**
     * 作用同[FragmentActivity.startActivityForResult] 示例：
     *
     * ```
     *      //不携带参数
     *      ActivityMessenger.launchActivityForResult(this, TestActivity::class) {resultCode, result->
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
     * @param starter 发起的Activity
     * @param target 要启动的Activity
     * @param params extras键值对
     * @param callback onActivityResult的回调
     */
    inline fun launchActivityForResultCode(
        starter: FragmentActivity?,
        target: KClass<out FragmentActivity>,
        vararg params: Pair<String, Any?>,
        crossinline callback: ((resultCode: Int, result: Intent?) -> Unit),
    ) {
        starter.runIfNonNull {
            finallyLaunchActivityForResultCode(
                it,
                Intent(it, target.java).putExtras(*params),
                callback
            )
        }
    }

    /** 作用同上，此方法为了兼容Java Class */
    inline fun launchActivityForResultCode(
        starter: FragmentActivity?,
        target: Class<out FragmentActivity>,
        vararg params: Pair<String, Any?>,
        crossinline callback: ((resultCode: Int, result: Intent?) -> Unit),
    ) {
        starter.runIfNonNull {
            finallyLaunchActivityForResultCode(it, Intent(it, target).putExtras(*params), callback)
        }
    }


    /**
     * 作用同[FragmentActivity.finish] 示例：
     *
     * ```
     *      ActivityMessenger.finish(this, "Key" to "Value")
     * ```
     *
     * @param src 发起的Activity
     * @param params extras键值对
     */
    fun finish(src: FragmentActivity, vararg params: Pair<String, Any?>) = src.run {
        setResult(FragmentActivity.RESULT_OK, Intent().putExtras(*params))
        finish()
    }

    /**
     * Fragment调用，作用同[FragmentActivity.finish] 示例：
     *
     * ```
     *      ActivityMessenger.finish(this, "Key" to "Value")
     * ```
     *
     * @param src 发起的Fragment
     * @param params extras键值对
     */
    fun finish(src: Fragment, vararg params: Pair<String, Any?>) =
        src.activity?.run { finish(this, *params) }
}