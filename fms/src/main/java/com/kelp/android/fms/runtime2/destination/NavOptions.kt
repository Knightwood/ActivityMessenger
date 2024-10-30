package com.kelp.android.fms.runtime2.destination

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import java.io.Serializable


/**
 * 描述一个启动时，需要的参数
 */
data class NavOptions(
    var bundle: Bundle? = null,
    var root: Boolean = false,
    var flatten: Boolean = false,
    var addToBackStack: Boolean = true,
    var allowLostState: Boolean = false,
    var dropSelf: Boolean = false,
    var clearHistory: Boolean = false,
    var deeplink: String? = null,
) {

    /**
     * 可以修改默认的fragment容器，默认为null，使用controller中的容器。
     */
    var containerView: FragmentContainerView? = null

    /**
     * 设置参数，可以是任意类型，只要Bundle支持的即可。
     *
     * 如果你重写了[newInstanceFactory]，且调用此方法，
     * 那么此方法设置的参数将覆盖你在[newInstanceFactory]调用[Fragment.setArguments]设置的参数。
     *
     * 调用示例：
     *
     * ```
     * params("param1" to 1, "param2" to "2")
     *
     * ```
     *
     * @param params
     */
    fun params(
        vararg params: Pair<String, Any?>,
    ) {
        bundle = Bundle().putExtras(*params)
    }
}

fun <T> Bundle.putExtras(vararg params: Pair<String, T>): Bundle {
    if (params.isEmpty()) return this
    params.forEach { (key, value) ->
        when (value) {
            is Int -> putInt(key, value)
            is Byte -> putByte(key, value)
            is Char -> putChar(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Short -> putShort(key, value)
            is Double -> putDouble(key, value)
            is Boolean -> putBoolean(key, value)
            is Bundle -> putBundle(key, value)
            is String -> putString(key, value)
            is IntArray -> putIntArray(key, value)
            is ByteArray -> putByteArray(key, value)
            is CharArray -> putCharArray(key, value)
            is LongArray -> putLongArray(key, value)
            is FloatArray -> putFloatArray(key, value)
            is Parcelable -> putParcelable(key, value)
            is ShortArray -> putShortArray(key, value)
            is DoubleArray -> putDoubleArray(key, value)
            is BooleanArray -> putBooleanArray(key, value)
            is CharSequence -> putCharSequence(key, value)
            is Array<*> -> {
                when {
                    value.isArrayOf<String>() ->
                        putStringArray(key, value as Array<String?>)

                    value.isArrayOf<Parcelable>() ->
                        putParcelableArray(key, value as Array<Parcelable?>)

                    value.isArrayOf<CharSequence>() ->
                        putCharSequenceArray(key, value as Array<CharSequence?>)

                    else -> throw IllegalArgumentException("放不进去，怎么想都放不进去吧")
                }
            }

            is Serializable -> putSerializable(key, value)
        }
    }
    return this
}
