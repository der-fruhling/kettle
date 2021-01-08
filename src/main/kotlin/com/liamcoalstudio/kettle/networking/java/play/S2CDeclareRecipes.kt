package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.networking.main.packets.Packet

class S2CDeclareRecipes : Packet(0x5a, null) {
    override fun write(buf: Buffer) {
        buf.addVarInt(0)
        // todo add actual recipes
    }
}