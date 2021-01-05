package com.liamcoalstudio.kettle.nbt.comp

import com.liamcoalstudio.kettle.nbt.NBTTag
import com.liamcoalstudio.kettle.helpers.Buffer
import kotlin.reflect.KClass

class NBTByteArray(val bytes: ByteArray, parent: NBTTag? = null) : NBTTag(parent) {
    override fun <T : NBTTag> isOf(clazz: KClass<T>) = clazz == NBTByteArray::class

    override fun write(buffer: Buffer) {
        buffer.addByte(id)
        buffer.addNBTString(parent!!.nameOf(this)!!)
        writeInArray(buffer)
    }

    override fun writeInArray(buffer: Buffer) {
        buffer.addInt(bytes.size)
        bytes.forEach { b -> buffer.addByte(b) }
    }

    override val id: Byte = 0x07
}