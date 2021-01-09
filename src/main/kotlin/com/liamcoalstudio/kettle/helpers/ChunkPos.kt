package com.liamcoalstudio.kettle.helpers

import kotlin.experimental.and
import kotlin.experimental.or

class ChunkPos(var x: Byte, var y: Byte, var z: Byte) {
    @ExperimentalStdlibApi
    var short: Short
    get() = (0.toShort() or
            (x.toShort() and 0b1111) or
            (z.toShort() and 0b1111).rotateLeft(4) or
            (y.toShort() and 0b1111).rotateRight(4))
    set(v) {
        x = (v and 0b1111).toByte()
        z = (v.rotateRight(4) and 0b1111).toByte()
        y = (v.rotateRight(8) and 0b1111).toByte()
    }

    @ExperimentalStdlibApi
    constructor(short: Short) : this(0, 0, 0) {
        this.short = short
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ChunkPos) return false

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.toInt()
        result = 31 * result + y
        result = 31 * result + z
        return result
    }
}