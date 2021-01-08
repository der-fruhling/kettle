package com.liamcoalstudio.kettle.world

import com.liamcoalstudio.kettle.actions.Action
import com.liamcoalstudio.kettle.helpers.Block
import com.liamcoalstudio.kettle.helpers.Dimension
import com.liamcoalstudio.kettle.networking.java.play.S2CChunkDataPacket
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.servers.main.KettleServer
import kotlin.math.floor

class Player(val client: Client?, eid: Int) : Entity(eid) {
    constructor() : this(null, -1)

    var renderDistance: Int = 2
    var x: Double = 0.0
    var y: Double = 64.0
    var z: Double = 0.0
    var world: World = KettleServer.GLOBAL!!.get().worlds[Dimension.OVERWORLD]!!
    var yaw: Float = 0.0f
    var pitch: Float = 0.0f

    var locale = "en_GB"
    var chatMode: Byte = 2
    var chatColors = true
    var displayedSkinParts = 0b01111111
    var mainHand = 1

    val actions = HashMap<Int, Action>()
    val loadedChunks = mutableListOf<Position>()

    @ExperimentalStdlibApi
    fun updateChunks() =
        Thread {
            val cx = floor(x / 16.0).toInt()
            val cz = floor(z / 16.0).toInt()
            val notInList = MutableList(loadedChunks.size) { loadedChunks[it] }
            for(x1 in (cx-(renderDistance / 2 + 2))..(cx+(renderDistance / 2 + 2)))
                for(z1 in (cz-(renderDistance / 2 + 2))..(cz+(renderDistance / 2 + 2))) {
                    if (!loadedChunks.contains(Position(x1.toLong(), 0, z1.toLong())) &&
                        world.isChunkPosValid(Position(x1.toLong(), 0, z1.toLong()))) {
                        client!!.send(S2CChunkDataPacket(this, world, x1.toLong(), z1.toLong()))
                        loadedChunks.add(Position(x1.toLong(), 0, z1.toLong()))
                    }
                    notInList.remove(Position(x1.toLong(), 0, z1.toLong()))
                }
            loadedChunks.removeAll(notInList)
        }

    @ExperimentalStdlibApi
    private fun chunkIsAir(world: World, x: Int, z: Int): Boolean {
        val list = mutableListOf<Chunk>()
        for(i in 0L..15L)
            list.add(world[Position(x.toLong(), i, z.toLong())])
        return list.all { chunk -> chunk.blocks.all { it.value == Block.air } }
    }
}
