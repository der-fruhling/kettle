package com.liamcoalstudio.kettle.world

import com.liamcoalstudio.kettle.helpers.Block

class Chunk {
    val blocks: HashMap<Position, Int> = HashMap()

    init {
        for (x in 0L..15L) for (y in 0L..15L) for (z in 0L..15L)
            blocks[Position(x, y, z)] = Block.air
    }
}