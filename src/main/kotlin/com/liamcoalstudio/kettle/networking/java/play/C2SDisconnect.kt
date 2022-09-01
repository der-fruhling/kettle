package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Dimension
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.ClientState
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.networking.main.packets.Producer
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.servers.main.KettleServer

class C2SDisconnect : Packet(0, ClientState.Status.Play), Producer<Packet> {

    override fun updateOnRead(state: ServerState, client: Client) {
        val world = KettleServer.GLOBAL!!.get().worlds[Dimension.OVERWORLD]!!
        val player = world.players.find { it.client == client }!!
        world.players.remove(player)
        KettleServer.GLOBAL!!.get().worlds[Dimension.OVERWORLD] = world
    }

    override fun produce(serverState: ServerState): Packet {
        TODO("not implemented")
    }
}