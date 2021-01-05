package com.liamcoalstudio.kettle.world

import kotlin.random.Random

class World {
    val chunks = HashMap<Position, Chunk>().toMutableMap()
    val entities = MutableList(0) { Entity(-1) }

    operator fun set(pos: Position, block: Int) {
        if(chunks[pos / 16] == null)
            chunks[pos / 16] = Chunk()
        val blockPos = pos % 16
        chunks[pos / 16]!!.blocks[blockPos] = block
    }

    fun isChunkPosValid(pos: Position) = pos.inRange(LongRange(-32, 32), LongRange(0, 8), LongRange(-32, 32))
    fun newEntityId() = Random.nextInt()
}