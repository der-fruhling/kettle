package com.liamcoalstudio.kettle.helpers

import com.liamcoalstudio.kettle.servers.java.JavaServer

object Block {
    val air get() = JavaServer.BLOCKS["minecraft:air"]!![0]
    val stone get() = JavaServer.BLOCKS["minecraft:stone"]!![0]
}