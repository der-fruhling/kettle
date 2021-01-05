package com.liamcoalstudio.kettle.nbt

import com.liamcoalstudio.kettle.nbt.prim.NBTByte
import com.liamcoalstudio.kettle.helpers.Buffer
import kotlin.reflect.KClass
import kotlin.reflect.cast

abstract class NBTTag(var parent: NBTTag?) {
    abstract fun<T : NBTTag> isOf(clazz: KClass<T>): Boolean
    abstract fun write(buffer: Buffer)
    abstract fun writeInArray(buffer: Buffer)
    abstract val id: Byte

    open fun nameOf(value: NBTTag): String? = ""
    open fun<T : NBTTag> to(clazz: KClass<T>): T? = if(isOf(clazz)) clazz.cast(this) else null

    open operator fun get(name: String): NBTTag = NBTByte(0)
    open operator fun get(int: Int): NBTTag = NBTByte(0)
    open operator fun set(name: String, nbtTag: NBTTag) {}
    open operator fun set(int: Int, nbtTag: NBTTag) {}
}