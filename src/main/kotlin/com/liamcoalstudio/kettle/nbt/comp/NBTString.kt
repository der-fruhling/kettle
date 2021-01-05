package com.liamcoalstudio.kettle.nbt.comp

import com.liamcoalstudio.kettle.nbt.NBTTag
import com.liamcoalstudio.kettle.helpers.Buffer
import kotlin.reflect.KClass

class NBTString(val bytes: CharArray, parent: NBTTag? = null) : NBTTag(parent) {
    constructor(s: String, parent: NBTTag? = null) : this(s.toCharArray(), parent)

    override fun <T : NBTTag> isOf(clazz: KClass<T>) = clazz == NBTString::class

    override fun write(buffer: Buffer) {
        buffer.addByte(id)
        buffer.addNBTString(parent!!.nameOf(this)!!)
        writeInArray(buffer)
    }

    override fun writeInArray(buffer: Buffer) {
        buffer.addShort(bytes.size.toShort())
        bytes.forEach { c -> buffer.addByte(c.toByte()) }
    }

    override val id: Byte = 0x08
}