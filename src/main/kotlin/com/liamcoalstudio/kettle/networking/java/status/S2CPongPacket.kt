package com.liamcoalstudio.kettle.networking.java.status

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.networking.main.packets.Packet

class S2CPongPacket(val rand: Long) : Packet(0x01, null) {
    override fun write(buf: Buffer) {
        buf.addLong(rand)
    }
}