package com.liamcoalstudio.kettle.networking.java.login

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.networking.main.packets.Packet

class S2CSetCompression(val threshold: Int) : Packet(0x03, null) {
    override fun write(buf: Buffer) {
        buf.addVarInt(threshold)
    }
}