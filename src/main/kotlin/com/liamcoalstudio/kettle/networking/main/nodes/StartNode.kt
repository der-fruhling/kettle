package com.liamcoalstudio.kettle.networking.main.nodes

class StartNode : Node() {
    override fun passWrite(input: ByteArray) = input
    override fun passRead(input: ByteArray) = input
}