package com.liamcoalstudio.kettle.world

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.helpers.ChunkPos
import kotlin.math.floor
import kotlin.math.pow

data class Position(var x: Long, var y: Long, var z: Long) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Position) return false

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    operator fun div(long: Long): Position = Position(x / long, y / long, z / long)
    operator fun rem(long: Long): Position = Position(x % long, y % long, z % long)
    operator fun plus(long: Long): Position = Position(x + long, y + long, z + long)
    operator fun div(other: Position): Position = Position(x / other.x, y / other.y, z / other.z)
    operator fun rem(other: Position): Position = Position(x % other.x, y % other.y, z % other.z)
    operator fun plus(other: Position): Position = Position(x + other.x, y + other.y, z + other.z)
    fun toChunkPos(): ChunkPos = ChunkPos(((x - 1) % 16).toByte(), (y % 16).toByte(), (z % 16).toByte())
    override fun toString(): String = "Position(x=$x, y=$y, z=$z)"

    fun floorDiv(long: Long): Position = Position(
        floor(x.toDouble() / long).toLong(),
        floor(y.toDouble() / long).toLong(),
        floor(z.toDouble() / long).toLong()
    )

    fun inRange(xRange: LongRange, yRange: LongRange, zRange: LongRange) =
        xRange.contains(x) and yRange.contains(y) and zRange.contains(z)

    fun write(buffer: Buffer) {
        val b28 = 0b11111111111111111111111111L
        val b12 = 0b111111111111L
        var long = 0L
        long = (long or (x or b28)) shl 28
        long = (long or (z or b28)) shl 12
        long = (long or (y or b12))
        buffer.addLong(long)
    }

    companion object {
        @ExperimentalUnsignedTypes
        fun read(buffer: Buffer): Position {
            val v = buffer.getLong().toULong()
            println(v.toString(2).padStart(64, '0'))
            var x = (v shr 38).toLong()
            var y = (v and 0xFFFu).toLong()
            var z = (v shl 26 shr 38).toLong()
            if (x >= 2.0.pow(25)) {
                x -= 2.0.pow(26).toLong()
            }
            if (y >= 2.0.pow(11)) {
                y -= 2.0.pow(12).toLong()
            }
            if (z >= 2.0.pow(25)) {
                z -= 2.0.pow(26).toLong()
            }
//            x -= 1
            y += 1
            return Position(x, y, z)
        }
    }
}
