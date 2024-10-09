package com.kiylx.libx.activitymessenger.fms.impl.action

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.kiylx.libx.activitymessenger.fms.impl.SealFragmentController
import com.kiylx.libx.activitymessenger.fms.mode.NavOptions


/**
 * 向[FragmentManager]中添加一个fragment
 *
 * @property controller
 * @property navOptions
 */
open class AddAction(
    controller: SealFragmentController,
    navOptions: NavOptions,
) : BaseAction(controller, navOptions) {

    override fun run() {
        val fm = controller.fm

        val f: Fragment? = controller.getFragmentInstance(
            fm,
            navOptions.launchMode,
            navOptions.tag,
            navOptions.newInstanceFactory
        )
        if (f != null) {
            if (navOptions.bundle != null) {
                f.setArguments(navOptions.bundle)
            }
            fm.commit(navOptions.allowLostState) {
                // 如果是在fragment A 中生成的controller，且使用controller第一次添加子fragment，则设置该fragment A 为primaryNavigationFragment
                if (controller.fm.fragments.isEmpty() && !controller.inActivity) {
                    setPrimaryNavigationFragment(f)
                }
                //首先我们得知道，node要添加进的容器中最上层显示的fragment
                if (navOptions.dropSelf) {

                }
                if (navOptions.clearHistory) {

                }
                if (!navOptions.root) {
                    setReorderingAllowed(true)
                    addToBackStack(navOptions.tag)
                }
                if (navOptions.isUseReplace) {
                    //与隐藏同理，也需要按照不同容器来区分replace，
                    //如果不能区分，那就不能用replace方式。
                    //从源码来看，似乎replace是区分了不同的容器的，所以可以认为，这里可以不用判断。
                    replace(navOptions.containerView.id, f, navOptions.tag)
                } else {
                    //todo 这里可以增加一个开关来判断是否在显示新的fragment时，隐藏之前的fragment。
                    hideAll()
                    add(navOptions.containerView.id, f, navOptions.tag)
                }
            }
        } else {
            Log.e(TAG, "run: fragment is null")
        }
    }

    companion object {
        const val TAG = "AddAction"
    }
}