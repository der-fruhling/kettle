package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.helpers.Dimension
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.servers.main.KettleServer

class S2CUpdateViewPosition(val x: Int, val z: Int) : Packet(0x40, null) {
    override fun write(buf: Buffer) {
        buf.addVarInt(x)
        buf.addVarInt(z)
    }

    @ExperimentalStdlibApi
    override fun updateOnWrite(state: ServerState, client: Client) {
        val p = KettleServer.GLOBAL!!.get().worlds[Dimension.OVERWORLD]!!.players.find { it.client == client }!!
        p.updateChunks()
    }
}