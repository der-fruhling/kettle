package com.liamcoalstudio.kettle.nbt.prim

import com.liamcoalstudio.kettle.nbt.NBTTag
import com.liamcoalstudio.kettle.helpers.Buffer
import kotlin.reflect.KClass

class NBTByte(private val value: Byte, parent: NBTTag? = null) : NBTTag(parent) {
    override fun<T : NBTTag> isOf(clazz: KClass<T>) = clazz == NBTByte::class

    override fun write(buffer: Buffer) {
        buffer.addByte(id)
        buffer.addNBTString(parent!!.nameOf(this)!!)
        writeInArray(buffer)
    }

    override fun writeInArray(buffer: Buffer) {
        buffer.addByte(value)
    }

    override val id: Byte = 0x01

    companion object {
        const val nfalse: Byte = 0
        const val ntrue: Byte = 1
    }
}