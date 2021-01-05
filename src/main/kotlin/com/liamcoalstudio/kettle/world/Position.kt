package com.liamcoalstudio.kettle.world

data class Position(val x: Long, val y: Long, val z: Long) {
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

    operator fun div(long: Long): Position {
        return Position(x / long, y / long, z / long)
    }

    operator fun rem(long: Long): Position {
        return Position(x % long, y % long, z % long)
    }

    operator fun plus(long: Long): Position {
        return Position(x + long, y + long, z + long)
    }

    operator fun div(other: Position): Position {
        return Position(x / other.x, y / other.y, z / other.z)
    }

    operator fun rem(other: Position): Position {
        return Position(x % other.x, y % other.y, z % other.z)
    }

    operator fun plus(other: Position): Position {
        return Position(x + other.x, y + other.y, z + other.z)
    }

    fun inRange(xRange: LongRange, yRange: LongRange, zRange: LongRange): Boolean {
        return xRange.contains(x) and yRange.contains(y) and zRange.contains(z)
    }
}
