package com.liamcoalstudio.kettle.networking.main.nodes

import java.util.concurrent.atomic.AtomicReference

abstract class Node {
    private var output: AtomicReference<Node>? = null

    abstract fun pass(input: ByteArray): ByteArray

    open fun passThrough(input: ByteArray): ByteArray = output!!.get().passThrough(pass(input))
    open fun setOutput(node: Node) { output = AtomicReference(node) }
    open fun insertAfter(node: Node) {
        node.output = output
        output = AtomicReference(node)
    }
}
