package com.liamcoalstudio.kettle.nbt.comp

import com.liamcoalstudio.kettle.nbt.NBTTag
import com.liamcoalstudio.kettle.helpers.Buffer
import kotlin.reflect.KClass

class NBTIntArray(val ints: IntArray, parent: NBTTag? = null) : NBTTag(parent) {
    override fun <T : NBTTag> isOf(clazz: KClass<T>) = clazz == NBTIntArray::class

    override fun write(buffer: Buffer) {
        buffer.addByte(id)
        buffer.addNBTString(parent!!.nameOf(this)!!)
        writeInArray(buffer)
    }

    override fun writeInArray(buffer: Buffer) {
        buffer.addInt(ints.size)
        ints.forEach { i -> buffer.addInt(i) }
    }

    override val id: Byte = 0x0b
}