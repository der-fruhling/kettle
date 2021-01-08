package com.liamcoalstudio.kettle.world

import com.liamcoalstudio.kettle.helpers.Block
import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.helpers.ChunkPos
import com.liamcoalstudio.kettle.logging.ConsoleLogger
import kotlin.math.floor

@ExperimentalStdlibApi
class Chunk(pos: Position) {
    fun write(buf: Buffer) {
        buf.addShort(blocks.count { it.value != 0 }.toShort())
        val count = 4

        buf.addByte(count.toByte())

        val used = blocks.values.distinct()
        buf.addVarInt(used.size)
        for(u in used) buf.addVarInt(u)

        var o = 0L
        var ci = 0
        var c = 0
        val bbuf = Buffer()
        xzy().forEach { v ->
            if(ci % floor(64.0 / count).toInt() == 0 && ci > 0) {
                bbuf.addLong(o)
                o = 0
                c++
            }
            o = o or used.indexOf(v).toLong()
            o = o.rotateLeft(count)
            ci++
        }
        bbuf.addLong(o)
        buf.addVarInt(c+1)
        buf.addBuffer(bbuf)
    }

    private fun xzy(): MutableList<Int> {
        val xzy = mutableListOf<Int>()
        for(y in 0..15) for(z in 0..15) for(x in 0..15)
            xzy.add(blocks[ChunkPos(x.toByte(), y.toByte(), z.toByte()).short]!!)
        return xzy
    }

    val blocks: HashMap<Short, Int> = HashMap()

    init {
        val layers = arrayOf(Block.bedrock, Block.dirt, Block.dirt, Block.grass_block)
        for (x in 0..15) for (y in 0..15) for (z in 0..15) {
            if(y < 4 && pos.y == 0L)
                blocks[ChunkPos(x.toByte(), y.toByte(), z.toByte()).short] = layers[y]
            else
                blocks[ChunkPos(x.toByte(), y.toByte(), z.toByte()).short] = Block.air
        }
    }

    constructor(world: World, pos: Position) : this(pos) {
        val heightMap = world.heightMap(pos.x, pos.y, pos.z)
        for (x in 0..15) for (y in 0..15) for (z in 0..15) {
            if(heightMap[x][y][z] > 8.0)
                blocks[ChunkPos(x.toByte(), y.toByte(), z.toByte()).short] = Block.stone
            else
                blocks[ChunkPos(x.toByte(), y.toByte(), z.toByte()).short] = Block.air
        }
    }
}