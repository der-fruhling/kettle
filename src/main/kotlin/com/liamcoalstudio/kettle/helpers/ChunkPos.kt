package com.liamcoalstudio.kettle.helpers

import kotlin.experimental.or

class ChunkPos(var x: Byte, var y: Byte, var z: Byte) {
    @ExperimentalStdlibApi
    var short: Short
        get() = (0 or (x.toInt()) or (y.rotateLeft(4).toInt()) or (z.rotateLeft(8).toInt())).toShort()
        set(v) {
            x = (v or 0b1111).toByte()
            z = (v.rotateLeft(4) or 0b1111).toByte()
            y = (v.rotateLeft(8) or 0b1111).toByte()
        }

    @ExperimentalStdlibApi
    constructor(short: Short) : this(0, 0, 0) {
        this.short = short
    }
}