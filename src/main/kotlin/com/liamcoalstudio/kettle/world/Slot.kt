package com.liamcoalstudio.kettle.world

import com.liamcoalstudio.kettle.helpers.Block
import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.helpers.Item
import com.liamcoalstudio.kettle.servers.main.KettleServer
import net.querz.nbt.io.NBTDeserializer
import net.querz.nbt.io.NBTSerializer
import net.querz.nbt.io.NamedTag
import net.querz.nbt.tag.CompoundTag

data class Slot(val present: Boolean, val id: Int, val count: Byte, val nbt: CompoundTag) {
    val item: Item? get() = Item.values().find { it.id == id }
    val block: Block? get() = item?.block

    constructor() : this(false, 0, 0, CompoundTag())

    fun write(buffer: Buffer) {
        buffer.addBoolean(present)
        if(present) {
            buffer.addVarInt(id)
            buffer.addByte(count)
            buffer.addBuffer(Buffer(NBTSerializer(false).toBytes(NamedTag("NBT", nbt))))
        }
    }

    override fun toString(): String {
        return if(present)
            "Slot(present=$present, id=$id, count=$count, nbt=$nbt, item=$item, block=$block)"
        else
            "Slot<empty>"
    }

    companion object {
        fun read(buffer: Buffer): Slot {
            val present = buffer.getBoolean()
            return if(present) {
                val id = buffer.getVarInt()
                val count = buffer.getByte()
                val nbt = if(buffer.peekByte() != 0.toByte())
                    NBTDeserializer(false).fromBytes(buffer.getBuffer(buffer.bytesLeft).array).tag as CompoundTag
                else
                    CompoundTag()
                Slot(true, id, count, nbt)
            } else Slot()
        }
    }
}
