package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.networking.main.packets.Packet

class S2CWorldBorderInit(val size: Double) : Packet(0x3d, null) {
    override fun write(buf: Buffer) {
        buf.addVarInt(3)
        buf.addDouble(0.0)
        buf.addDouble(0.0)
        buf.addDouble(0.0)
        buf.addDouble(size)
        buf.addVarLong(0)
        buf.addVarInt(size.toInt() / 2)
        buf.addVarInt(0)
        buf.addVarInt(0)
    }
}