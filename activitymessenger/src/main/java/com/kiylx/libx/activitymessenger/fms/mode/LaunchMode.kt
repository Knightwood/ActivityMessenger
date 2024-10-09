package com.kiylx.libx.activitymessenger.fms.mode

/**
 * 启动模式
 */
sealed class LaunchMode {
    /**
     * 标准模式，每次都生成一个新的fragment
     */
    data object Standard : LaunchMode()

    /**
     * 启动时，查询attachTo的controller中是否存在此fragment
     */
    data object SingleInTask : LaunchMode()

    /**
     * 启动时，查询attachTo的controller的栈顶是否存在此fragment
     */
    data object SingleTop : LaunchMode()
}