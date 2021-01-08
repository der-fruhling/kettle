package com.liamcoalstudio.kettle.helpers

import com.google.common.primitives.*
import com.liamcoalstudio.kettle.networking.main.packets.*
import java.util.*

/**
 * This buffer is used for packets to provide a nice way of adding
 * and getting data from a sequence of bytes.
 */
class Buffer() {
    val hasMore: Boolean get() = readIndex < list.size
    val bytesLeft: Int get() =  list.size - readIndex
    private var list: MutableList<Byte> = MutableList(0) { 0 }
    private var readIndex = 0
    var array
        get() = list.toByteArray()
        set(value) { list = value.toMutableList() }

    constructor(array: ByteArray) : this() {
        this.array = array
    }

    private fun add(packetObject: PacketObject<*>) {
        list.addAll(packetObject.toByteArray())
    }

    private fun add(array: ByteArray) {
        this.array = this.array + array
    }

    private fun get(size: Int): Array<Byte> {
        val ret = Array(size) { i -> list[i + readIndex] }
        readIndex += size
        return ret
    }

    fun addByte(byte: Byte) = add(BytePacketObject(byte))
    fun addShort(short: Short) = add(ShortPacketObject(short))
    fun addInt(int: Int) = add(IntPacketObject(int))
    fun addLong(long: Long) = add(LongPacketObject(long))
    fun addFloat(float: Float) = add(FloatPacketObject(float))
    fun addDouble(double: Double) = add(DoublePacketObject(double))
    fun addVarInt(int: Int) = add(VarIntPacketObject(int))
    fun addVarLong(long: Long) = add(VarLongPacketObject(long))
    fun addBoolean(boolean: Boolean) = add(BooleanPacketObject(boolean))
    fun addString(string: String) = add(StringPacketObject(string, false))
    fun addBuffer(buffer: Buffer) = add(buffer.array)
    fun addUUID(uuid: UUID) = add(UUIDPacketObject(uuid))
    fun addNBTString(string: String) = add(StringPacketObject(string, true))

    fun getByte() = BytePacketObject(get(1)).obj
    fun getShort() = ShortPacketObject(get(Shorts.BYTES)).obj
    fun getInt() = IntPacketObject(get(Ints.BYTES)).obj
    fun getLong() = LongPacketObject(get(Longs.BYTES)).obj
    fun getFloat() = FloatPacketObject(get(Floats.BYTES)).obj
    fun getDouble() = DoublePacketObject(get(Doubles.BYTES)).obj
    fun getVarInt() = VarIntPacketObject(this).obj
    fun getVarLong() = VarLongPacketObject(this).obj
    fun getBoolean() = BooleanPacketObject(get(1)).obj
    fun getString() = StringPacketObject(this, false).obj
    fun getBuffer(size: Int) = Buffer(get(size).toByteArray())
    fun getUUID() = UUIDPacketObject(this).obj
    fun getNBTString() = StringPacketObject(this, true).obj
}