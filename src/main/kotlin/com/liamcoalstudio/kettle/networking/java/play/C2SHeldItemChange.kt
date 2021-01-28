package com.liamcoalstudio.kettle.networking.java.play

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.ClientState
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.networking.main.packets.Producer
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.servers.main.KettleServer

class C2SHeldItemChange : Packet(0x25, ClientState.Status.Play), Producer<Packet> {
    private var slot = 0

    override fun read(buf: Buffer) {
        slot = buf.getShort().toInt()
    }

    override fun updateOnRead(state: ServerState, client: Client) {
        KettleServer.player(client).selected = slot
    }

    override fun produce(serverState: ServerState): Packet = C2SHeldItemChange()
}