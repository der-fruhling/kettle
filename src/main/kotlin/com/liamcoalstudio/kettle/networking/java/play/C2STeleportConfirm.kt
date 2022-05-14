package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.actions.TeleportAction
import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.helpers.Dimension
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.ClientState
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.networking.main.packets.Producer
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.servers.main.KettleServer

class C2STeleportConfirm : Packet(0x00, ClientState.Status.Play), Producer<Packet> {
    var tid: Int = 0

    override fun read(buf: Buffer) {
        tid = buf.getVarInt()
    }

    override fun updateOnRead(state: ServerState, client: Client) {
        val player = KettleServer.GLOBAL!!.get().worlds[Dimension.OVERWORLD]!!.players.find { it.client == client }!!
        if (player.actions[tid] is TeleportAction) {
            player.actions[tid]!!.perform()
            player.actions.remove(tid)
        }
    }

    override fun produce(serverState: ServerState) = C2STeleportConfirm()
}