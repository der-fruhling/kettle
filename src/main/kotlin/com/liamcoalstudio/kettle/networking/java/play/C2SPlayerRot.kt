package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.helpers.Dimension
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.ClientState
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.networking.main.packets.Producer
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.servers.main.KettleServer

class C2SPlayerRot : Packet(0x14, ClientState.Status.Play), Producer<Packet> {
    var yaw: Float = 0.0f
    var pitch: Float = 0.0f

    override fun read(buf: Buffer) {
        yaw = buf.getFloat()
        pitch = buf.getFloat()
        buf.getBoolean()
    }

    override fun updateOnRead(state: ServerState, client: Client) {
        val player = KettleServer.GLOBAL!!.get().worlds[Dimension.OVERWORLD]!!.players.find { it.client == client }!!
        player.yaw = yaw
        player.pitch = pitch
    }

    override fun produce(serverState: ServerState) = C2SPlayerRot()
}