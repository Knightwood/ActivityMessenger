package com.kelp.android.fms.runtime.fragment_nodes

import java.util.LinkedList

/**
 * 节点的抽象，所有节点的父类。 节点处理
 *
 * @property tag 节点的tag，用于在节点间查找。
 */
abstract class LinkedNode(
    var tag: String,
) {
    /**
     * 标注此fragment是和其他fragment平级的，而不是栈结构。
     * 比如底部的导航栏对应的fragment，不是栈结构，而是平级的。而进入设置页面或其他业务页面，则是栈结构。
     */
    var prev: LinkedNode? = null
        internal set
    var next: LinkedNode? = null
        internal set


    /**
     * 添加多个平级的节点
     *
     * @param nodes
     * @return
     */
    fun addNode(vararg nodes: LinkedNode) {
        //找到最后一个平级节点，并添加
        //筛选掉重复节点
        val fixedNodes = filterNodes(nodes.toList())
        lastNode().flattenInner(fixedNodes.toTypedArray())
    }

    /**
     * 将自己和others对比，并返回没有重复的节点。
     *
     * @param others 要筛除已存在节点的集合
     * @return 剩余的未重复的节点
     */
    fun filterNodes(others: Collection<LinkedNode>): List<LinkedNode> {
        val result = LinkedList<LinkedNode>()
        val own = toLinkedList()
        others.forEach {
            if (!own.contains(it)) {
                result.add(it)
            }
        }
        return result
    }

    /**
     * 合并两批节点，如果有重复节点，则筛除，
     *
     * @param older
     * @param newer
     * @return
     */
    fun mergeNodes(older: Collection<LinkedNode>, newer: Collection<LinkedNode>): List<LinkedNode> {
        val result = LinkedList<LinkedNode>()
        result.addAll(older)
        newer.forEach {
            if (!result.contains(it)) {
                result.add(it)
            }
        }
        return result
    }

    /**
     * 判断当前节点是否是单节点
     */
    fun isSingleNode() = next == null && prev == null

    /**
     * 向后查找节点
     */
    fun findNodeNext(tag: String): LinkedNode? {
        return if (tag == this.tag) this else next?.findNodeNext(tag)
    }

    /**
     * 向前查找节点
     */
    fun findNodePrev(tag: String): LinkedNode? {
        return if (tag == this.tag) this else prev?.findNodePrev(tag)
    }

    /**
     * 查找最后一个平行节点
     */
    fun lastNode(): LinkedNode {
        return next?.lastNode() ?: this
    }

    /**
     * 查找第一个平行节点
     */
    fun firstNode(): LinkedNode {
        return prev?.firstNode() ?: this
    }

    fun remove(node: LinkedNode) {
        val prev = node.prev
        val next = node.next
        if (prev != null) {
            prev.next = next
        }
        if (next != null) {
            next.prev = prev
        }
    }

    /**
     * 将所有节点组织成一个队列
     */
    fun toLinkedList(): LinkedList<LinkedNode> {
        val result: LinkedList<LinkedNode> = LinkedList()
        //向前遍历
        var current: LinkedNode? = this
        while (current != null) {
            result.addFirst(current)
            current = current.prev
        }
        //向后遍历
        current = this.next //向前遍历时已经添加过了，因此此处跳过当前节点
        while (current != null) {
            result.addLast(current)
            current = current.next
        }
        return result
    }

    /**
     * 添加多个平级节点，请注意此方法不会筛出重复节点
     */
    private fun flattenInner(fixedNodes: Array<out LinkedNode>) {
        //添加平级节点
        fixedNodes.forEachIndexed { index, node ->
            if (index == 0) {
                //设置当前节点
                next = node
                //设置第一个节点
                if (fixedNodes.size > 1) {
                    node.next = fixedNodes[1]
                    node.prev = this
                }
            } else if (index == fixedNodes.lastIndex) {
                //最后一个节点
                node.prev = fixedNodes[index - 1]
                node.next = null
            } else {
                //中间节点
                node.prev = fixedNodes[index - 1]
                node.next = fixedNodes[index + 1]
            }
        }
        //检查头尾节点，避免形成环形链路
        if (prev == fixedNodes.last()) {
            prev = null
        }
    }
}