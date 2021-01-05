package com.liamcoalstudio.kettle.nbt.comp

import com.liamcoalstudio.kettle.nbt.NBTTag
import com.liamcoalstudio.kettle.nbt.prim.NBTByte
import com.liamcoalstudio.kettle.helpers.Buffer
import kotlin.reflect.KClass

class NBTList(private val map: MutableList<NBTTag> = MutableList(0) { NBTByte(0) }, parent: NBTTag? = null) : NBTTag(parent) {
    override fun <T : NBTTag> isOf(clazz: KClass<T>) = clazz == NBTList::class

    constructor(size: Int, parent: NBTTag? = null) : this(MutableList(size) { NBTByte(0) }, parent)

    override fun write(buffer: Buffer) {
        buffer.addByte(id)
        buffer.addNBTString(parent!!.nameOf(this)!!)
        writeInArray(buffer)
    }

    override fun writeInArray(buffer: Buffer) {
        buffer.addByte(if(map.isEmpty()) 0 else map[0].id)
        buffer.addInt(map.size)
        map.forEach { tag -> tag.writeInArray(buffer) }
    }

    override val id: Byte = 0x09

    fun add(nbtTag: NBTTag) {
        nbtTag.parent = this
        map.add(nbtTag)
    }

    override fun get(int: Int): NBTTag = map[int]
    override fun set(int: Int, nbtTag: NBTTag) { map[int] = nbtTag }
}