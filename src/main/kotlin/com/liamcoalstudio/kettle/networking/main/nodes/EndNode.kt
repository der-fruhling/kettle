package com.liamcoalstudio.kettle.networking.main.nodes

class EndNode : Node() {
    override fun pass(input: ByteArray): ByteArray = input
    override fun passThrough(input: ByteArray): ByteArray = input
}