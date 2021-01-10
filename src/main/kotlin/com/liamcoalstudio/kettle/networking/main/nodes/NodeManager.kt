package com.liamcoalstudio.kettle.networking.main.nodes

class NodeManager {
    private val startNode: Node = StartNode()
    private val compressionNode: Node = CompressionNode()
    private val endNode: Node = EndNode()

    init {
        startNode.setOutput(endNode)
    }

    fun enableCompression(i: Int) {
        (compressionNode as CompressionNode).threshold = i
        startNode.insertAfter(compressionNode)
    }
    fun disableCompression() = startNode.setOutput(endNode)
    fun hasCompression() = startNode.isOutputOf(CompressionNode::class)

    fun putRead(byteArray: ByteArray) = startNode.passThroughRead(byteArray)
    fun putWrite(byteArray: ByteArray) = startNode.passThroughWrite(byteArray)
}