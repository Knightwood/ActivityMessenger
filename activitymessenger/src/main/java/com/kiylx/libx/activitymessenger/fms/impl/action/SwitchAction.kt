package com.kiylx.libx.activitymessenger.fms.impl.action

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.kiylx.libx.activitymessenger.fms.impl.SealFragmentController
import com.kiylx.libx.activitymessenger.fms.mode.FragmentNode

class SwitchAction(
    controller: SealFragmentController,
    node: FragmentNode,
) : BaseAction(controller, node) {

    override fun run() {
        val fm = controller.fm

        val f: Fragment? = controller.getFragmentInstance(
            fm,
            navOptions.launchMode,
            navOptions.tag,
            navOptions.newInstanceFactory
        )
        //切换显示，如果在不同的fm中如何处理呢？
        //如果是同一个fm，但在不同的容器中如何处理呢？
        if (f != null) {
            fm.commit(navOptions.allowLostState) {
                hideAll()
            }
        } else {
            Log.e(TAG, "run: fragment is null")
        }
    }

    companion object {
        const val TAG = "SwitchAction"
    }
}