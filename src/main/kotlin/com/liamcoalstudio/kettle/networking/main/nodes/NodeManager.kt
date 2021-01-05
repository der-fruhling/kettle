package com.liamcoalstudio.kettle.networking.main.nodes

class NodeManager {
    private val startNodeRead: Node = StartNode()
    private val endNodeRead: Node = EndNode()

    private val startNodeWrite: Node = StartNode()
    private val endNodeWrite: Node = EndNode()

    init {
        startNodeRead.setOutput(endNodeRead)
        startNodeWrite.setOutput(endNodeWrite)
    }

    fun putRead(byteArray: ByteArray) = startNodeRead.passThrough(byteArray)
    fun putWrite(byteArray: ByteArray): ByteArray = startNodeWrite.passThrough(byteArray)

}