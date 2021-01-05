package com.liamcoalstudio.kettle.nbt.comp

import com.liamcoalstudio.kettle.nbt.NBTTag
import com.liamcoalstudio.kettle.helpers.Buffer
import kotlin.reflect.KClass

class NBTLongArray(val longs: LongArray, parent: NBTTag? = null) : NBTTag(parent) {
    override fun <T : NBTTag> isOf(clazz: KClass<T>) = clazz == NBTLongArray::class

    override fun write(buffer: Buffer) {
        buffer.addByte(id)
        buffer.addNBTString(parent!!.nameOf(this)!!)
        writeInArray(buffer)
    }

    override fun writeInArray(buffer: Buffer) {
        buffer.addInt(longs.size)
        longs.forEach { l -> buffer.addLong(l) }
    }

    override val id: Byte = 0x0c
}