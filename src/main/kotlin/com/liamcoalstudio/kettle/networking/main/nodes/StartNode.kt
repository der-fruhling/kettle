package com.liamcoalstudio.kettle.networking.main.nodes

class StartNode : Node() {
    override fun pass(input: ByteArray) = input
}