package com.liamcoalstudio.kettle.networking.main.nodes

import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KClass

abstract class Node {
    private var output: AtomicReference<Node>? = null

    abstract fun passWrite(input: ByteArray): ByteArray
    abstract fun passRead(input: ByteArray): ByteArray

    open fun passThroughWrite(input: ByteArray): ByteArray = output!!.get().passThroughWrite(passWrite(input))
    open fun passThroughRead(input: ByteArray): ByteArray = output!!.get().passThroughRead(passRead(input))
    open fun setOutput(node: Node) {
        output = AtomicReference(node)
    }

    open fun insertAfter(node: Node) {
        node.output = output
        output = AtomicReference(node)
    }

    fun isOutputOf(clazz: KClass<CompressionNode>) = clazz.isInstance(output)
}
