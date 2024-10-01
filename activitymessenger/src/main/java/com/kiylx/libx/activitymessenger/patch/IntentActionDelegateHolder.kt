package com.kiylx.libx.activitymessenger.patch

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import java.util.UUID

/**
 * IntentAction Delegate Holder
 *
 * author: knightwood
 *
 * in any context, you can call this method to invoke startActivityForResult
 */
object IntentActionDelegateHolder {
    const val REQUEST_KEY = "RequestFinish"
    const val RESULT_KEY = "Result"

    internal val holder: MutableMap<UUID, (activity: FragmentActivity) -> Unit> = HashMap()

    /**
     * startActivityForResult in any context.
     *
     * kotlin example:
     * ```
     * PermissionDelegateHolder.delegate(this) { activity ->
     * }
     *
     * ```
     *
     * java example:
     * ```
     * PermissionDelegateHolder.delegate(this, activity -> {
     *     return null;
     * });
     * ```
     * @param ctx the context which can launch a new activity
     * @param block the request permissions block to be executed
     */
    @JvmStatic
    fun delegate(ctx: Context, block: (activity: FragmentActivity) -> Unit) {
        val uuid = UUID.randomUUID()
        holder[uuid] = block
        val intent = Intent(ctx, DelegateActivity::class.java)
        intent.putExtra("uuid", uuid)
        ctx.startActivity(intent)
    }
}