package com.liamcoalstudio.kettle.networking.main.packets

import com.google.common.primitives.Ints
import com.google.common.primitives.Longs
import com.google.common.primitives.Shorts
import com.liamcoalstudio.kettle.helpers.Buffer
import java.nio.ByteBuffer
import java.util.*
import kotlin.experimental.or

open class BytePacketObject(v: Byte) : PacketObject<Byte>(v) {
    override fun toByteArray() = arrayOf(obj)
    constructor(array: Array<Byte>) : this(array[0])
}
open class ShortPacketObject(v: Short) : PacketObject<Short>(v) {
    override fun toByteArray() = Shorts.toByteArray(obj).toTypedArray()
    constructor(array: Array<Byte>) : this(Shorts.fromByteArray(array.toByteArray()))
}
open class IntPacketObject(v: Int) : PacketObject<Int>(v) {
    override fun toByteArray() = Ints.toByteArray(obj).toTypedArray()
    constructor(array: Array<Byte>) : this(Ints.fromByteArray(array.toByteArray()))
}
open class LongPacketObject(v: Long) :  PacketObject<Long>(v) {
    override fun toByteArray() = Longs.toByteArray(obj).toTypedArray()
    constructor(array: Array<Byte>) : this(Longs.fromByteArray(array.toByteArray()))
}
open class FloatPacketObject(v: Float) : PacketObject<Float>(v) {
    override fun toByteArray() = Ints.toByteArray(java.lang.Float.floatToIntBits(obj)).toTypedArray()
    constructor(array: Array<Byte>) : this(Float.fromBits(Ints.fromByteArray(array.toByteArray())))
}
open class DoublePacketObject(v: Double) : PacketObject<Double>(v) {
    override fun toByteArray() = Longs.toByteArray(java.lang.Double.doubleToLongBits(obj)).toTypedArray()
    constructor(array: Array<Byte>) : this(Double.fromBits(Longs.fromByteArray(array.toByteArray())))
}
open class BooleanPacketObject(v: Boolean) : PacketObject<Boolean>(v) {
    override fun toByteArray(): Array<Byte> = arrayOf(if(obj) 0x01 else 0x00)
    constructor(array: Array<Byte>) : this(array[0] == 0x01.toByte())
}
open class VarIntPacketObject(v: Int) : PacketObject<Int>(v) {
    override fun toByteArray(): Array<Byte> {
        val buf = ByteBuffer.allocate(16)
        do {
            var temp = (obj and 0b01111111).toByte()
            obj = obj ushr 7
            if (obj != 0) {
                temp = temp or 0b10000000.toByte()
            }
            buf.put(temp)
        } while (obj != 0)
        val size = buf.flip().limit()
        return buf.array().slice(0 until size).toTypedArray()
    }

    constructor(buf: Buffer) : this(fromByteArray(buf))

    companion object {
        private fun fromByteArray(buf: Buffer): Int {
            var numRead = 0
            var result = 0
            var read: Byte
            do {
                read = buf.getByte()
                val value: Int = read.toInt() and 127
                result = result or (value shl 7 * numRead)
                numRead++
                if (numRead > 5) {
                    throw RuntimeException("VarInt is too big")
                }
            } while (read.toInt() and 128 != 0)
            return result
        }
    }
}
open class VarLongPacketObject(v: Long) : PacketObject<Long>(v) {
    override fun toByteArray(): Array<Byte> {
        val buf = ByteBuffer.allocate(16)
        do {
            var temp = (obj and 127).toByte()
            obj = obj ushr 7
            if (obj != 0L) {
                temp = temp or 0b10000000.toByte()
            }
            buf.put(temp)
        } while (obj != 0L)
        val size = buf.flip().limit()
        return buf.array().slice(0 until size).toTypedArray()
    }

    constructor(buf: Buffer) : this(fromByteArray(buf))

    companion object {
        private fun fromByteArray(buf: Buffer): Long {
            var numRead = 0
            var result = 0L
            var read: Byte
            do {
                read = buf.getByte()
                val value: Int = read.toInt() and 127
                result = result or ((value shl 7 * numRead).toLong())
                numRead++
                if (numRead > 10) {
                    throw RuntimeException("VarInt is too big")
                }
            } while (read.toInt() and 128 != 0)
            return result
        }
    }
}
open class StringPacketObject(v: String, val short: Boolean) : PacketObject<String>(v) {
    override fun toByteArray(): Array<Byte> =
        (if(!short) VarIntPacketObject(obj.length) else ShortPacketObject(obj.length.toShort()))
            .toByteArray().plus(obj.toByteArray().toTypedArray())
    constructor(buf: Buffer, short: Boolean) : this(readString(buf.getVarInt(), buf), short)

    companion object {
        fun readString(size: Int, buf: Buffer): String {
            var string = ""
            for (i in 0 until size) {
                string += buf.getByte().toChar()
            }
            return string
        }
    }
}
open class UUIDPacketObject(v: UUID) : PacketObject<UUID>(v) {
    override fun toByteArray() =
        LongPacketObject(obj.mostSignificantBits).toByteArray() +
        LongPacketObject(obj.leastSignificantBits).toByteArray()

    constructor(buf: Buffer) : this(UUID(buf.getLong(), buf.getLong()))
}