package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.networking.main.packets.Packet

class S2CSpawnPosition : Packet(0x42, null) {
    override fun write(buf: Buffer) {
        buf.addLong(0)
    }
}