package com.kelp.android.fms.runtime.action

import android.text.TextUtils.replace
import androidx.fragment.app.commit
import com.kelp.android.fms.runtime.core.NavControllerImpl
import com.kelp.android.fms.runtime.fragment_nodes.FragmentNode
import com.kelp.android.fms.runtime.nav_options.NavOptions

class AddAction(
    controller: NavControllerImpl,
    navOptions: NavOptions,
    node: FragmentNode,
) : NavAction(controller, navOptions, node) {

    override fun run() {
        /*if (navOptions.flatten) {
            val last = queue.findTop()
            if (last != null)
                last.addNode(node)
            else
                queue.putIn(node)
        }*/

        fm.commit(navOptions.allowLostState) {
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
                replace(containerView.id, node.clazz, navOptions.bundle, navOptions.tag)
            } else {
                //todo 这里可以增加一个开关来判断是否在显示新的fragment时，隐藏之前的fragment。
                hideAll()
                add(containerView.id, node.clazz, navOptions.bundle, navOptions.tag)
            }
        }
    }
}