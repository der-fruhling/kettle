package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.networking.main.packets.Packet

class S2CUpdateViewPosition(val x: Int, val z: Int) : Packet(0x40, null) {
    override fun write(buf: Buffer) {
        buf.addVarInt(x)
        buf.addVarInt(z)
    }
}