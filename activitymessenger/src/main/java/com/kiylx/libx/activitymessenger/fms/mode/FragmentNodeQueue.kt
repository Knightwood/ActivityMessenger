package com.kiylx.libx.activitymessenger.fms.mode

import androidx.fragment.app.Fragment
import com.kiylx.libx.activitymessenger.fms.mode.FragmentNode.FragmentNodeBuilder
import java.util.LinkedList
import java.util.Queue

/**
 * fragment action 队列。存储所有action。
 */
class FragmentNodeQueue {
    val stack: Queue<FragmentNode> = LinkedList()

    fun isEmpty(): Boolean {
        return stack.isEmpty()
    }

    /**
     * 作用同[putIn]，都是添加节点
     *
     * @param block
     * @param T
     */
    inline fun <reified T : Fragment> plus(block: FragmentNodeBuilder<T>.() -> Unit = {}) {
        val builder2 = FragmentNodeBuilder<T>(T::class.java)
        builder2.block()
        val node = builder2.build()
        stack.add(node)
    }

    fun putIn(wrapper: FragmentNode) {
        stack.add(wrapper)
    }

    /**
     * 从fragment栈中移除
     *
     * @param wrapper 要移除的fragment
     * @param clear 是否移除wrapper之后的所有fragment
     */
    fun getOut(wrapper: FragmentNode) {
        stack.remove(wrapper)
    }

    fun release() {
        stack.clear()
    }

    /**
     * 判断当前节点是否是tag代表的节点，如果当前节点存在平行节点，则判断这些平行节点是否包含此tag
     *
     * @return
     */
    private fun FragmentNode.compare(s: String): Boolean {
        return if (!isSingleNode()) {
            findNodeNext(s) != null || findNodePrev(s) != null
        } else {
            this.tag == s
        }
    }

    fun findInStack(tag: String): FragmentNode? {
        return stack.find {
            it.compare(tag)
        }
    }

    fun findTop(tag: String): FragmentNode? {
        val founded = stack.peek()
        return founded?.takeIf {
            it.compare(tag)
        }
    }
}