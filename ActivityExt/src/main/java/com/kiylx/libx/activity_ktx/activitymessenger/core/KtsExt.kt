package com.kiylx.libx.activity_ktx.activitymessenger.core


/** 可空对象转非空对象 */
inline fun <O> O?.runIfNonNull(block: (O) -> Unit) {
    this?.let { block(it) }
}


/** 不报错执行 */
inline fun <T, R> T.runSafely(block: (orange: T) -> R) = try {
    block(this)
} catch (e: Exception) {
    e.printStackTrace()
    null
}