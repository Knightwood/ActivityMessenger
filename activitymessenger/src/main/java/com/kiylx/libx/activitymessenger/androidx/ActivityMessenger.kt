@file:Suppress(
    "unused",
    "SpellCheckingInspection",
)

package com.kiylx.libx.activitymessenger.androidx

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.kiylx.libx.activitymessenger.core.finallyLaunchActivityForResult
import com.kiylx.libx.activitymessenger.core.finallyLaunchActivityForResultCode
import com.kiylx.libx.activitymessenger.core.putExtras
import com.kiylx.libx.activitymessenger.core.runIfNonNull

/**
 * 比起扩展方法需要fragment或者fragmentActivity接收者才可以调用 这里是非扩展方法版本。
 * startActivity和startActivityResult的方法集合
 */
object ActivityMessenger {

//<editor-fold desc="startActivity">
    /**
     * 作用同[Activity.startActivity] 示例：
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
    inline fun <reified TARGET : Activity> launchActivity(
        starter: Activity, vararg params: Pair<String, Any?>,
    ) {
        starter.startActivity(Intent(starter, TARGET::class.java).putExtras(*params))
    }

    /**
     * Fragment跳转，同[Activity.startActivity] 示例：
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
    inline fun <reified TARGET : Activity> launchActivity(
        starter: Fragment, vararg params: Pair<String, Any?>,
    ) {
        starter.startActivity(Intent(starter.activity, TARGET::class.java).putExtras(*params))
    }

    /**
     * Activity跳转，同[Context.startActivity] 示例：
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
    inline fun <reified TARGET : Activity> launchActivity(
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
        starter: Activity,
        target: Class<out Activity>,
        vararg params: Pair<String, Any?>,
    ) {
        starter.startActivity(Intent(starter, target).putExtras(*params))
    }

    /**
     * Fragment跳转，同[Activity.startActivity] 示例：
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
        starter: Fragment, target: Class<out Activity>, vararg params: Pair<String, Any?>,
    ) {
        starter.startActivity(Intent(starter.activity, target).putExtras(*params))
    }

    /**
     * FragmentActivity里面跳转，同[Context.startActivity] 示例：
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
        starter: Context, target: Class<out Activity>, vararg params: Pair<String, Any?>,
    ) {
        starter.startActivity(Intent(starter, target).putExtras(*params))
    }

    //</editor-fold>

//====================================startActivityForResult=====================================//

//<editor-fold desc="startActivityForResult">
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
    inline fun <reified TARGET : Activity> launchActivityForResult(
        starter: FragmentActivity, vararg params: Pair<String, Any?>,
        crossinline callback: ((result: Intent?) -> Unit),
    ) {
        launchActivityForResult(starter, TARGET::class.java, *params, callback = callback)
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
    inline fun <reified TARGET : Activity> launchActivityForResult(
        starter: Fragment, vararg params: Pair<String, Any?>,
        crossinline callback: ((result: Intent?) -> Unit),
    ) = launchActivityForResult(
        starter, TARGET::class.java, *params,
        callback = callback
    )

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
    inline fun <reified TARGET : Activity> launchActivityForResultCode(
        starter: FragmentActivity, vararg params: Pair<String, Any?>,
        crossinline callback: ((resultCode: Int, result: Intent?) -> Unit),
    ) = launchActivityForResultCode(starter, TARGET::class.java, *params, callback = callback)

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
    inline fun <reified TARGET : Activity> launchActivityForResultCode(
        starter: Fragment, vararg params: Pair<String, Any?>,
        crossinline callback: ((resultCode: Int, result: Intent?) -> Unit),
    ) = launchActivityForResultCode(
        starter,
        TARGET::class.java,
        *params,
        callback = callback
    )

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
        starter: FragmentActivity, target: Class<out Activity>,
        vararg params: Pair<String, Any?>, crossinline callback: ((result: Intent?) -> Unit),
    ) = starter.let {
        finallyLaunchActivityForResult(it, Intent(it, target).putExtras(*params), callback)
    }

    inline fun launchActivityForResult(
        starter: Fragment,
        target: Class<out Activity>,
        vararg params: Pair<String, Any?>,
        crossinline callback: ((result: Intent?) -> Unit),
    ) {
        finallyLaunchActivityForResult(
            starter,
            Intent(starter.activity, target).putExtras(*params),
            callback=callback
        )
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
        starter: FragmentActivity,
        target: Class<out Activity>,
        vararg params: Pair<String, Any?>,
        crossinline callback: ((resultCode: Int, result: Intent?) -> Unit),
    ) {
        finallyLaunchActivityForResultCode(starter, Intent(starter, target).putExtras(*params), callback)
    }

    inline fun launchActivityForResultCode(
        starter: Fragment,
        target: Class<out Activity>,
        vararg params: Pair<String, Any?>,
        crossinline callback: ((resultCode: Int, result: Intent?) -> Unit),
    ) {
        finallyLaunchActivityForResultCode(
            starter,
            Intent(starter.activity, target).putExtras(*params),
            callback=callback
        )
    }
    //</editor-fold>

//<editor-fold desc="finish">

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
    fun finish(src: Activity, vararg params: Pair<String, Any?>) = src.run {
        setResult(Activity.RESULT_OK, Intent().putExtras(*params))
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
    //</editor-fold>

}