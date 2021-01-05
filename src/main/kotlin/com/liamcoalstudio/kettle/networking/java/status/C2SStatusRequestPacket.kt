package com.liamcoalstudio.kettle.networking.java.status

import com.liamcoalstudio.kettle.logging.ConsoleLogger
import com.liamcoalstudio.kettle.networking.main.Client
import com.liamcoalstudio.kettle.networking.main.packets.*

class C2SStatusRequestPacket(val state: ServerState?) : Packet(0x00, ClientState.Status.Status), Producer<Packet> {
    private val logger = ConsoleLogger(C2SStatusRequestPacket::class)

    override fun updateOnRead(state: ServerState, client: Client) {
        logger.info("Sending status")
        client.send(S2CStatusResponsePacket(state))
    }

    override fun produce(serverState: ServerState): Packet = C2SStatusRequestPacket(serverState)
}