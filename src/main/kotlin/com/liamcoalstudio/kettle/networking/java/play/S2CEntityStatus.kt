package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.networking.main.packets.Packet

class S2CEntityStatus(val eid: Int, val action: Byte) : Packet(0x1a, null) {
    override fun write(buf: Buffer) {
        buf.addInt(eid)
        buf.addByte(action)
    }
}