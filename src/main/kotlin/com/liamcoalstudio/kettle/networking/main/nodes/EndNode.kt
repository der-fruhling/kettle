package com.liamcoalstudio.kettle.networking.main.nodes

class EndNode : Node() {
    override fun passWrite(input: ByteArray) = input
    override fun passRead(input: ByteArray) = input
    override fun passThroughRead(input: ByteArray) = input
    override fun passThroughWrite(input: ByteArray) = input
}