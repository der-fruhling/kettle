package com.liamcoalstudio.kettle.networking.main.packets

abstract class PacketObject<T>(var obj: T) {
    constructor(array: Array<Byte>) : this(array as T)
    abstract fun toByteArray(): Array<Byte>
}
