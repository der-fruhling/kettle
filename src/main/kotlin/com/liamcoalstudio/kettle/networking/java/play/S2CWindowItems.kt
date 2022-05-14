package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.logging.ConsoleLogger
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.world.Slot

class S2CWindowItems(val wid: Byte, val array: Array<Slot>) : Packet(0x13, null) {
    override fun write(buf: Buffer) {
        buf.addByte(wid)
        buf.addShort(array.size.toShort())
        for (slot in array) slot.write(buf)
        ConsoleLogger(S2CWindowItems::class).info("Sent slots: ${array.contentToString()}")
    }
}