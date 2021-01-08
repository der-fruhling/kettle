package com.liamcoalstudio.kettle.helpers

import com.liamcoalstudio.kettle.servers.main.KettleServer

object Block {
    val air: Int get() = KettleServer.BLOCKS["minecraft:air"]!!
    val stone: Int get() = KettleServer.BLOCKS["minecraft:stone"]!!
}