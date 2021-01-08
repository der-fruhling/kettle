package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.networking.main.packets.Packet

class S2CHeldItemChange(val slotId: Byte) : Packet(0x3f, null) {
    override fun write(buf: Buffer) {
        buf.addByte(slotId)
    }
}