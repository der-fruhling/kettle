package com.liamcoalstudio.kettle.world

import FastNoiseLite
import com.liamcoalstudio.kettle.helpers.Dimension
import com.liamcoalstudio.kettle.logging.ConsoleLogger
import com.liamcoalstudio.kettle.tasks.Tasks
import java.time.Instant
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.collections.HashMap
import kotlin.random.Random

class World(val dimension: Dimension, val noise: FastNoiseLite?) {
    @ExperimentalStdlibApi
    val chunks = HashMap<Position, Chunk>().toMutableMap()
    val entities = MutableList<Entity?>(0) { null }
    val players = MutableList(0) { Player() }
    val idsInUse = MutableList(0) { 0 }
    var seed = Random.nextLong()
    val logger = ConsoleLogger(World::class)


    @ExperimentalStdlibApi
    operator fun set(pos: Position, block: Int) {
        if(chunks[pos / 16] == null)
            chunks[pos / 16] = Chunk(this, pos / 16)
        val blockPos = pos % 16
        chunks[pos / 16]!!.blocks[blockPos.toChunkPos().short] = block
    }

    @ExperimentalStdlibApi
    operator fun set(pos: Position, chunk: Chunk) {
        chunks[pos] = chunk
    }

    @ExperimentalStdlibApi
    operator fun get(pos: Position): Chunk {
        if(!chunks.containsKey(pos))
            chunks[pos] = Chunk(this, pos)
        return chunks[pos]!!
    }

    fun isChunkPosValid(pos: Position) = pos.inRange(LongRange(-32, 32), LongRange(0, 8), LongRange(-32, 32))

    fun newEntityId(): Int {
        val id = (0..Int.MAX_VALUE).first { !idsInUse.contains(it) }
        idsInUse.add(id)
        return id
    }

    @ExperimentalStdlibApi
    fun generateChunkRange(xr: LongRange, yr: LongRange, zr: LongRange) {
        logger.info("Generating ${xr.count() * yr.count() * zr.count()} chunks.")
        val runnables = mutableListOf<Runnable>()
        val ref = AtomicReference(this)
        xr.forEach { x ->
            yr.forEach { y ->
                zr.forEach { z ->
                    runnables.add {
                        ref.getAndUpdate {
//                            it.logger.info("Generating $x, $y, $z")
                            it[Position(x,y,z)] = if(noise == null) Chunk(Position(x,y,z)) else Chunk(it, Position(x,y,z))
                            it
                        }
                    }
                }
            }
        }
        logger.info("Async array created, executing")
        val start = System.nanoTime()
        Tasks.executeAllAsync(*runnables.toTypedArray()).await()
        val end = System.nanoTime()
        logger.info("Took ${(end - start).toDouble() / 1000000.0}ms to generate ${xr.count() * yr.count() * zr.count()} chunks.")
    }

    fun getNoise(x: Long, bx: Byte, y: Long, by: Byte, z: Long, bz: Byte, index: Float) =
        ((noise!!.GetNoise(
            (x * 16).toFloat() + bx.toFloat(),
            (z * 16).toFloat() + bz.toFloat(),
            index) + 1.0) / 8.0) *
            64.0 - (y.toFloat() * 16.0 + by.toFloat())

    fun heightMap(x: Long, y: Long, z: Long) =
        Array(16) { bx ->
            Array(16) { by ->
                Array(16) { bz ->
                    getNoise(x, bx.toByte(), y, by.toByte(), z, bz.toByte(), 0.0f) +
                    getNoise(x, bx.toByte(), y, by.toByte(), z, bz.toByte(), 2.0f)
                }
            }
        }

    companion object {
        fun createIfNotExist(s: String) {
//            TODO()
        }

        fun readNBT(s: String): World? {
//            val worldNBT = NBTUtil.read(File(s))
//            val worldData = worldNBT.tag as CompoundTag
//            val name = worldData.getString("Name")
//            TODO()
            return World(Dimension.OVERWORLD, FastNoiseLite(0))
        }

        @ExperimentalStdlibApi
        fun noise(seed: Long): World {
            val world = World(Dimension.OVERWORLD, FastNoiseLite(seed.toInt()))
            world.noise!!.SetNoiseType(FastNoiseLite.NoiseType.Perlin)
            world.seed = seed
            world.generateChunkRange(-32L..32L, 0L..15L, -32L..32L)
            return world
        }

        @ExperimentalStdlibApi
        fun flat(): World {
            val world = World(Dimension.OVERWORLD, null)
            world.generateChunkRange(-32L..32L, 0L..15L, -32L..32L)
            return world
        }
    }
}