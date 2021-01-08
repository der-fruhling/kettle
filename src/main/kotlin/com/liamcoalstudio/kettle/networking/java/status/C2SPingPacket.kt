package com.liamcoalstudio.kettle.networking.java.status

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.ClientState
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.networking.main.packets.Producer
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import kotlin.properties.Delegates

class C2SPingPacket : Packet(0x01, ClientState.Status.Status), Producer<Packet> {
    var rand by Delegates.notNull<Long>()

    override fun read(buf: Buffer) {
        rand = buf.getLong()
    }

    override fun updateOnRead(state: ServerState, client: Client) {
        client.send(S2CPongPacket(rand))
    }

    override fun produce(serverState: ServerState) = C2SPingPacket()
}