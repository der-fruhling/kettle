package com.liamcoalstudio.kettle.helpers

import com.liamcoalstudio.kettle.servers.main.KettleServer

object Block {
    val air: Int get() = KettleServer.BLOCKS["minecraft:air"]!!
    val stone: Int get() = KettleServer.BLOCKS["minecraft:stone"]!!
    val grass_block: Int get() = KettleServer.BLOCKS["minecraft:grass_block"]!!
    val dirt: Int get() = KettleServer.BLOCKS["minecraft:dirt"]!!
    val bedrock: Int get() = KettleServer.BLOCKS["minecraft:bedrock"]!!

}