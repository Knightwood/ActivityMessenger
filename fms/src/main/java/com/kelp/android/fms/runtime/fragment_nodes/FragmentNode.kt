package com.kelp.android.fms.runtime.fragment_nodes

import androidx.fragment.app.Fragment
import com.kelp.android.fms.fragment_nodes.LinkedNode


/**
 * 代表某个fragment
 *
 * @property tag 添加到FragmentManager时的tag，默认为clazz.name
 * @property clazz 要启动的fragment
 */
@Deprecated("废弃")
open class FragmentNode(
    val clazz: Class<out Fragment>,
) : LinkedNode(clazz.name) {
    /**
     * 增加一个平级节点
     *
     * @param block
     * @param T
     */
    inline fun <reified T : Fragment> plus(block: FragmentNodeBuilder<T>.() -> Unit = {}) {
        val builder2 = FragmentNodeBuilder<T>(T::class.java)
        builder2.block()
        val node = builder2.build()
        addNode(node)
    }

    /**
     * fragment生成器，默认为无参构造函数
     */
    var newInstanceFactory: () -> Fragment = {
        clazz.getDeclaredConstructor().newInstance()
    }

    companion object {
    }

    /**
     * 使用builder模式构建节点，同时，还可以检查[newInstanceFactory]的返回值
     *
     * @param T
     * @property clazz
     */
    class FragmentNodeBuilder<T : Fragment>(var clazz: Class<T>) {
        /**
         * fragment生成器，默认为无参构造函数
         */
        var newInstanceFactory: (() -> T)? = null

        fun build(): FragmentNode {
            return FragmentNode(clazz).also { node ->
                this.newInstanceFactory?.let { node.newInstanceFactory = it }
            }
        }
    }
}

class RootFragment : Fragment() {}