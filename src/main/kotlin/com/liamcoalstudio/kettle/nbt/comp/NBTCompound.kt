package com.liamcoalstudio.kettle.nbt.comp

import com.liamcoalstudio.kettle.nbt.NBTTag
import com.liamcoalstudio.kettle.helpers.Buffer
import kotlin.reflect.KClass

open class NBTCompound(parent: NBTTag? = null) : NBTTag(parent) {
    private val map = HashMap<String, NBTTag>()

    override fun <T : NBTTag> isOf(clazz: KClass<T>) = clazz == NBTCompound::class

    override fun write(buffer: Buffer) {
        buffer.addByte(id)
        buffer.addNBTString(parent!!.nameOf(this)!!)
        writeInArray(buffer)
    }

    override fun writeInArray(buffer: Buffer) {
        map.forEach { (_, tag) ->
            tag.write(buffer)
        }
        buffer.addByte(0)
    }

    override val id: Byte = 0x0a

    override operator fun set(name: String, nbtTag: NBTTag) {
        nbtTag.parent = this
        map[name] = nbtTag
    }

    override operator fun get(name: String): NBTTag = map[name]!!

    override fun nameOf(value: NBTTag): String? {
        return map.entries.find { it.value == value }?.key
    }
}