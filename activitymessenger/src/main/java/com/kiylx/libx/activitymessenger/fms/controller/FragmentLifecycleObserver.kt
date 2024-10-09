package com.kiylx.libx.activitymessenger.fms.controller

import android.os.Bundle
import android.system.Os.remove
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * 在fragment销毁时，清理FragmentNode和相关数据
 *
 * @property controller
 *
 * 每个controller都与一个FragmentManager关联，那么，就可以监听此FragmentManager中的fragment的生命周期。
 */
class FragmentLifecycleObserver(val controller: AFragmentController) :
    FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentDestroyed(fm, f)
        // 移除fragments
        controller.fragments.remove(f.tag)
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentCreated(fm, f, savedInstanceState)
        controller.fragments[f.tag!!] = f
    }
}