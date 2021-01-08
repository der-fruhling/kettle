package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.networking.main.packets.Packet

class S2CDeclareCommands : Packet(0x10, null) {
    override fun write(buf: Buffer) {
        buf.addVarInt(1)
        buf.addByte(0b00000000)
        buf.addVarInt(0)
        buf.addVarInt(0)
    }
}