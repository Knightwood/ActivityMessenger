package com.kiylx.libx.activitymessenger.fms.controller

import androidx.fragment.app.Fragment

/**
 * 管理所有的fragment控制器
 *
 * fragment控制器会形成一棵树形结构
 */
object FragmentControllerManager {
    private val controllers: MutableMap<String, AFragmentController> = mutableMapOf()

    operator fun plus(sealFragmentController: AFragmentController) {
        controllers[sealFragmentController.tag] = sealFragmentController
    }

    operator fun minus(sealFragmentController: AFragmentController) {
        controllers.remove(sealFragmentController.tag)
    }

    fun get(tag: String): AFragmentController? {
        return controllers[tag]
    }

}