package com.liamcoalstudio.kettle.networking.java.handshake

import com.liamcoalstudio.kettle.helpers.Buffer
import com.liamcoalstudio.kettle.logging.ConsoleLogger
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.ClientState
import com.liamcoalstudio.kettle.networking.main.packets.Packet
import com.liamcoalstudio.kettle.networking.main.packets.Producer
import com.liamcoalstudio.kettle.networking.main.packets.ServerState

class C2SHandshakePacket : Packet(0x00, ClientState.Status.Handshake), Producer<Packet> {
    private val logger = ConsoleLogger(C2SHandshakePacket::class)
    var protocolNumber: Int = -1
    var address = ""
    var port = (-1).toShort()
    var next = 0

    override fun read(buf: Buffer) {
        protocolNumber = buf.getVarInt()
        address = buf.getString()
        port = buf.getShort()
        next = buf.getVarInt()
    }

    override fun updateOnRead(state: ServerState, client: Client) {
        when (next) {
            1 -> client.status = ClientState.Status.Status
            2 -> client.status = ClientState.Status.Login
            else -> throw NotImplementedError("$next is invalid")
        }
        logger.info("Changed state to ${client.status}")
    }

    override fun produce(serverState: ServerState): Packet = C2SHandshakePacket()
}