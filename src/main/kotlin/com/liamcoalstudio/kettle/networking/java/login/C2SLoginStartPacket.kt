package com.liamcoalstudio.kettle.networking.java.login

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.ClientState
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.networking.main.packets.Producer
import com.liamcoalstudio.kettle.networking.main.packets.ServerState
import com.liamcoalstudio.kettle.servers.java.JavaServer

class C2SLoginStartPacket : Packet(0x00, ClientState.Status.Login), Producer<Packet> {
    lateinit var username: String

    override fun read(buf: Buffer) {
        username = buf.getString()
    }

    override fun updateOnRead(state: ServerState, client: Client) {
        client.send(S2CSetCompression(512))
        JavaServer.GLOBAL_CONTROLLER!!.get().execute {
            client.nodeManager.enableCompression(512)
        }
        client.send(S2CLoginSuccessPacket(username))
    }

    override fun produce(serverState: ServerState) = C2SLoginStartPacket()
}